package org.qum.iotdataprocessingsystem.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.qum.iotdataprocessingsystem.dto.UpdatePasswordDto;
import org.qum.iotdataprocessingsystem.dto.UserInfo;
import org.qum.iotdataprocessingsystem.pojo.Admin;
import org.qum.iotdataprocessingsystem.pojo.User;
import org.qum.iotdataprocessingsystem.service.AdminService;
import org.qum.iotdataprocessingsystem.service.UserService;
import org.qum.iotdataprocessingsystem.util.ApiResponse;
import org.qum.iotdataprocessingsystem.util.ConstUtil;
import org.qum.iotdataprocessingsystem.util.PasswordUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;

@RestController
@RequestMapping("/general")
@CrossOrigin(origins = "*")
public class GeneralController {
    @Autowired
    private AdminService adminService;

    @Autowired
    private UserService userService;

    @GetMapping("/userinfo")
    public ResponseEntity<ApiResponse<UserInfo>> getUsername(HttpServletRequest request) {
        UserInfo userInfo = new UserInfo();
        String username = (String) request.getAttribute("username");
        String role = (String) request.getAttribute("role");

        userInfo.setUsername(username);
        userInfo.setRole(role);

        return ResponseEntity.ok(ApiResponse.success(userInfo));
    }

    @PostMapping("/pw")
    public ResponseEntity<ApiResponse<String>> updatePw(@RequestBody UpdatePasswordDto updatePasswordDto, HttpServletRequest request) {
        // 获取用户角色
        String role = (String) request.getAttribute("role");
        // 验证角色是否为管理员
        if (ConstUtil.ADMIN.equals(role)) {
            String password = adminService.getPwByUsername(updatePasswordDto.getUsername());
            updatePasswordDto.setPw(PasswordUtil.hashPassword(updatePasswordDto.getPw()));

            if(Objects.equals(password, updatePasswordDto.getPw())) {
                Admin admin = new Admin();
                admin.setUsername(updatePasswordDto.getUsername());
                admin.setPassword(PasswordUtil.hashPassword(updatePasswordDto.getNpw()));
                adminService.setPassword(admin);
                return ResponseEntity.ok(ApiResponse.success("ok"));
            }

            return ResponseEntity.ok(ApiResponse.error(401, "error"));
        }
        else if(ConstUtil.USER.equals(role)) {
            //普通用户
            String origin_password = userService.getPwByUsername(updatePasswordDto.getUsername());
            String password = PasswordUtil.hashPassword(updatePasswordDto.getPw());

            if(Objects.equals(origin_password, password)) {
                User user = new User();
                user.setUsername(updatePasswordDto.getUsername());
                user.setPassword(PasswordUtil.hashPassword(updatePasswordDto.getNpw()));
                userService.updatePassword(user);
                return ResponseEntity.ok(ApiResponse.success("ok"));
            }

            return ResponseEntity.ok(ApiResponse.error(401, "error"));
        }


        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(ApiResponse.error(401, "Invalid credentials"));
    }
}
