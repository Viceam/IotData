package org.qum.iotdataprocessingsystem.service.impl;

import org.qum.iotdataprocessingsystem.dto.MaintainRecordQueryDto;
import org.qum.iotdataprocessingsystem.mapper.MaintainRecordMapper;
import org.qum.iotdataprocessingsystem.pojo.MaintainRecord;
import org.qum.iotdataprocessingsystem.service.MaintainRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MaintainRecordServiceImpl implements MaintainRecordService {

    @Autowired
    MaintainRecordMapper maintainRecordMapper;

    @Override
    public void addRecord(MaintainRecord maintainRecord) {
        maintainRecordMapper.addMaintainRecord(maintainRecord);
    }

    @Override
    public List<MaintainRecord> queryOnlyByTime(MaintainRecordQueryDto dto) {
        return maintainRecordMapper.queryOnlyByTime(dto);
    }

    @Override
    public List<MaintainRecord> queryByTimeAndEquipmentId(MaintainRecordQueryDto dto) {
        return maintainRecordMapper.queryByTimeAndEquipmentId(dto);
    }

    @Override
    public List<MaintainRecord> queryByTimeAndOperatorName(MaintainRecordQueryDto dto) {
        return maintainRecordMapper.queryByTimeAndOperatorName(dto);
    }

    @Override
    public List<MaintainRecord> queryByAll(MaintainRecordQueryDto dto) {
        return maintainRecordMapper.queryByAll(dto);
    }

    @Override
    public int countOnlyByTime(MaintainRecordQueryDto dto) {
        return maintainRecordMapper.queryCountOnlyByTime(dto);
    }

    @Override
    public int countByTimeAndEquipmentId(MaintainRecordQueryDto dto) {
        return maintainRecordMapper.queryCountByTimeAndEquipmentId(dto);
    }

    @Override
    public int countByTimeAndOperatorName(MaintainRecordQueryDto dto) {
        return maintainRecordMapper.queryCountByTimeAndOperatorName(dto);
    }

    @Override
    public int countByAll(MaintainRecordQueryDto dto) {
        return maintainRecordMapper.queryCountByAll(dto);
    }
}
