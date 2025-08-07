package com.hospital.repository;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hospital.model.entity.Permission;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 系统权限表 Mapper 接口
 * </p>
 *
 * @author AnYuan
 * @since 2025-08-05
 */
@Mapper
public interface PermissionMapper extends BaseMapper<Permission> {
    
    List<Permission> selectPermissionsByUserId(@Param("userId") Long userId);
    
    List<String> selectPermissionCodesByUserId(@Param("userId") Long userId);
    
    /**
     * 根据角色ID查询权限列表
     */
    List<Permission> selectPermissionsByRoleId(@Param("roleId") Long roleId);
    
    /**
     * 查询所有菜单权限（权限类型为1）
     */
    List<Permission> selectAllMenus();
}
