package org.qum.iotdataprocessingsystem.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface AdminMapper {
    @Select("SELECT password FROM admins WHERE username = #{username}")
    String getPasswordByUsername(@Param("username") String username);
}
