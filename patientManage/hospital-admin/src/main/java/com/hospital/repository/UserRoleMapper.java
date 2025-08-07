package com.hospital.repository;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hospital.model.entity.UserRole;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 用户角色关联表 Mapper 接口
 * </p>
 *
 * @author AnYuan
 * @since 2025-08-05
 */
public interface UserRoleMapper extends BaseMapper<UserRole> {
    /**
     * 根据用户ID删除用户角色关联
     */
    int deleteByUserId(@Param("userId") Long userId);
}
