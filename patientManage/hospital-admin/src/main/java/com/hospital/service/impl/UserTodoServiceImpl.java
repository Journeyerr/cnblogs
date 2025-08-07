package com.hospital.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hospital.dto.UserTodoReqDTO;
import com.hospital.exception.AppException;
import com.hospital.model.entity.CurrentUser;
import com.hospital.model.entity.UserTodo;
import com.hospital.repository.UserTodoMapper;
import com.hospital.service.UserTodoService;
import com.hospital.util.UserTokenUtil;
import com.hospital.vo.UserTodoVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;

/**
 * <p>
 * 待办事项表 服务实现类
 * </p>
 *
 * @author AnYuan
 * @since 2025-08-05
 */
@Slf4j
@Service
public class UserTodoServiceImpl extends ServiceImpl<UserTodoMapper, UserTodo> implements UserTodoService {
    
    @Autowired
    private HttpServletRequest httpServletRequest;
    
    @Override
    public UserTodoVO createUserTodo(UserTodoReqDTO userTodoReqDTO) {
        
        if (userTodoReqDTO == null) {
            throw new AppException("参数错误");
        }
        
        UserTodo userTodo = new UserTodo();
        BeanUtils.copyProperties(userTodoReqDTO, userTodo);
        CurrentUser currentUser = UserTokenUtil.currentUser();
        
        userTodo.setCreateUserId(currentUser.getId());
//        userTodo.setTargetUserId(currentUser.getId());
        
        save(userTodo);
        
        UserTodoVO userTodoVO = new UserTodoVO();
        BeanUtils.copyProperties(userTodo, userTodoVO);
        
        return userTodoVO;
    }
}
