package org.qum.iotdataprocessingsystem.mapper;

import org.apache.ibatis.annotations.*;
import org.qum.iotdataprocessingsystem.pojo.User;

@Mapper
public interface UserMapper {
    @Insert("INSERT INTO users(username, password, location) VALUES (#{username}, #{password}, #{location})")
    int add(User user);

    @Select("SELECT id FROM users WHERE username = #{username}")
    Integer getIdByUsername(String username);

    @Select("SELECT password FROM users WHERE username = #{username}")
    String getPasswordByUsername(String username);

    @Update("UPDATE users SET password = #{password}, location = #{location} WHERE username = #{username}")
    int updateUserByUsername(User user);
}
