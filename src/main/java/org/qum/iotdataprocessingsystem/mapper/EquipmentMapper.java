package org.qum.iotdataprocessingsystem.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface EquipmentMapper {
    @Select("SELECT location FROM equipments WHERE equipment_id = #{equipmentId}")
    String getLocationByEquipmentId(String equipmentId);

    @Select("SELECT count(*) FROM equipments WHERE location = #{location}")
    int getNumByLocation(String location);

    @Select("SELECT equipments.equipment_id FROM equipments WHERE location = #{location}")
    List<String> getEquipmentIdByLocation(String location);
}
