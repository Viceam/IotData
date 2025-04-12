package org.qum.iotdataprocessingsystem.service.impl;

import org.qum.iotdataprocessingsystem.dto.FaultyRecordQueryDto;
import org.qum.iotdataprocessingsystem.dto.FaultyRecordQueryResultDto;
import org.qum.iotdataprocessingsystem.mapper.FaultyRecordMapper;
import org.qum.iotdataprocessingsystem.pojo.FaultyRecord;
import org.qum.iotdataprocessingsystem.service.FaultyRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class FaultyRecordServiceImpl implements FaultyRecordService {
    @Autowired
    FaultyRecordMapper faultyRecordMapper;

    public void add(String equipmentId, LocalDateTime time) {
        faultyRecordMapper.add(equipmentId, time);
    }

    @Override
    public List<FaultyRecord> getFaultyRecords(FaultyRecordQueryDto faultyRecordQueryDto) {
        return faultyRecordMapper.getFaultyRecords(faultyRecordQueryDto);
    }

    @Override
    public List<FaultyRecord> getFaultyRecordsByTime(FaultyRecordQueryDto faultyRecordQueryDto) {
        return faultyRecordMapper.getFaultyRecordsByTime(faultyRecordQueryDto);
    }

    @Override
    public int getCount(FaultyRecordQueryDto faultyRecordQueryDto) {
        return faultyRecordMapper.getCount(faultyRecordQueryDto);
    }

    @Override
    public int getCountTime(FaultyRecordQueryDto faultyRecordQueryDto) {
        return faultyRecordMapper.getCountTime(faultyRecordQueryDto);
    }
}
