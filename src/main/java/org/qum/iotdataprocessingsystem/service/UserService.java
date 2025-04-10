package org.qum.iotdataprocessingsystem.service;


import org.qum.iotdataprocessingsystem.dto.UserLoginDto;
import org.qum.iotdataprocessingsystem.pojo.User;

public interface UserService {
    Integer addUser(User user);

    boolean login(UserLoginDto userLoginDto);

    boolean update(User user);
}
