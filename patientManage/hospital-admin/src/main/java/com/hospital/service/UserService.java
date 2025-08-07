package com.hospital.service;

import com.hospital.dto.ApiResponse;
import com.hospital.dto.LoginRequest;
import com.hospital.dto.LoginResponse;
import com.hospital.model.entity.User;

import java.util.List;

public interface UserService {
    
    /**
     * 根据用户名查找用户
     */
    User findByUsername(String username);
    
    /**
     * 登录
     */
    ApiResponse<LoginResponse> login(LoginRequest loginRequest);
    
    /**
     * 根据用户名查找用户
     */
    User findByUsernameOrPhone(String usernameOrPhone);
    
    /**
     * 根据用户ID获取角色编码列表
     */
    List<String> getUserRoleCodes(Long userId);
    
    /**
     * 根据用户ID获取权限编码列表
     */
    List<String> getUserPermissionCodes(Long userId);
} 