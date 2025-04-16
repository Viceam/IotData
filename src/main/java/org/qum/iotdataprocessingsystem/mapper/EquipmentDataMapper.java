package org.qum.iotdataprocessingsystem.mapper;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.qum.iotdataprocessingsystem.pojo.EquipmentData;

@Mapper
public interface EquipmentDataMapper {
    @Insert("INSERT INTO equipment_data " +
            "(equipment_id, temperature, pressure, vibration, humidity, time, faulty) " +
            "VALUES " +
            "(#{equipmentId}, #{temperature}, #{pressure}, #{vibration}, #{humidity}, #{time}, #{faulty})")
    void add(EquipmentData equipmentData);
}
