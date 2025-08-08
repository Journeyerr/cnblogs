package com.hospital.controller;


import com.hospital.dto.ApiResponse;
import com.hospital.dto.UserTodoReqDTO;
import com.hospital.service.UserTodoService;
import com.hospital.vo.UserTodoVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

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
    
    @PostMapping("/create")
    public ApiResponse<UserTodoVO> create(@Valid @RequestBody UserTodoReqDTO userTodoReqDTO) {
        return ApiResponse.success(userTodoService.create(userTodoReqDTO));
    }
    
    @GetMapping("/list")
    public ApiResponse<List<UserTodoVO>> list(@RequestParam(required = false) Integer status) {
        return ApiResponse.success(userTodoService.list(status));
    }
    
    @PostMapping("/update")
    public ApiResponse<UserTodoVO> update(@RequestBody UserTodoReqDTO userTodoReqDTO) {
        return ApiResponse.success(userTodoService.update(userTodoReqDTO));
    }
    
    @PostMapping("/delete/{id}")
    public ApiResponse<Boolean> update(@PathVariable Long id) {
        return ApiResponse.success(userTodoService.delete(id));
    }
}

