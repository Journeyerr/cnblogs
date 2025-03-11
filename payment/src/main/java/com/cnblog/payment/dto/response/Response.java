package com.cnblog.payment.dto.response;

import lombok.Data;

@Data
public class Response<T> {
    
    private String code;
    
    private String message;
    
    private Object data;
    
    public static <T> Response<T> success(T data) {
        Response<T> response = new Response<>();
        response.setCode("0000");
        response.setMessage("成功");
        response.setData(data);
        return response;
    }
    
    public static <T> Response<T> success() {
        Response<T> response = new Response<>();
        response.setCode("0000");
        response.setMessage("成功");
        return response;
    }

    public static <T> Response<T> fail(String message) {
        Response<T> response = new Response<>();
        response.setCode("500");
        response.setMessage(message);
        return response;
    }
}
