package com.hospital.controller;

import com.hospital.dto.ApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api")
public class TestController {

    @GetMapping("/public")
    public ApiResponse<String> publicEndpoint() {
        return ApiResponse.success("这是一个公开的接口，无需认证");
    }

    @GetMapping("/protected")
    public ApiResponse<Map<String, Object>> protectedEndpoint() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        
        Map<String, Object> result = new HashMap<>();
        result.put("message", "这是一个受保护的接口，需要JWT认证");
        result.put("username", authentication.getName());
        result.put("authorities", authentication.getAuthorities());
        
        log.info("用户 {} 访问了受保护的接口", authentication.getName());
        
        return ApiResponse.success(result);
    }

    @GetMapping("/admin")
    @PreAuthorize("hasRole('ADMIN') or hasRole('SUPER_ADMIN')")
    public ApiResponse<String> adminEndpoint() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        log.info("用户 {} 访问了管理员接口", authentication.getName());
        return ApiResponse.success("这是管理员专用接口");
    }

    @GetMapping("/super-admin")
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    public ApiResponse<String> superAdminEndpoint() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        log.info("用户 {} 访问了超级管理员接口", authentication.getName());
        return ApiResponse.success("这是超级管理员专用接口");
    }

    @GetMapping("/doctor")
    @PreAuthorize("hasRole('DOCTOR')")
    public ApiResponse<String> doctorEndpoint() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        log.info("用户 {} 访问了医生接口", authentication.getName());
        return ApiResponse.success("这是医生专用接口");
    }

    @GetMapping("/nurse")
    @PreAuthorize("hasRole('NURSE')")
    public ApiResponse<String> nurseEndpoint() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        log.info("用户 {} 访问了护士接口", authentication.getName());
        return ApiResponse.success("这是护士专用接口");
    }

    @GetMapping("/user-query")
    @PreAuthorize("hasAuthority('system:user:query')")
    public ApiResponse<String> userQueryEndpoint() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        log.info("用户 {} 访问了用户查询接口", authentication.getName());
        return ApiResponse.success("这是用户查询权限接口");
    }

    @GetMapping("/patient-query")
    @PreAuthorize("hasAuthority('hospital:patient:query')")
    public ApiResponse<String> patientQueryEndpoint() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        log.info("用户 {} 访问了患者查询接口", authentication.getName());
        return ApiResponse.success("这是患者查询权限接口");
    }

    @GetMapping("/patient-manage")
    @PreAuthorize("hasAuthority('hospital:patient:add') or hasAuthority('hospital:patient:edit') or hasAuthority('hospital:patient:delete')")
    public ApiResponse<String> patientManageEndpoint() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        log.info("用户 {} 访问了患者管理接口", authentication.getName());
        return ApiResponse.success("这是患者管理权限接口");
    }
} 