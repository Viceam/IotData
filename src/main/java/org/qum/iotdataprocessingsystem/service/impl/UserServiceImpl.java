package org.qum.iotdataprocessingsystem.service.impl;

import org.qum.iotdataprocessingsystem.dto.UserLoginDto;
import org.qum.iotdataprocessingsystem.mapper.UserMapper;
import org.qum.iotdataprocessingsystem.pojo.User;
import org.qum.iotdataprocessingsystem.service.UserService;
import org.qum.iotdataprocessingsystem.util.PasswordUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    UserMapper userMapper;

    public Integer addUser(User user) {
        try {
            String encoded = PasswordUtil.hashPassword(user.getPassword());
            user.setPassword(encoded);
            int rowsAffected = userMapper.add(user);
            if (rowsAffected > 0) {
                return 1; // 插入成功
            }
        } catch (DataIntegrityViolationException e) {
            // 捕获唯一约束冲突异常（用户名已存在）
            return 0; // 用户名已存在
        } catch (Exception e) {
            // 捕获其他异常
            e.printStackTrace(); // 打印异常日志
            return -1; // 其他异常
        }
        return -1; // 默认返回其他异常
    }

    @Override
    public boolean login(UserLoginDto userLoginDto) {
        if(userLoginDto == null || userLoginDto.getUsername() == null || userLoginDto.getPassword() == null) {
            return false;
        }
        String password = userMapper.getPasswordByUsername(userLoginDto.getUsername());
        if(password == null) {
            return false;
        }
        String passwordGiven = PasswordUtil.hashPassword(userLoginDto.getPassword());

        return passwordGiven.equals(password);
    }

    @Override
    public boolean update(User user) {
        try {
            String encoded = PasswordUtil.hashPassword(user.getPassword());
            user.setPassword(encoded);
            // 调用 Mapper 更新用户信息
            int rowsAffected = userMapper.updateUserByUsername(user);

            // 如果受影响的行数为 0，说明更新失败（可能是 username 不存在）
            if (rowsAffected == 0) {
                return false; // 返回 false 表示更新失败
            }

            // 更新成功
            return true;
        } catch (Exception e) {
            // 捕获异常并打印日志（可以根据需要记录更详细的日志）
            System.err.println("更新用户信息时发生异常: " + e.getMessage());
            e.printStackTrace();

            // 返回 false 表示更新失败
            return false;
        }
    }

    @Override
    public String getLocationByUsername(String username) {
        return userMapper.getLocationByUsername(username);
    }
}
