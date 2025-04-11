package org.qum.iotdataprocessingsystem.pojo;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class FaultyRecord {
    private Integer id;
    private String equipmentId;
    private LocalDateTime time;
}
