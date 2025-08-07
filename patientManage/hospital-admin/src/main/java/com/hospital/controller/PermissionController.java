package com.hospital.controller;

import com.hospital.dto.ApiResponse;
import com.hospital.model.entity.Permission;
import com.hospital.service.PermissionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/permission")
@PreAuthorize("hasRole('ADMIN') or hasRole('SUPER_ADMIN')")
public class PermissionController {

    @Autowired
    private PermissionService permissionService;

    @GetMapping("/list")
    @PreAuthorize("hasAuthority('system:permission:query')")
    public ApiResponse<List<Permission>> getPermissionList() {
        try {
            List<Permission> permissions = permissionService.getAllPermissions();
            return ApiResponse.success(permissions);
        } catch (Exception e) {
            log.error("获取权限列表失败: {}", e.getMessage(), e);
            return ApiResponse.error("获取权限列表失败");
        }
    }

    @GetMapping("/tree")
    @PreAuthorize("hasAuthority('system:permission:query')")
    public ApiResponse<List<Permission>> getPermissionTree() {
        try {
            List<Permission> permissionTree = permissionService.getMenuTree();
            return ApiResponse.success(permissionTree);
        } catch (Exception e) {
            log.error("获取权限树失败: {}", e.getMessage(), e);
            return ApiResponse.error("获取权限树失败");
        }
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('system:permission:query')")
    public ApiResponse<Permission> getPermissionById(@PathVariable Long id) {
        try {
            Permission permission = permissionService.getPermissionById(id);
            if (permission == null) {
                return ApiResponse.error(404, "权限不存在");
            }
            return ApiResponse.success(permission);
        } catch (Exception e) {
            log.error("获取权限信息失败: {}", e.getMessage(), e);
            return ApiResponse.error("获取权限信息失败");
        }
    }

    @PostMapping
    @PreAuthorize("hasAuthority('system:permission:add')")
    public ApiResponse<String> createPermission(@RequestBody Permission permission) {
        try {
            if (permissionService.savePermission(permission)) {
                return ApiResponse.success("权限创建成功", null);
            } else {
                return ApiResponse.error("权限创建失败");
            }
        } catch (Exception e) {
            log.error("创建权限失败: {}", e.getMessage(), e);
            return ApiResponse.error("创建权限失败");
        }
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('system:permission:edit')")
    public ApiResponse<String> updatePermission(@PathVariable Long id, @RequestBody Permission permission) {
        try {
            permission.setId(id);
            if (permissionService.updatePermission(permission)) {
                return ApiResponse.success("权限更新成功", null);
            } else {
                return ApiResponse.error("权限更新失败");
            }
        } catch (Exception e) {
            log.error("更新权限失败: {}", e.getMessage(), e);
            return ApiResponse.error("更新权限失败");
        }
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('system:permission:delete')")
    public ApiResponse<String> deletePermission(@PathVariable Long id) {
        try {
            if (permissionService.deletePermission(id)) {
                return ApiResponse.success("权限删除成功", null);
            } else {
                return ApiResponse.error("权限删除失败");
            }
        } catch (Exception e) {
            log.error("删除权限失败: {}", e.getMessage(), e);
            return ApiResponse.error("删除权限失败");
        }
    }
} 