package org.qum.iotdataprocessingsystem.service;

import org.qum.iotdataprocessingsystem.dto.AdminLoginDto;
import org.qum.iotdataprocessingsystem.pojo.Admin;

public interface AdminService {
    boolean login(AdminLoginDto adminLoginDto);

    void setPassword(Admin admin);

    String getPwByUsername(String username);
}
