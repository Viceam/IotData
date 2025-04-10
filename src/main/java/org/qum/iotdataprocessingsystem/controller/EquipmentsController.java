package org.qum.iotdataprocessingsystem.controller;

import org.qum.iotdataprocessingsystem.dto.EquipmentsStatusDto;
import org.qum.iotdataprocessingsystem.service.InfluxDBService;
import org.qum.iotdataprocessingsystem.service.UserService;
import org.qum.iotdataprocessingsystem.util.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/equipment")
public class EquipmentsController {
    @Autowired
    private InfluxDBService influxDBService;

    @GetMapping("/{equipmentId}")
    public ResponseEntity<ApiResponse<List<EquipmentsStatusDto>>> getStatus(@PathVariable String equipmentId, // 接收路径参数
                                                                            @RequestParam("startTime") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startTime,
                                                                            @RequestParam("endTime") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endTime) {
        List<EquipmentsStatusDto> equipmentsStatusDtos = influxDBService.queryDeviceStatus(equipmentId, startTime, endTime);
        return ResponseEntity.ok(ApiResponse.success(equipmentsStatusDtos));
    }
}
