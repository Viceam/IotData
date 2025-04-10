package org.qum.iotdataprocessingsystem.service.impl;

import org.qum.iotdataprocessingsystem.dto.AdminLoginDto;
import org.qum.iotdataprocessingsystem.mapper.AdminMapper;
import org.qum.iotdataprocessingsystem.pojo.Admin;
import org.qum.iotdataprocessingsystem.service.AdminService;
import org.qum.iotdataprocessingsystem.util.PasswordUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AdminServiceImpl implements AdminService {

    @Autowired
    AdminMapper adminMapper;

    @Override
    public boolean login(AdminLoginDto adminLoginDto) {
        if(adminLoginDto == null || adminLoginDto.getUsername() == null || adminLoginDto.getPassword() == null) {
            return false;
        }

        String password = adminMapper.getPasswordByUsername(adminLoginDto.getUsername());
        if(password == null) {
            return false;
        }

        return password.equals(PasswordUtil.hashPassword(adminLoginDto.getPassword()));
    }
}
