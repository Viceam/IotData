package org.qum.iotdataprocessingsystem.service;

import java.util.List;

public interface EquipmentService {
    String getLocationByEquipmentId(String equipmentId);

    int getNumByLocation(String location);

    List<String> getSensorByUsername(String username);
}
