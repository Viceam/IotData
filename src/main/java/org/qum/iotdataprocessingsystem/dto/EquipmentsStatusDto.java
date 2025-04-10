package org.qum.iotdataprocessingsystem.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class EquipmentsStatusDto {
    private String equipmentId;
    private String type;
    private String location;
    private Integer faulty;
    private Double temperature;
    private Double pressure;
    private Double vibration;
    private Double humidity;
    private LocalDateTime timestamp;
}

