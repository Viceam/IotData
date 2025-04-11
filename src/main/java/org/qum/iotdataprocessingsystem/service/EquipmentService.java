package org.qum.iotdataprocessingsystem.service;

public interface EquipmentService {
    String getLocationByEquipmentId(String equipmentId);

    int getNumByLocation(String location);
}
