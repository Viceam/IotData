package org.qum.iotdataprocessingsystem.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.qum.iotdataprocessingsystem.dto.AdminLoginDto;
import org.qum.iotdataprocessingsystem.pojo.Admin;
import org.qum.iotdataprocessingsystem.service.AdminService;
import org.qum.iotdataprocessingsystem.util.ApiResponse;
import org.qum.iotdataprocessingsystem.util.ConstUtil;
import org.qum.iotdataprocessingsystem.util.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private AdminService adminService;

    /**
     * 测试接口：验证管理员权限
     */
    @GetMapping("/hello")
    public ResponseEntity<ApiResponse<String>> test(HttpServletRequest request) {
        // 获取用户角色
        String role = (String) request.getAttribute("role");

        // 验证角色是否为管理员
        if (!ConstUtil.ADMIN.equals(role)) {
            // 返回 401 Unauthorized 响应
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(ApiResponse.error(401, "Invalid credentials"));
        }

        // 如果是管理员，返回成功响应
        return ResponseEntity.ok(ApiResponse.success("hello"));
    }

    /**
     * 登录接口：管理员登录
     */
    @PostMapping(value = "/login", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiResponse<String>> login(@RequestBody AdminLoginDto adminLoginDto) {
        // 调用服务层进行登录验证
        if (adminService.login(adminLoginDto)) {
            // 登录成功，生成 JWT Token
            String token = JwtUtils.generateToken(adminLoginDto.getUsername(), ConstUtil.ADMIN);

            // 返回 200 OK 响应
            return ResponseEntity.ok(ApiResponse.success(token));
        } else {
            // 登录失败，返回 401 Unauthorized 响应
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(ApiResponse.error(401, "Invalid credentials"));
        }
    }

    /**
     * 添加管理员
     */
    @PostMapping
    public ResponseEntity<ApiResponse<String>> addAdmin(@RequestBody Admin admin) {
        return null;
    }
}
