package org.qum.iotdataprocessingsystem.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.qum.iotdataprocessingsystem.dto.MaintainRecordQueryDto;
import org.qum.iotdataprocessingsystem.dto.MaintainRecordQueryResultDto;
import org.qum.iotdataprocessingsystem.pojo.MaintainRecord;
import org.qum.iotdataprocessingsystem.service.MaintainRecordService;
import org.qum.iotdataprocessingsystem.service.impl.RedisService;
import org.qum.iotdataprocessingsystem.util.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/record/maintain")
public class MaintainRecordController {
    @Autowired
    private MaintainRecordService maintainRecordService;

    @Autowired
    private RedisService redisService;

    @PostMapping
    public ResponseEntity<ApiResponse<String>> addRecord(@RequestBody MaintainRecord maintainRecord) {

        try {
            maintainRecordService.addRecord(maintainRecord);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.ok(ApiResponse.error(500, "error"));
        }

        return ResponseEntity.ok(ApiResponse.success("ok"));
    }

    @PostMapping("/query")
    public ResponseEntity<ApiResponse<MaintainRecordQueryResultDto>> getMaintainRecords(@RequestBody MaintainRecordQueryDto maintainRecordQueryDto, HttpServletRequest request) {
        String role = (String) request.getAttribute("role");
        // 处理时间默认值（最近一年）
        LocalDateTime now = LocalDateTime.now();
        if (maintainRecordQueryDto.getStartTime() == null) {
            maintainRecordQueryDto.setStartTime(now.minusYears(1));
        }
        if (maintainRecordQueryDto.getEndTime() == null) {
            maintainRecordQueryDto.setEndTime(now);
        }

        String equipmentId = maintainRecordQueryDto.getEquipmentId();
        String operatorName = maintainRecordQueryDto.getOperatorName();

        MaintainRecordQueryResultDto maintainRecordQueryResultDto = new MaintainRecordQueryResultDto();

        if(isN(equipmentId) && isN(operatorName)) {
            // 只根据时间
            maintainRecordQueryResultDto.setMaintainRecords(maintainRecordService.queryOnlyByTime(maintainRecordQueryDto));
            maintainRecordQueryResultDto.setTotal(maintainRecordService.countOnlyByTime(maintainRecordQueryDto));
        } else if(isN(operatorName)) {
            // 根据时间和机器id
            maintainRecordQueryResultDto.setMaintainRecords(maintainRecordService.queryByTimeAndEquipmentId(maintainRecordQueryDto));
            maintainRecordQueryResultDto.setTotal(maintainRecordService.countByTimeAndEquipmentId(maintainRecordQueryDto));
        } else if(isN(equipmentId)) {
            // 根据时间和操作员name
            maintainRecordQueryResultDto.setMaintainRecords(maintainRecordService.queryByTimeAndOperatorName(maintainRecordQueryDto));
            maintainRecordQueryResultDto.setTotal(maintainRecordService.countByTimeAndOperatorName(maintainRecordQueryDto));
        } else {
            // 根据所有条件查询
            maintainRecordQueryResultDto.setMaintainRecords(maintainRecordService.queryByAll(maintainRecordQueryDto));
            maintainRecordQueryResultDto.setTotal(maintainRecordService.countByAll(maintainRecordQueryDto));
        }

        return ResponseEntity.ok(ApiResponse.success(maintainRecordQueryResultDto));
    }

    private boolean isN(String s) {
        return (s == null || s.isEmpty() || s.isBlank());
    }
}
