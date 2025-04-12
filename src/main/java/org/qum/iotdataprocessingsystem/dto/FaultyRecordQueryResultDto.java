package org.qum.iotdataprocessingsystem.dto;

import lombok.Data;
import org.qum.iotdataprocessingsystem.pojo.FaultyRecord;

import java.util.List;

@Data
public class FaultyRecordQueryResultDto {
    private List<FaultyRecord> faultyRecords;
    private int total;
}
