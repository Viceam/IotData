package org.qum.iotdataprocessingsystem.pojo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class EquipmentData {
    int id;
    String equipmentId;
    Double temperature;
    Double pressure;
    Double vibration;
    Double humidity;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    LocalDateTime time;
    Integer faulty;
}
