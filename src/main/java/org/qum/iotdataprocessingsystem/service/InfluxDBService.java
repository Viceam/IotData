package org.qum.iotdataprocessingsystem.service;

import org.influxdb.InfluxDB;
import org.influxdb.dto.Query;
import org.influxdb.dto.QueryResult;
import org.qum.iotdataprocessingsystem.dto.EquipmentsStatusDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
//import java.util.Map;

@Service
public class InfluxDBService {

    @Autowired
    private InfluxDB influxDB;

    public List<EquipmentsStatusDto> queryDeviceStatus(
            String deviceId, LocalDateTime startTime, LocalDateTime endTime) {

        List<EquipmentsStatusDto> result = new ArrayList<>();

        // 时间转换逻辑
        ZoneId shanghaiZone = ZoneId.of("Asia/Shanghai");
        long startNs = startTime.atZone(shanghaiZone).toInstant().toEpochMilli() * 1_000_000L;
        long endNs = endTime.atZone(shanghaiZone).toInstant().toEpochMilli() * 1_000_000L;

        String queryStr = String.format(
                "SELECT * FROM sensor_data " +
                        "WHERE equipment_id = '%s' " +
                        "AND time >= %d AND time <= %d " +
                        "ORDER BY time DESC",
                deviceId, startNs, endNs);

        QueryResult queryResult = influxDB.query(new Query(queryStr, "equipments"));

        // 解析结果
        for (QueryResult.Result res : queryResult.getResults()) {
            if (res.getSeries() != null) {
                System.out.println(res.getSeries());
                for (QueryResult.Series series : res.getSeries()) {
                    List<String> columns = series.getColumns();

                    // 获取字段索引
                    int timeIndex = columns.indexOf("time");
                    int tempIndex = columns.indexOf("temperature");
                    int pressureIndex = columns.indexOf("pressure");
                    int vibrationIndex = columns.indexOf("vibration");
                    int humidityIndex = columns.indexOf("humidity");
                    int equipmentTypeIndex = columns.indexOf("equipment_type");
                    int locationIndex = columns.indexOf("location");
                    int faultyIndex = columns.indexOf("faulty");

                    for (List<Object> row : series.getValues()) {
                        // 处理时间戳
                        String timeStr = row.get(timeIndex).toString();
                        ZonedDateTime utcTime = ZonedDateTime.parse(timeStr, DateTimeFormatter.ISO_DATE_TIME);
                        LocalDateTime localTime = utcTime.withZoneSameInstant(shanghaiZone).toLocalDateTime();

                        // 构建 DTO
                        EquipmentsStatusDto dto = new EquipmentsStatusDto();
                        dto.setEquipmentId(deviceId);
                        dto.setTimestamp(localTime);
                        dto.setTemperature(parseDouble(row, tempIndex));
                        dto.setPressure(parseDouble(row, pressureIndex));
                        dto.setVibration(parseDouble(row, vibrationIndex));
                        dto.setHumidity(parseDouble(row, humidityIndex));
                        dto.setType(row.get(equipmentTypeIndex).toString());
                        dto.setLocation(row.get(locationIndex).toString());
                        dto.setFaulty(parseInt(row, faultyIndex));

                        result.add(dto);
                    }
                }
            }
        }

        return result;
    }

    // 辅助方法：安全转换为 Double
    private Double parseDouble(List<Object> row, int index) {
        if (index == -1) return 0.0;
        Object value = row.get(index);
        if (value instanceof String) {
            return Double.parseDouble((String) value);
        }
        return (Double) value;
    }

    private Integer parseInt(List<Object> row, int index) {
        if (index == -1) return 0;
        Object value = row.get(index);
        if (value instanceof String) return Integer.parseInt((String) value);
        if (value instanceof Number) return ((Number) value).intValue();
        return 0;
    }
}
