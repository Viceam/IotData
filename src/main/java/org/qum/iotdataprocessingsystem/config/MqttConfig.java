package org.qum.iotdataprocessingsystem.config;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.influxdb.InfluxDB;
import org.influxdb.dto.Point;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.core.MessageProducer;
import org.springframework.integration.mqtt.core.DefaultMqttPahoClientFactory;
import org.springframework.integration.mqtt.core.MqttPahoClientFactory;
import org.springframework.integration.mqtt.inbound.MqttPahoMessageDrivenChannelAdapter;
import org.springframework.integration.mqtt.outbound.MqttPahoMessageHandler;
import org.springframework.integration.mqtt.support.DefaultPahoMessageConverter;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageHandler;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.TimeUnit;

@Configuration
public class MqttConfig {

    public static final String CHANNEL_NAME_IN = "mqttInputChannel";

    @Bean
    public MqttPahoClientFactory mqttClientFactory() {
        DefaultMqttPahoClientFactory factory = new DefaultMqttPahoClientFactory();
        MqttConnectOptions options = new MqttConnectOptions();
        options.setServerURIs(new String[]{"tcp://localhost:1883"});
        options.setConnectionTimeout(10);
        options.setKeepAliveInterval(20);
        factory.setConnectionOptions(options);
        return factory;
    }

    @Bean
    public MessageChannel mqttInputChannel() {
        return new DirectChannel();
    }

    @Bean
    public MessageProducer inbound() {
        MqttPahoMessageDrivenChannelAdapter adapter = new MqttPahoMessageDrivenChannelAdapter(
                "mqttClient-consumer", mqttClientFactory(), "sensor/data");
        adapter.setCompletionTimeout(5000);
        DefaultPahoMessageConverter converter = new DefaultPahoMessageConverter();
        adapter.setConverter(converter);
        adapter.setQos(1);
        adapter.setOutputChannel(mqttInputChannel());
        return adapter;
    }

    @Bean
    @ServiceActivator(inputChannel = CHANNEL_NAME_IN)
    public MessageHandler handler(InfluxDB influxDB) {
        return message -> {
            String payload = message.getPayload().toString();
            String topic = message.getHeaders().get("mqtt_receivedTopic").toString();

            if (!"sensor/data".equals(topic)) {
                System.out.println("Message discarded: Topic [" + topic + "], Payload: " + payload);
                return;
            }

            try {
                ObjectMapper mapper = new ObjectMapper();
                JsonNode root = mapper.readTree(payload);

                String deviceId = root.get("device_id").asText();
                // 修正时间解析部分
                String timestampStr = root.get("timestamp").asText();

                // 关键修改点：使用 ZonedDateTime 并包含时区格式
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSSSSXXX");
                ZonedDateTime zonedDateTime = ZonedDateTime.parse(timestampStr, formatter);
                Instant timestamp = zonedDateTime.toInstant();

                double temperature = root.get("temperature").asDouble();
                double pressure = root.get("pressure").asDouble();
                double vibration = root.get("vibration").asDouble();
                double humidity = root.get("humidity").asDouble();
                String equipment = root.get("equipment").asText();
                String location = root.get("location").asText();
                int faulty = root.get("faulty").asInt();

                Point point = Point.measurement("sensor_data")
                        .time(timestamp.toEpochMilli(), TimeUnit.MILLISECONDS)
                        .tag("equipment_id", deviceId)
                        .tag("equipment_type", equipment)
                        .tag("location", location)
                        .tag("faulty", String.valueOf(faulty))
                        .addField("temperature", temperature)
                        .addField("pressure", pressure)
                        .addField("vibration", vibration)
                        .addField("humidity", humidity)
                        .build();

                influxDB.write(point);
                System.out.println("[✅] Data written to InfluxDB: " + deviceId);

            } catch (Exception e) {
                System.err.println("❌ Error processing message: " + e.getMessage());
                e.printStackTrace();
            }
        };
    }

}

