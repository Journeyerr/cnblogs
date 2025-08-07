package com.hospital.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hospital.model.entity.Permission;
import com.hospital.repository.PermissionMapper;
import com.hospital.service.PermissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class PermissionServiceImpl extends ServiceImpl<PermissionMapper, Permission> implements PermissionService {

    @Autowired
    private PermissionMapper permissionMapper;

    @Override
    public List<Permission> getPermissionsByUserId(Long userId) {
        return permissionMapper.selectPermissionsByUserId(userId);
    }

    @Override
    public List<Permission> getPermissionsByRoleId(Long roleId) {
        return permissionMapper.selectPermissionsByRoleId(roleId);
    }

    @Override
    public List<Permission> getAllPermissions() {
        QueryWrapper<Permission> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("status", 1);
        queryWrapper.orderByAsc("sort_order");
        return permissionMapper.selectList(queryWrapper);
    }

    @Override
    public List<Permission> getMenuTree() {
        List<Permission> allPermissions = getAllPermissions();
        return buildTree(allPermissions, 0L);
    }

    @Override
    public Permission getPermissionById(Long id) {
        return permissionMapper.selectById(id);
    }

    @Override
    @Transactional
    public boolean savePermission(Permission permission) {
        permission.setCreateTime(LocalDateTime.now());
        permission.setUpdateTime(LocalDateTime.now());
        return permissionMapper.insert(permission) > 0;
    }

    @Override
    @Transactional
    public boolean updatePermission(Permission permission) {
        permission.setUpdateTime(LocalDateTime.now());
        return permissionMapper.updateById(permission) > 0;
    }

    @Override
    @Transactional
    public boolean deletePermission(Long id) {
        // 检查是否有子权限
        QueryWrapper<Permission> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("parent_id", id);
        queryWrapper.eq("status", 1);
        if (permissionMapper.selectCount(queryWrapper) > 0) {
            throw new RuntimeException("存在子权限，无法删除");
        }
        return permissionMapper.deleteById(id) > 0;
    }

    /**
     * 构建权限树
     */
    private List<Permission> buildTree(List<Permission> permissions, Long parentId) {
        Map<Long, List<Permission>> permissionMap = permissions.stream()
                .collect(Collectors.groupingBy(Permission::getParentId));
        
        return permissionMap.getOrDefault(parentId, new ArrayList<>()).stream()
                .peek(permission -> permission.setChildren(buildTree(permissions, permission.getId())))
                .collect(Collectors.toList());
    }
} 