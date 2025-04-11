package org.qum.iotdataprocessingsystem.controller;

import org.qum.iotdataprocessingsystem.dto.EquipmentsStatusDto;
import org.qum.iotdataprocessingsystem.service.impl.InfluxDBService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/test")
public class TestController {

    @Autowired
    private InfluxDBService influxDBService;

    @GetMapping
    public List<EquipmentsStatusDto> querySensor() {
        String deviceId = "sensor_0";
        LocalDateTime endTime = LocalDateTime.now();
        LocalDateTime startTime = endTime.minusHours(6);

        return influxDBService.queryDeviceStatus(deviceId, startTime, endTime);
    }
}

