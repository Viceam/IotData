package org.qum.iotdataprocessingsystem.pojo;


import lombok.Data;

import java.time.LocalDateTime;

@Data
public class MaintainRecord {
    private Integer id;
    private Integer operatorId;
    private LocalDateTime time;
}
