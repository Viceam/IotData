package org.qum.iotdataprocessingsystem.service;

import org.qum.iotdataprocessingsystem.dto.FaultyRecordQueryDto;
import org.qum.iotdataprocessingsystem.dto.FaultyRecordQueryResultDto;
import org.qum.iotdataprocessingsystem.pojo.FaultyRecord;

import java.time.LocalDateTime;
import java.util.List;

public interface FaultyRecordService {
    void add(String equipmentId, LocalDateTime time);

    List<FaultyRecord> getFaultyRecords(FaultyRecordQueryDto faultyRecordQueryDto);

    List<FaultyRecord> getFaultyRecordsByTime(FaultyRecordQueryDto faultyRecordQueryDto);

    int getCount(FaultyRecordQueryDto faultyRecordQueryDto);

    int getCountTime(FaultyRecordQueryDto faultyRecordQueryDto);
}
