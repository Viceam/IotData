package org.qum.iotdataprocessingsystem.service.impl;

import org.qum.iotdataprocessingsystem.mapper.EquipmentDataMapper;
import org.qum.iotdataprocessingsystem.pojo.EquipmentData;
import org.qum.iotdataprocessingsystem.service.EquipmentDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EquipmentDataServiceImpl implements EquipmentDataService {
    @Autowired
    EquipmentDataMapper equipmentDataMapper;

    @Override
    public void add(EquipmentData equipmentData) {
        equipmentDataMapper.add(equipmentData);
    }
}
