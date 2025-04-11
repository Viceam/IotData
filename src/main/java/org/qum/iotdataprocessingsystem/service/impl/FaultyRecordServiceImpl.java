package org.qum.iotdataprocessingsystem.service.impl;

import org.qum.iotdataprocessingsystem.mapper.FaultyRecordMapper;
import org.qum.iotdataprocessingsystem.service.FaultyRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class FaultyRecordServiceImpl implements FaultyRecordService {
    @Autowired
    FaultyRecordMapper faultyRecordMapper;

    public void add(String equipmentId, LocalDateTime time) {
        faultyRecordMapper.add(equipmentId, time);
    }
}
