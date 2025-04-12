package org.qum.iotdataprocessingsystem.mapper;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.qum.iotdataprocessingsystem.dto.FaultyRecordQueryDto;
import org.qum.iotdataprocessingsystem.pojo.FaultyRecord;

import java.time.LocalDateTime;
import java.util.List;

@Mapper
public interface FaultyRecordMapper {
    @Insert("INSERT INTO faultyrecords(equipment_id, time) VALUES (#{equipmentId}, #{time})")
    void add(String equipmentId, LocalDateTime time);

    @Select("SELECT * FROM faultyrecords WHERE equipment_id = #{equipmentId} AND time BETWEEN #{startTime} AND #{endTime} LIMIT 7 OFFSET ((#{page} - 1)*7)")
    List<FaultyRecord> getFaultyRecords(FaultyRecordQueryDto faultyRecordQueryDto);

    @Select("SELECT * FROM faultyrecords WHERE time BETWEEN #{startTime} AND #{endTime} LIMIT 7 OFFSET ((#{page} - 1)*7)")
    List<FaultyRecord> getFaultyRecordsByTime(FaultyRecordQueryDto faultyRecordQueryDto);

    @Select("SELECT count(*) FROM faultyrecords WHERE equipment_id = #{equipmentId} AND time BETWEEN #{startTime} AND #{endTime}")
    int getCount(FaultyRecordQueryDto faultyRecordQueryDto);

    @Select("SELECT count(*) FROM faultyrecords WHERE time BETWEEN #{startTime} AND #{endTime}")
    int getCountTime(FaultyRecordQueryDto faultyRecordQueryDto);
}
