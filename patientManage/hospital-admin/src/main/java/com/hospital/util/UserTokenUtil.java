package com.hospital.util;

import com.hospital.model.entity.CurrentUser;
import org.springframework.security.core.context.SecurityContextHolder;

public class UserTokenUtil {
    
    public static CurrentUser currentUser() {
        return (CurrentUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }
}