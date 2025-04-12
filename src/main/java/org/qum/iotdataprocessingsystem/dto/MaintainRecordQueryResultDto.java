package org.qum.iotdataprocessingsystem.dto;

import lombok.Data;
import org.qum.iotdataprocessingsystem.pojo.MaintainRecord;

import java.util.List;

@Data
public class MaintainRecordQueryResultDto {
    private List<MaintainRecord> maintainRecords;
    private int total;
}
