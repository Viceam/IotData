package org.qum.iotdataprocessingsystem.config;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.qum.iotdataprocessingsystem.pojo.EquipmentData;
import org.qum.iotdataprocessingsystem.service.EquipmentDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class EquipmentDataConsumer {
    @Autowired
    private EquipmentDataService equipmentDataService;

    @KafkaListener(topics = "equipment-data-topic", groupId = "equipment-data-group")
    public void consumeEquipmentData(ConsumerRecord<String, EquipmentData> record) {
        EquipmentData data = record.value();
        equipmentDataService.add(data);  // 调用持久化方法
    }
}
