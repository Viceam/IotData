package org.qum.iotdataprocessingsystem.mapper;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.qum.iotdataprocessingsystem.pojo.MaintainRecord;

@Mapper
public interface MaintainRecordMapper {
    @Insert("INSERT INTO maintainrecords (equipment_id, operator_name, time) VALUES (#{equipmentId}, #{operatorName}, #{time})")
    void addMaintainRecord(MaintainRecord maintainRecord);
}
