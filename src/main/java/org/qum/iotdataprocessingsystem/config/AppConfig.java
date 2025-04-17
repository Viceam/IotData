package org.qum.iotdataprocessingsystem.config;

import org.qum.iotdataprocessingsystem.service.ServiceDiscovery;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {
    @Bean
    public ServiceDiscovery serviceDiscovery() {
        return new ServiceDiscovery("localhost", 8500);
    }
}
