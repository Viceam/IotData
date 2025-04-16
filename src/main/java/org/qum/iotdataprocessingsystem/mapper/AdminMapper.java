package org.qum.iotdataprocessingsystem.mapper;

import org.apache.ibatis.annotations.*;
import org.qum.iotdataprocessingsystem.pojo.Admin;

@Mapper
public interface AdminMapper {
    @Select("SELECT password FROM admins WHERE username = #{username}")
    String getPasswordByUsername(@Param("username") String username);

    @Update("UPDATE admins SET password = #{password} WHERE username = #{username}")
    void updatePassword(Admin admin);

    @Insert("INSERT INTO admins(username, password)  VALUES (#{username}, #{password})")
    void insert(Admin admin);

    @Select("SELECT count(*) FROM admins WHERE username = #{username}")
    int getCountByUsername(String username);
}
