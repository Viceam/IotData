package org.qum.iotdataprocessingsystem.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.qum.iotdataprocessingsystem.dto.UserInfo;
import org.qum.iotdataprocessingsystem.util.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/general")
@CrossOrigin(origins = "*")
public class GeneralController {
    @GetMapping("/userinfo")
    public ResponseEntity<ApiResponse<UserInfo>> getUsername(HttpServletRequest request) {
        UserInfo userInfo = new UserInfo();
        String username = (String) request.getAttribute("username");
        String role = (String) request.getAttribute("role");

        userInfo.setUsername(username);
        userInfo.setRole(role);

        return ResponseEntity.ok(ApiResponse.success(userInfo));
    }
}
