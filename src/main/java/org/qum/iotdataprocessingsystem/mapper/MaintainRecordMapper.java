package org.qum.iotdataprocessingsystem.mapper;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.qum.iotdataprocessingsystem.dto.MaintainRecordQueryDto;
import org.qum.iotdataprocessingsystem.pojo.MaintainRecord;

import java.util.List;

@Mapper
public interface MaintainRecordMapper {
    @Insert("INSERT INTO maintainrecords (equipment_id, operator_name, time) " +
            "VALUES (#{equipmentId}, #{operatorName}, #{time})")
    void addMaintainRecord(MaintainRecord maintainRecord);

    @Select("SELECT * FROM maintainrecords WHERE time BETWEEN #{startTime} AND #{endTime} " +
            "LIMIT 7 OFFSET ((#{page} - 1) * 7)")
    List<MaintainRecord> queryOnlyByTime(MaintainRecordQueryDto dto);

    @Select("SELECT * FROM maintainrecords WHERE time BETWEEN #{startTime} AND #{endTime} " +
            "AND equipment_id = #{equipmentId} " +
            "LIMIT 7 OFFSET ((#{page} - 1) * 7)")
    List<MaintainRecord> queryByTimeAndEquipmentId(MaintainRecordQueryDto dto);

    @Select("SELECT * FROM maintainrecords WHERE time BETWEEN #{startTime} AND #{endTime} " +
            "AND operator_name = #{operatorName} " +
            "LIMIT 7 OFFSET ((#{page} - 1) * 7)")
    List<MaintainRecord> queryByTimeAndOperatorName(MaintainRecordQueryDto dto);

    @Select("SELECT * FROM maintainrecords WHERE time BETWEEN #{startTime} AND #{endTime} " +
            "AND equipment_id = #{equipmentId} AND operator_name = #{operatorName} " +
            "LIMIT 7 OFFSET ((#{page} - 1) * 7)")
    List<MaintainRecord> queryByAll(MaintainRecordQueryDto dto);

    @Select("SELECT COUNT(*) FROM maintainrecords WHERE time BETWEEN #{startTime} AND #{endTime}")
    int queryCountOnlyByTime(MaintainRecordQueryDto dto);

    @Select("SELECT COUNT(*) FROM maintainrecords WHERE time BETWEEN #{startTime} AND #{endTime} " +
            "AND equipment_id = #{equipmentId}")
    int queryCountByTimeAndEquipmentId(MaintainRecordQueryDto dto);

    @Select("SELECT COUNT(*) FROM maintainrecords WHERE time BETWEEN #{startTime} AND #{endTime} " +
            "AND operator_name = #{operatorName}")
    int queryCountByTimeAndOperatorName(MaintainRecordQueryDto dto);

    @Select("SELECT COUNT(*) FROM maintainrecords WHERE time BETWEEN #{startTime} AND #{endTime} " +
            "AND equipment_id = #{equipmentId} AND operator_name = #{operatorName}")
    int queryCountByAll(MaintainRecordQueryDto dto);
}
