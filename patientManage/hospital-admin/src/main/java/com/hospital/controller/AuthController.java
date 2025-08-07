package com.hospital.controller;

import com.hospital.dto.ApiResponse;
import com.hospital.dto.LoginRequest;
import com.hospital.dto.LoginResponse;
import com.hospital.model.entity.User;
import com.hospital.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.HashSet;
import java.util.Set;

@Slf4j
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private  UserService userService;
    
    // 用于存储已注销的token（在实际生产环境中应该使用Redis）
    private static final Set<String> blacklistedTokens = new HashSet<>();

    @PostMapping("/login")
    public ApiResponse<LoginResponse> login(@Valid @RequestBody LoginRequest loginRequest) {
        return userService.login(loginRequest);
    }

    @PostMapping("/logout")
    public ApiResponse<String> logout(HttpServletRequest request) {
        try {
            String token = getTokenFromRequest(request);
            if (token != null) {
                // 将token加入黑名单
                blacklistedTokens.add(token);
                log.info("用户已注销登录，token已加入黑名单");
            }
            
            // 清除Security上下文
            SecurityContextHolder.clearContext();
            
            return ApiResponse.success("注销成功", null);
            
        } catch (Exception e) {
            log.error("注销失败: {}", e.getMessage(), e);
            return ApiResponse.error("注销失败，请稍后重试");
        }
    }

    @GetMapping("/info")
    public ApiResponse<User> getUserInfo() {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication == null || !authentication.isAuthenticated()) {
                return ApiResponse.error(401, "用户未认证");
            }
            
            String username = authentication.getName();
            User user = userService.findByUsername(username);
            
            if (user == null) {
                return ApiResponse.error(404, "用户不存在");
            }
            
            // 清除敏感信息
            user.setPassword(null);
            
            return ApiResponse.success(user);
            
        } catch (Exception e) {
            log.error("获取用户信息失败: {}", e.getMessage(), e);
            return ApiResponse.error("获取用户信息失败");
        }
    }

    private String getTokenFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }
    
    /**
     * 检查token是否在黑名单中
     */
    public static boolean isTokenBlacklisted(String token) {
        return blacklistedTokens.contains(token);
    }
} 