package org.qum.iotdataprocessingsystem.config;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.influxdb.InfluxDB;
import org.influxdb.dto.Point;
import org.qum.iotdataprocessingsystem.pojo.EquipmentData;
import org.qum.iotdataprocessingsystem.service.FaultyRecordService;
import org.qum.iotdataprocessingsystem.service.PredictionClient;
import org.qum.iotdataprocessingsystem.service.impl.RedisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.core.MessageProducer;
import org.springframework.integration.mqtt.core.DefaultMqttPahoClientFactory;
import org.springframework.integration.mqtt.core.MqttPahoClientFactory;
import org.springframework.integration.mqtt.inbound.MqttPahoMessageDrivenChannelAdapter;
import org.springframework.integration.mqtt.support.DefaultPahoMessageConverter;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageHandler;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Configuration
public class MqttConfig {

    public static final String CHANNEL_NAME_IN = "mqttInputChannel";

    @Autowired
    RedisService redisService;

    @Autowired
    FaultyRecordService faultyRecordService;

    @Autowired
    KafkaTemplate<String, EquipmentData> kafkaTemplate;

    @Autowired
    PredictionClient predictionClient;

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
                //int faulty = checkFault(temperature, pressure, vibration, humidity);

                // 此处改为grpc
                int faulty = predictionClient.predict(temperature, pressure, vibration, humidity);

                // 构造 EquipmentData 对象
                LocalDateTime utc8DateTime = LocalDateTime.ofInstant(
                        timestamp,
                        ZoneId.of("Asia/Shanghai") // 使用上海时区（UTC+8）
                );

                EquipmentData equipmentData = new EquipmentData();
                equipmentData.setEquipmentId(deviceId);
                equipmentData.setTemperature(temperature);
                equipmentData.setPressure(pressure);
                equipmentData.setVibration(vibration);
                equipmentData.setHumidity(humidity);
                equipmentData.setFaulty(faulty);
                equipmentData.setTime(utc8DateTime); // 使用 UTC+8 时间

                // 发送数据到 Kafka
                kafkaTemplate.send("equipment-data-topic", deviceId, equipmentData);

                if(faulty == 1) {

                    System.out.println(deviceId + "异常");
                    faultyRecordService.add(deviceId, utc8DateTime);

                    // 1. 检查设备是否已存在于异常列表
                    boolean exists = redisService.checkAbnormal(deviceId);

                    // 2. 如果设备首次标记为异常，增加全局计数器
                    if (!exists) {
                        redisService.incr("abnormal_total");
                    }

                    // 存入redis
                    Map<String, Object> deviceData = new HashMap<>();
                    deviceData.put("temperature", temperature);
                    deviceData.put("pressure", pressure);
                    deviceData.put("vibration", vibration);
                    deviceData.put("humidity", humidity);
                    deviceData.put("type", equipment);
                    deviceData.put("location", location);  // 包含位置信息
                    deviceData.put("timestamp", timestamp.toEpochMilli());

                    // 2. 调用 Redis 服务保存异常设备
                    redisService.saveAbnormalDevice(deviceId, deviceData);
                }

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
//                System.out.println("[✅] Data written to InfluxDB: " + deviceId);

            } catch (Exception e) {
                System.err.println("❌ Error processing message: " + e.getMessage());
                e.printStackTrace();
            }
        };
    }

    private static int checkFault(double temperature, double pressure, double vibration, double humidity) {
        try {
            // 构造JSON请求体
            String jsonBody = String.format("{\"temperature\":%.1f,\"pressure\":%.1f,\"vibration\":%.1f,\"humidity\":%.1f}",
                    temperature, pressure, vibration, humidity);

            // 创建HTTP客户端
            HttpClient client = HttpClient.newHttpClient();

            // 构建POST请求
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("http://localhost:5000/predict"))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(jsonBody))
                    .timeout(Duration.ofSeconds(5))
                    .build();

            // 发送请求并处理响应
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            // 使用JSON解析代替字符串截取
            if (response.statusCode() == 200) {
                JsonNode rootNode = new ObjectMapper().readTree(response.body());
                return rootNode.path("faulty").asInt();
            } else {
                throw new RuntimeException("HTTP请求失败，状态码：" + response.statusCode());
            }
        } catch (Exception e) {
            throw new RuntimeException("故障检测失败: " + e.getMessage(), e);
        }
    }

}

