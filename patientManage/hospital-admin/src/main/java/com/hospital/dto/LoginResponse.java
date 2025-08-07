package com.hospital.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginResponse {
    
    private Long id;
    private String token;
    private String username;
    private String realName;
    private String message;
    private String phone;
    
    public LoginResponse(Long id, String token, String username, String realName, String phone) {
        this.id = id;
        this.token = token;
        this.username = username;
        this.realName = realName;
        this.message = "登录成功";
        this.phone = phone;
    }
}