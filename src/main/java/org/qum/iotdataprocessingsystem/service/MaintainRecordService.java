package org.qum.iotdataprocessingsystem.service;

import org.qum.iotdataprocessingsystem.dto.MaintainRecordQueryDto;
import org.qum.iotdataprocessingsystem.pojo.MaintainRecord;

import java.util.List;

public interface MaintainRecordService {
    void addRecord(MaintainRecord maintainRecord);

    // ==== 分页查询方法 ====
    List<MaintainRecord> queryOnlyByTime(MaintainRecordQueryDto dto);
    List<MaintainRecord> queryByTimeAndEquipmentId(MaintainRecordQueryDto dto);
    List<MaintainRecord> queryByTimeAndOperatorName(MaintainRecordQueryDto dto);
    List<MaintainRecord> queryByAll(MaintainRecordQueryDto dto);

    // ==== 统计查询方法 ====
    int countOnlyByTime(MaintainRecordQueryDto dto);
    int countByTimeAndEquipmentId(MaintainRecordQueryDto dto);
    int countByTimeAndOperatorName(MaintainRecordQueryDto dto);
    int countByAll(MaintainRecordQueryDto dto);
}
