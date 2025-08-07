package com.hospital.repository;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hospital.model.entity.RolePermission;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 角色权限关联表 Mapper 接口
 * </p>
 *
 * @author AnYuan
 * @since 2025-08-05
 */
@Mapper
public interface RolePermissionMapper extends BaseMapper<RolePermission> {
    /**
     * 根据角色ID删除角色权限关联
     */
    int deleteByRoleId(@Param("roleId") Long roleId);
}
