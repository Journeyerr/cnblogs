package com.hospital.controller;


import com.hospital.dto.ApiResponse;
import com.hospital.dto.UserTodoReqDTO;
import com.hospital.service.UserTodoService;
import com.hospital.vo.UserTodoVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * <p>
 * 待办事项表 前端控制器
 * </p>
 *
 * @author AnYuan
 * @since 2025-08-05
 */
@RestController
@RequestMapping("/user/todo")
public class UserTodoController {
    
    @Autowired
    private UserTodoService userTodoService;
    
    @RequestMapping("/create")
    public ApiResponse<UserTodoVO> createUserTodo(@Valid @RequestBody UserTodoReqDTO userTodoReqDTO) {
        return ApiResponse.success(userTodoService.createUserTodo(userTodoReqDTO));
    }
}

