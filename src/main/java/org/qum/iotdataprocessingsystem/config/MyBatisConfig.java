package org.qum.iotdataprocessingsystem.config;

import org.mybatis.spring.boot.autoconfigure.ConfigurationCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MyBatisConfig {
    @Bean
    public ConfigurationCustomizer mybatisConfigurationCustomizer() {
        return configuration -> {
            // 开启驼峰转下划线
            configuration.setMapUnderscoreToCamelCase(true);
        };
    }
}
