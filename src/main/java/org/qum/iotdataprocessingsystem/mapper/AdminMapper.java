package org.qum.iotdataprocessingsystem.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.qum.iotdataprocessingsystem.pojo.Admin;

@Mapper
public interface AdminMapper {
    @Select("SELECT password FROM admins WHERE username = #{username}")
    String getPasswordByUsername(@Param("username") String username);

    @Update("UPDATE admins SET password = #{password} WHERE username = #{username}")
    void updatePassword(Admin admin);
}
