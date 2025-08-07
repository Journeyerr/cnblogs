package com.hospital.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.hospital.dto.ApiResponse;
import com.hospital.dto.LoginRequest;
import com.hospital.dto.LoginResponse;
import com.hospital.model.entity.User;
import com.hospital.repository.PermissionMapper;
import com.hospital.repository.RoleMapper;
import com.hospital.repository.UserMapper;
import com.hospital.service.UserService;
import com.hospital.util.JwtUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;
    @Autowired
    private RoleMapper roleMapper;
    @Autowired
    private PermissionMapper permissionMapper;
    @Autowired
    private JwtUtil jwtUtil;

    @Override
    public User findByUsername(String username) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username", username);
        return userMapper.selectOne(queryWrapper);
    }
    
    @Override
    public ApiResponse<LoginResponse> login(LoginRequest loginRequest) {
        try {
            String username = loginRequest.getUsername().trim();
            String password = loginRequest.getPassword().trim();
            
            // 获取用户信息
            User user = findByUsernameOrPhone(username);
            if (null == user || !new BCryptPasswordEncoder().matches(password, user.getPassword())) {
                return ApiResponse.error(401, "用户名或密码错误");
            }
            
            // 生成JWT token
            String token = jwtUtil.generateToken(username);
            
            // 创建登录响应
            LoginResponse loginResponse = new LoginResponse(user.getId(), token, username, user.getRealName(), user.getPhone());
            
            log.info("用户 {} 登录成功", username);
            return ApiResponse.success("登录成功", loginResponse);
            
        } catch (Exception e) {
            log.error("登录失败: {}", e.getMessage(), e);
            return ApiResponse.error("登录失败，请稍后重试");
        }
    }
    
    @Override
    public User findByUsernameOrPhone(String usernameOrPhone) {
        return userMapper.selectOne(new LambdaQueryWrapper<User>().eq(User::getUsername, usernameOrPhone)
            .or().eq(User::getPhone, usernameOrPhone));
    }

    @Override
    public List<String> getUserRoleCodes(Long userId) {
        return roleMapper.selectRoleCodesByUserId(userId);
    }

    @Override
    public List<String> getUserPermissionCodes(Long userId) {
        return permissionMapper.selectPermissionCodesByUserId(userId);
    }
} 