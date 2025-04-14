package org.qum.iotdataprocessingsystem.pojo;


import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class MaintainRecord {
    private Integer id;
    private String operatorName;
    private String equipmentId;
    private String description;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime time;
}
