package org.qum.iotdataprocessingsystem.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.qum.iotdataprocessingsystem.dto.EquipmentsStatusDto;
import org.qum.iotdataprocessingsystem.service.EquipmentService;
import org.qum.iotdataprocessingsystem.service.impl.InfluxDBService;
import org.qum.iotdataprocessingsystem.service.impl.RedisService;
import org.qum.iotdataprocessingsystem.service.UserService;
import org.qum.iotdataprocessingsystem.util.ApiResponse;
import org.qum.iotdataprocessingsystem.util.ConstUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/api/equipment")
public class EquipmentsController {
    @Autowired
    private InfluxDBService influxDBService;

    @Autowired
    private RedisService redisService;

    @Autowired
    private UserService userService;

    @Autowired
    private EquipmentService equipmentService;

    @GetMapping("/{equipmentId}")
    public ResponseEntity<ApiResponse<List<EquipmentsStatusDto>>> getStatus(@PathVariable String equipmentId, // 接收路径参数
                                                                            @RequestParam("startTime") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startTime,
                                                                            @RequestParam("endTime") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endTime,
                                                                            HttpServletRequest request) {
        String role = (String) request.getAttribute("role");
        if(ConstUtil.ADMIN.equals(role)) {
            List<EquipmentsStatusDto> equipmentsStatusDtos = influxDBService.queryDeviceStatus(equipmentId, startTime, endTime);
            return ResponseEntity.ok(ApiResponse.success(equipmentsStatusDtos));
        } else if(ConstUtil.USER.equals(role)) {
            // 查找用户和机器所在地区
            String username = (String) request.getAttribute("username");
            String userLocation = userService.getLocationByUsername(username);
            String equipmentLocation = equipmentService.getLocationByEquipmentId(equipmentId);

            if(!Objects.equals(userLocation, equipmentLocation)) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(ApiResponse.error(401, "Invalid credentials"));
            }

            List<EquipmentsStatusDto> equipmentsStatusDtos = influxDBService.queryDeviceStatus(equipmentId, startTime, endTime);
            return ResponseEntity.ok(ApiResponse.success(equipmentsStatusDtos));
        }

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(ApiResponse.error(401, "Invalid credentials"));
    }

    @GetMapping("/alerts")
    public ResponseEntity<ApiResponse<List<EquipmentsStatusDto>>> getAlerts(HttpServletRequest request) {
        String role = (String) request.getAttribute("role");
        if(ConstUtil.ADMIN.equals(role)) {
            // 管理员可查看所有异常设备
            return ResponseEntity.ok(ApiResponse.success(redisService.getAlerts()));
        } else if(ConstUtil.USER.equals(role)) {
            List<EquipmentsStatusDto> equipmentsStatusDtos = redisService.getAlerts();
            // 查找用户所在地区
            String username = (String) request.getAttribute("username");
            String location = userService.getLocationByUsername(username);
            List<EquipmentsStatusDto> filteredList = equipmentsStatusDtos.stream()
                    .filter(dto -> dto.getLocation().equals(location))
                    .toList();

            return ResponseEntity.ok(ApiResponse.success(filteredList));
        }

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(ApiResponse.error(401, "Invalid credentials"));
    }

    @GetMapping("/status/ratio")
    public ResponseEntity<ApiResponse<Double>> getRatio(HttpServletRequest request) {
        String role = (String) request.getAttribute("role");
        if(ConstUtil.ADMIN.equals(role)) {
            return ResponseEntity.ok(ApiResponse.success(1. - (Integer)redisService.getValue("abnormal_total") / 100.));
        }
        else if(ConstUtil.USER.equals(role)) {
            // 所在地区
            String username = (String) request.getAttribute("username");
            String userLocation = userService.getLocationByUsername(username);

            // 查找本地区设备总量
            int total = equipmentService.getNumByLocation(userLocation);
            //查找本地区异常设备数量
            List<String> equipmentIds = redisService.getAllAbnormalDevices();
            int num = 0;
            for (String id:equipmentIds) {
                String equipmentLocation = equipmentService.getLocationByEquipmentId(id);
                if(Objects.equals(userLocation, equipmentLocation)) {
                    ++num;
                }
            }

            return ResponseEntity.ok(ApiResponse.success(1.0 - ((double) num / (double) total)));
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(ApiResponse.error(401, "Invalid credentials"));
    }
}
