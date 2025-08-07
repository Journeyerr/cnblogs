package com.hospital.service;

import com.hospital.dto.UserTodoReqDTO;
import com.hospital.model.entity.UserTodo;
import com.baomidou.mybatisplus.extension.service.IService;
import com.hospital.vo.UserTodoVO;

/**
 * <p>
 * 待办事项表 服务类
 * </p>
 *
 * @author AnYuan
 * @since 2025-08-05
 */
public interface UserTodoService extends IService<UserTodo> {

    UserTodoVO createUserTodo(UserTodoReqDTO userTodoReqDTO);
}
