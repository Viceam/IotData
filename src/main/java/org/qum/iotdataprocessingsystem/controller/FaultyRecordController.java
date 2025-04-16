package org.qum.iotdataprocessingsystem.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.qum.iotdataprocessingsystem.dto.FaultyRecordQueryDto;
import org.qum.iotdataprocessingsystem.dto.FaultyRecordQueryResultDto;
import org.qum.iotdataprocessingsystem.pojo.FaultyRecord;
import org.qum.iotdataprocessingsystem.service.FaultyRecordService;
import org.qum.iotdataprocessingsystem.util.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/record/faulty")
@CrossOrigin(origins = "*")
public class FaultyRecordController {
    @Autowired
    private FaultyRecordService faultyRecordService;

    @PostMapping("/query")
    public ResponseEntity<ApiResponse<FaultyRecordQueryResultDto>> getFaultyRecords(@RequestBody FaultyRecordQueryDto faultyRecordQueryDto, HttpServletRequest request) {
        String role = (String) request.getAttribute("role");
        // 1. 处理时间默认值（最近一年）
        LocalDateTime now = LocalDateTime.now();
        if (faultyRecordQueryDto.getStartTime() == null) {
            faultyRecordQueryDto.setStartTime(now.minusYears(1));
        }
        if (faultyRecordQueryDto.getEndTime() == null) {
            faultyRecordQueryDto.setEndTime(now);
        }

        FaultyRecordQueryResultDto faultyRecordQueryResultDto = new FaultyRecordQueryResultDto();

        if(faultyRecordQueryDto.getEquipmentId() == null || faultyRecordQueryDto.getEquipmentId().isEmpty()) {
            faultyRecordQueryResultDto.setFaultyRecords(faultyRecordService.getFaultyRecordsByTime(faultyRecordQueryDto));
            faultyRecordQueryResultDto.setTotal(faultyRecordService.getCountTime(faultyRecordQueryDto));
            return ResponseEntity.ok(ApiResponse.success(faultyRecordQueryResultDto));
        }

        faultyRecordQueryResultDto.setFaultyRecords(faultyRecordService.getFaultyRecords(faultyRecordQueryDto));
        faultyRecordQueryResultDto.setTotal(faultyRecordService.getCount(faultyRecordQueryDto));
        return ResponseEntity.ok(ApiResponse.success(faultyRecordQueryResultDto));
    }
}
