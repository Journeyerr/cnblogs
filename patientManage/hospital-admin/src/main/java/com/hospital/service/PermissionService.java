package com.hospital.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hospital.model.entity.Permission;

import java.util.List;

public interface PermissionService extends IService<Permission> {
    
    /**
     * 根据用户ID获取权限列表
     */
    List<Permission> getPermissionsByUserId(Long userId);
    
    /**
     * 根据角色ID获取权限列表
     */
    List<Permission> getPermissionsByRoleId(Long roleId);
    
    /**
     * 获取所有权限
     */
    List<Permission> getAllPermissions();
    
    /**
     * 获取菜单权限树
     */
    List<Permission> getMenuTree();
    
    /**
     * 根据ID获取权限
     */
    Permission getPermissionById(Long id);
    
    /**
     * 保存权限
     */
    boolean savePermission(Permission permission);
    
    /**
     * 更新权限
     */
    boolean updatePermission(Permission permission);
    
    /**
     * 删除权限
     */
    boolean deletePermission(Long id);
} 