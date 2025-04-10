package org.qum.iotdataprocessingsystem.pojo;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class FaultyRecord {
    private Integer id;
    private Integer equipmentId;
    private LocalDateTime time;
}
