package org.qum.iotdataprocessingsystem.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.qum.iotdataprocessingsystem.util.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/general")
public class GeneralController {
    @GetMapping("/username")
    public ResponseEntity<ApiResponse<String>> getUsername(HttpServletRequest request) {
        String username = (String) request.getAttribute("username");

        return ResponseEntity.ok(ApiResponse.success(username));
    }
}
