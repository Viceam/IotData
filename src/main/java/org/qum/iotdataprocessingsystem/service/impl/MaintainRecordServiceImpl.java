package org.qum.iotdataprocessingsystem.service.impl;

import org.qum.iotdataprocessingsystem.mapper.MaintainRecordMapper;
import org.qum.iotdataprocessingsystem.pojo.MaintainRecord;
import org.qum.iotdataprocessingsystem.service.MaintainRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MaintainRecordServiceImpl implements MaintainRecordService {

    @Autowired
    MaintainRecordMapper maintainRecordMapper;

    @Override
    public void addRecord(MaintainRecord maintainRecord) {
        maintainRecordMapper.addMaintainRecord(maintainRecord);
    }
}
