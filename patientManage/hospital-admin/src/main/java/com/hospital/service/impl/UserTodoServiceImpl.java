package com.hospital.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
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
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

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
    public UserTodoVO create(UserTodoReqDTO userTodoReqDTO) {
        
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
    
    @Override
    public List<UserTodoVO> list(Integer status) {
        Long id = UserTokenUtil.currentUser().getId();
        
        LambdaQueryWrapper<UserTodo> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(UserTodo::getCreateUserId, id);
        if (null != status) {
            queryWrapper.eq(UserTodo::getStatus, status);
        }
        List<UserTodo> list = list(queryWrapper.orderByAsc(UserTodo::getStatus).orderByDesc(UserTodo::getUrgency));
        
        return list.stream().map(userTodo -> {
            UserTodoVO userTodoVO = new UserTodoVO();
            BeanUtils.copyProperties(userTodo, userTodoVO);
            return userTodoVO;
        }).collect(Collectors.toList());
    }
    
    @Override
    public UserTodoVO update(UserTodoReqDTO userTodoReqDTO) {
        if (null == userTodoReqDTO.getId()) {
            throw new AppException("待办事项ID不能为空");
        }
        
        UserTodo userTodo = getById(userTodoReqDTO.getId());
        
        if (null == userTodo || !Objects.equals(userTodo.getCreateUserId(), UserTokenUtil.currentUser().getId())) {
            throw new AppException("待办事项不存在");
        }
        if (null != userTodoReqDTO.getStatus()) {
            userTodo.setStatus(userTodoReqDTO.getStatus());
        }
        if (null != userTodoReqDTO.getTitle()) {
            userTodo.setTitle(userTodoReqDTO.getTitle());
        }
        if (null != userTodoReqDTO.getExecuteTime()) {
            userTodo.setExecuteTime(userTodoReqDTO.getExecuteTime());
        }
        
        updateById(userTodo);
        
        UserTodoVO userTodoVO = new UserTodoVO();
        BeanUtils.copyProperties(userTodo, userTodoVO);
        return userTodoVO;
    }
    
    @Override
    public Boolean delete(Long id) {
        UserTodo userTodo = getById(id);
        
        if (null == userTodo || !Objects.equals(userTodo.getCreateUserId(), UserTokenUtil.currentUser().getId())) {
            throw new AppException("待办事项不存在");
        }
       
        return removeById(id);
    }
}
