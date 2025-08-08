package com.hospital.service;

import com.hospital.dto.UserTodoReqDTO;
import com.hospital.model.entity.UserTodo;
import com.baomidou.mybatisplus.extension.service.IService;
import com.hospital.vo.UserTodoVO;

import java.util.List;

/**
 * <p>
 * 待办事项表 服务类
 * </p>
 *
 * @author AnYuan
 * @since 2025-08-05
 */
public interface UserTodoService extends IService<UserTodo> {

    UserTodoVO create(UserTodoReqDTO userTodoReqDTO);
    
    List<UserTodoVO> list(Integer status);
    
    UserTodoVO update(UserTodoReqDTO userTodoReqDTO);
    
    Boolean delete(Long id);
}
