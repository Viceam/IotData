package org.qum.iotdataprocessingsystem.controller;

import org.qum.iotdataprocessingsystem.pojo.MaintainRecord;
import org.qum.iotdataprocessingsystem.service.MaintainRecordService;
import org.qum.iotdataprocessingsystem.util.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/record/maintain")
public class MaintainRecordController {
    @Autowired
    private MaintainRecordService maintainRecordService;

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
}
