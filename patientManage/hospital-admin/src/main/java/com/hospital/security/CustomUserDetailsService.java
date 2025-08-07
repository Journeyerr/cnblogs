package com.hospital.security;

import com.hospital.model.entity.CurrentUser;
import com.hospital.model.entity.Permission;
import com.hospital.model.entity.Role;
import com.hospital.model.entity.User;
import com.hospital.repository.UserMapper;
import com.hospital.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class CustomUserDetailsService implements UserDetailsService {
    
    @Autowired
    private UserMapper userMapper;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        CurrentUser user = userMapper.loadUserByUsername(username);
        
        if (user == null) {
            throw new UsernameNotFoundException("用户名或密码错误: " + username);
        }
        
        // 获取用户角色和权限
        List<String> roleCodes = user.getRoles().stream().map(Role::getRoleCode).collect(Collectors.toList());
        List<String> permissionCodes = user.getPermission().stream().map(Permission::getPermissionCode).collect(Collectors.toList());
        
        // 构建权限列表
        List<SimpleGrantedAuthority> authorities = roleCodes.stream()
                .map(roleCode -> new SimpleGrantedAuthority("ROLE_" + roleCode))
                .collect(Collectors.toList());
        
        // 添加权限
        authorities.addAll(permissionCodes.stream()
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList()));
        
        return new org.springframework.security.core.userdetails.User(
                user.getUsername(),
                user.getPassword(),
                user.getStatus() == 1, // enabled
                true, // accountNonExpired
                true, // credentialsNonExpired
                true, // accountNonLocked
                authorities
        );
    }
} 