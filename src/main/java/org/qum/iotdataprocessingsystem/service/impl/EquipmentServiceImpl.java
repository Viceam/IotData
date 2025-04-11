package org.qum.iotdataprocessingsystem.service.impl;

import org.qum.iotdataprocessingsystem.mapper.EquipmentMapper;
import org.qum.iotdataprocessingsystem.service.EquipmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EquipmentServiceImpl implements EquipmentService {
    @Autowired
    EquipmentMapper equipmentMapper;

    @Override
    public String getLocationByEquipmentId(String equipmentId) {
        return equipmentMapper.getLocationByEquipmentId(equipmentId);
    }

    @Override
    public int getNumByLocation(String location) {
        return equipmentMapper.getNumByLocation(location);
    }
}
