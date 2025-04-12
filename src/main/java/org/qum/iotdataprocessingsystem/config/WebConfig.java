package org.qum.iotdataprocessingsystem.config;

import org.qum.iotdataprocessingsystem.controller.interceptor.JwtInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Autowired
    private JwtInterceptor jwtInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(jwtInterceptor)
                .addPathPatterns("/admin/**", "/admin") // 需要保护的路径
                .addPathPatterns("/user/**", "/user")
                .addPathPatterns("/api/**")
                .addPathPatterns("/record/**")
                .excludePathPatterns("/admin/login", "/user/login"); // 不需要拦截的路径

    }
}

