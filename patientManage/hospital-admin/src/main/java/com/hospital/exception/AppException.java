package com.hospital.exception;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class AppException extends RuntimeException {
    
    private String msg;
    private String code = "500";
    private String traceId;
    private Object data;
    
    public AppException(String msg) {
        super(msg);
        this.msg = msg;
    }
    
    public AppException(String msg, Throwable e) {
        super(msg, e);
        this.msg = msg;
    }
    
    public AppException(String msg, String code) {
        super(msg);
        this.msg = msg;
        this.code = code;
    }
    
    public AppException(String msg, String code, Throwable e) {
        super(msg, e);
        this.msg = msg;
        this.code = code;
    }
}
