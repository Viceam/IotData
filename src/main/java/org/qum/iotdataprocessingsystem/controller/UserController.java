package org.qum.iotdataprocessingsystem.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.apache.kafka.shaded.com.google.protobuf.Api;
import org.qum.iotdataprocessingsystem.dto.AdminLoginDto;
import org.qum.iotdataprocessingsystem.dto.UserLoginDto;
import org.qum.iotdataprocessingsystem.pojo.User;
import org.qum.iotdataprocessingsystem.service.UserService;
import org.qum.iotdataprocessingsystem.util.ApiResponse;
import org.qum.iotdataprocessingsystem.util.ConstUtil;
import org.qum.iotdataprocessingsystem.util.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
@CrossOrigin(origins = "*")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/add")
    public ResponseEntity<ApiResponse<String>> addUser(@RequestBody User user, HttpServletRequest request) {
        String role = (String) request.getAttribute("role");
        if(!ConstUtil.ADMIN.equals(role)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(ApiResponse.error(401, "Invalid credentials"));
        }

        Integer result = userService.addUser(user);
        return switch (result) {
            case 1 ->
                // 插入成功
                    ResponseEntity.ok(ApiResponse.success("ok"));
            case 0 ->
                // 用户名已存在
                    ResponseEntity.ok(ApiResponse.error(400, "error"));
            case -1 ->
                // 其他异常
                    ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                            .body(ApiResponse.error(500, "An error occurred while adding the user"));
            default ->
                // 未知错误
                    ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                            .body(ApiResponse.error(500, "Unknown error"));
        };
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<String>> login(@RequestBody UserLoginDto userLoginDto) {
        if(userService.login(userLoginDto)) {
            // 登录成功，生成 JWT Token
            String token = JwtUtils.generateToken(userLoginDto.getUsername(), ConstUtil.USER);

            // 返回 200 OK 响应
            return ResponseEntity.ok(ApiResponse.success(token));
        } else {
            // 登录失败，返回 401 Unauthorized 响应
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(ApiResponse.error(401, "Invalid credentials"));
        }
    }

    @PutMapping("/{username}")
    public ResponseEntity<ApiResponse<String>> update(
            @RequestBody User user,
            @PathVariable String username,
            HttpServletRequest request) {

        // 1. 权限检查
        String role = (String) request.getAttribute("role");
        if (role == null || !ConstUtil.ADMIN.equals(role)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(ApiResponse.error(401, "Unauthorized: Invalid credentials"));
        }

        // 2. 忽略请求体中的 username，始终以路径变量为准
        user.setUsername(username);

        // 3. 调用服务层更新用户信息
        try {
            boolean isUpdated = userService.update(user);
            if (isUpdated) {
                return ResponseEntity.ok(ApiResponse.success("Update successful"));
            } else {
                // 更新失败（可能是 username 不存在）
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(ApiResponse.error(404, "User not found or no changes made"));
            }
        } catch (Exception e) {
            // 捕获异常并记录日志
            System.err.println("Error updating user: " + e.getMessage());
            e.printStackTrace();

            // 返回 500 错误
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error(500, "Internal server error: Failed to update user"));
        }
    }
}
