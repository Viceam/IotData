package org.qum.iotdataprocessingsystem.dto;

import lombok.Data;

@Data
public class UpdatePasswordDto {
    String username;
    String pw;
    String npw;
}
