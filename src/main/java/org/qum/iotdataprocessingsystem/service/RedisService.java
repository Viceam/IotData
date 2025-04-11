package org.qum.iotdataprocessingsystem.service;

import org.qum.iotdataprocessingsystem.dto.EquipmentsStatusDto;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;

@Service
public class RedisService {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    // 存储键值对
    public void setValue(String key, Object value) {
        redisTemplate.opsForValue().set(key, value);
    }

    // 获取值
    public Object getValue(String key) {
        return  redisTemplate.opsForValue().get(key);
    }

    // 删除键
    public void deleteKey(String key) {
        redisTemplate.delete(key);
    }

    /**
     * 原子递增（若键不存在则自动创建，初始值为0）
     * @param key 键名
     * @return 递增后的值
     */
    public Long incr(String key) {
        return redisTemplate.opsForValue().increment(key);
    }

    /**
     * 原子递减（若键不存在则自动创建，初始值为0）
     * @param key 键名
     * @return 递减后的值
     */
    public Long decr(String key) {
        return redisTemplate.opsForValue().decrement(key);
    }

    public boolean checkAbnormal(String equipmentId) {
        return redisTemplate.opsForZSet()
                .score("abnormal_devices", equipmentId) != null;
    }

    /**
     * 保存异常设备（自动去重+更新时间）
     */
    public void saveAbnormalDevice(String equipmentId, Map<String, Object> deviceData) {
        // 从 deviceData 中提取时间戳
        Long timestamp = (Long) deviceData.get("timestamp");
        String deviceKey = "device:" + equipmentId;

        // 1. 保存设备详情（Hash）
        redisTemplate.opsForHash().putAll(deviceKey, deviceData);

        // 2. 更新异常设备集合（ZSet）- 使用传入的时间戳
        redisTemplate.opsForZSet().add("abnormal_devices", equipmentId, timestamp);
    }

    public List<String> getAllAbnormalDevices() {
        Set<Object> rawDevices = redisTemplate.opsForZSet()
                .reverseRange("abnormal_devices", 0, -1);

        if (rawDevices == null) {
            return Collections.emptyList();
        }

        // 安全类型转换
        return rawDevices.stream()
                .map(obj -> {
                    if (obj instanceof String) {
                        return (String) obj;
                    }
                    throw new ClassCastException("设备ID类型不匹配：" + obj.getClass());
                })
                .toList();
    }

    public List<EquipmentsStatusDto> getAlerts() {
        List<EquipmentsStatusDto> result = new ArrayList<>();

        // 1. 获取所有异常设备（按时间倒序）
        Set<ZSetOperations.TypedTuple<Object>> devices = redisTemplate.opsForZSet()
                .reverseRangeWithScores("abnormal_devices", 0, -1);

        // 2. 遍历设备列表
        for (ZSetOperations.TypedTuple<Object> tuple : devices) {
            String equipmentId = (String) tuple.getValue();
            Long timestamp = tuple.getScore().longValue();

            try {
                // 3. 获取设备详情
                Map<Object, Object> data = redisTemplate.opsForHash()
                        .entries("device:" + equipmentId);

                // 4. 创建DTO并设置属性
                EquipmentsStatusDto dto = new EquipmentsStatusDto();
                dto.setEquipmentId(equipmentId);
                dto.setType((String) data.get("type"));
                dto.setLocation((String) data.get("location"));
                dto.setFaulty(1); // 因为是异常设备列表

                // 数值类型转换
                dto.setTemperature(Double.valueOf(data.get("temperature").toString()));
                dto.setPressure(Double.valueOf(data.get("pressure").toString()));
                dto.setVibration(Double.valueOf(data.get("vibration").toString()));
                dto.setHumidity(Double.valueOf(data.get("humidity").toString()));

                // 时间戳转换
                dto.setTimestamp(LocalDateTime.ofInstant(
                        Instant.ofEpochMilli(timestamp),
                        ZoneId.systemDefault()
                ));

                result.add(dto);
            } catch (Exception e) {
                // 继续处理其他设备

            }
        }

        return result;
    }
}
