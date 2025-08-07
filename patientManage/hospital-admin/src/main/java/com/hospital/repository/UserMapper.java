package com.hospital.repository;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hospital.model.entity.CurrentUser;
import com.hospital.model.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.security.core.userdetails.UserDetails;

/**
 * <p>
 * 系统用户表 Mapper 接口
 * </p>
 *
 * @author AnYuan
 * @since 2025-08-05
 */
@Mapper
public interface UserMapper extends BaseMapper<User> {
    
    CurrentUser loadUserByUsername(@Param("userName") String userName);
}
