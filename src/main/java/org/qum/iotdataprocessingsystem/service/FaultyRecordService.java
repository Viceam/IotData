package org.qum.iotdataprocessingsystem.service;

import java.time.LocalDateTime;

public interface FaultyRecordService {
    void add(String equipmentId, LocalDateTime time);
}
