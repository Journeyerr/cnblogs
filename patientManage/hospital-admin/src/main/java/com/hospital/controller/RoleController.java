package com.hospital.controller;

import com.hospital.dto.ApiResponse;
import com.hospital.model.entity.Role;
import com.hospital.service.RoleService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/role")
@PreAuthorize("hasRole('ADMIN') or hasRole('SUPER_ADMIN')")
public class RoleController {

    @Autowired
    private RoleService roleService;

    @GetMapping("/list")
    @PreAuthorize("hasAuthority('system:role:query')")
    public ApiResponse<List<Role>> getRoleList() {
        try {
            List<Role> roles = roleService.getAllRoles();
            return ApiResponse.success(roles);
        } catch (Exception e) {
            log.error("获取角色列表失败: {}", e.getMessage(), e);
            return ApiResponse.error("获取角色列表失败");
        }
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('system:role:query')")
    public ApiResponse<Role> getRoleById(@PathVariable Long id) {
        try {
            Role role = roleService.getRoleById(id);
            if (role == null) {
                return ApiResponse.error(404, "角色不存在");
            }
            return ApiResponse.success(role);
        } catch (Exception e) {
            log.error("获取角色信息失败: {}", e.getMessage(), e);
            return ApiResponse.error("获取角色信息失败");
        }
    }

    @PostMapping
    @PreAuthorize("hasAuthority('system:role:add')")
    public ApiResponse<String> createRole(@RequestBody Role role) {
        try {
            if (roleService.saveRole(role)) {
                return ApiResponse.success("角色创建成功", null);
            } else {
                return ApiResponse.error("角色创建失败");
            }
        } catch (Exception e) {
            log.error("创建角色失败: {}", e.getMessage(), e);
            return ApiResponse.error("创建角色失败");
        }
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('system:role:edit')")
    public ApiResponse<String> updateRole(@PathVariable Long id, @RequestBody Role role) {
        try {
            role.setId(id);
            if (roleService.updateRole(role)) {
                return ApiResponse.success("角色更新成功", null);
            } else {
                return ApiResponse.error("角色更新失败");
            }
        } catch (Exception e) {
            log.error("更新角色失败: {}", e.getMessage(), e);
            return ApiResponse.error("更新角色失败");
        }
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('system:role:delete')")
    public ApiResponse<String> deleteRole(@PathVariable Long id) {
        try {
            if (roleService.deleteRole(id)) {
                return ApiResponse.success("角色删除成功", null);
            } else {
                return ApiResponse.error("角色删除失败");
            }
        } catch (Exception e) {
            log.error("删除角色失败: {}", e.getMessage(), e);
            return ApiResponse.error("删除角色失败");
        }
    }

    @PostMapping("/{id}/permissions")
    @PreAuthorize("hasAuthority('system:role:assign')")
    public ApiResponse<String> assignPermissions(@PathVariable Long id, @RequestBody List<Long> permissionIds) {
        try {
            if (roleService.assignPermissions(id, permissionIds)) {
                return ApiResponse.success("权限分配成功", null);
            } else {
                return ApiResponse.error("权限分配失败");
            }
        } catch (Exception e) {
            log.error("分配权限失败: {}", e.getMessage(), e);
            return ApiResponse.error("分配权限失败");
        }
    }
} 