package com.hospital.service;

import com.hospital.model.entity.Role;

import java.util.List;

public interface RoleService {
    
    /**
     * 根据用户ID获取角色列表
     */
    List<Role> getRolesByUserId(Long userId);
    
    /**
     * 获取所有角色
     */
    List<Role> getAllRoles();
    
    /**
     * 根据ID获取角色
     */
    Role getRoleById(Long id);
    
    /**
     * 保存角色
     */
    boolean saveRole(Role role);
    
    /**
     * 更新角色
     */
    boolean updateRole(Role role);
    
    /**
     * 删除角色
     */
    boolean deleteRole(Long id);
    
    /**
     * 分配角色权限
     */
    boolean assignPermissions(Long roleId, List<Long> permissionIds);
} 