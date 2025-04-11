package org.qum.iotdataprocessingsystem.mapper;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;

import java.time.LocalDateTime;

@Mapper
public interface FaultyRecordMapper {
    @Insert("INSERT INTO faultyrecords(equipment_id, time) VALUES (#{equipmentId}, #{time})")
    void add(String equipmentId, LocalDateTime time);
}
