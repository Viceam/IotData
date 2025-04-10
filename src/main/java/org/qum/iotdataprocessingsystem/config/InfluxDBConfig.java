package org.qum.iotdataprocessingsystem.config;

import org.influxdb.InfluxDB;
import org.influxdb.InfluxDBFactory;
import org.influxdb.dto.Point;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class InfluxDBConfig {

    @Value("${spring.influxdb.url}")
    private String influxDBUrl;

    @Value("${spring.influxdb.database}")
    private String database;

    @Value("${spring.influxdb.retention-policy}")
    private String retentionPolicy;

    @Bean
    public InfluxDB influxDB() {
        InfluxDB influxDB = InfluxDBFactory.connect(influxDBUrl);
        influxDB.setDatabase(database);
        influxDB.setRetentionPolicy(retentionPolicy);
        return influxDB;
    }
}


