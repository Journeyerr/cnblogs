package com.hospital.exception;

import com.hospital.dto.ApiResponse;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
    
    @ExceptionHandler(Exception.class)
    public ApiResponse<?> handleException(Exception e) {
        e.printStackTrace();
//        return ApiResponse.error("服务器内部错误：请稍后再试");
        return ApiResponse.error(e.getMessage());
    }
    
    
}