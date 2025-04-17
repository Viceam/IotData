package org.qum.iotdataprocessingsystem.service;

import com.google.common.net.HostAndPort;
import com.orbitz.consul.Consul;
import com.orbitz.consul.HealthClient;
import com.orbitz.consul.model.health.Service;
import com.orbitz.consul.model.health.ServiceHealth;

import java.util.List;


public class ServiceDiscovery {
    private final Consul consul;

    public ServiceDiscovery(String consulHost, int consulPort) {
        this.consul = Consul.builder().withHostAndPort(HostAndPort.fromParts(consulHost, consulPort)).build();
    }

    public ServiceHealth getService(String serviceName) {
        HealthClient healthClient = consul.healthClient();
        List<ServiceHealth> instances = healthClient.getHealthyServiceInstances(serviceName).getResponse();
        if (instances.isEmpty()) {
            throw new RuntimeException("No healthy instances found");
        }
        return instances.get(0); // 简单轮询策略
    }
}
