package org.qum.iotdataprocessingsystem.service.impl;

import org.qum.iotdataprocessingsystem.mapper.EquipmentMapper;
import org.qum.iotdataprocessingsystem.mapper.UserMapper;
import org.qum.iotdataprocessingsystem.service.EquipmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EquipmentServiceImpl implements EquipmentService {
    @Autowired
    EquipmentMapper equipmentMapper;

    @Autowired
    UserMapper userMapper;

    @Override
    public String getLocationByEquipmentId(String equipmentId) {
        return equipmentMapper.getLocationByEquipmentId(equipmentId);
    }

    @Override
    public int getNumByLocation(String location) {
        return equipmentMapper.getNumByLocation(location);
    }

    @Override
    public List<String> getSensorByUsername(String username) {
        String location = userMapper.getLocationByUsername(username);
        return equipmentMapper.getEquipmentIdByLocation(location);
    }
}
