package com.cnblog.seckill.dto;

import lombok.Data;

@Data
public class SecKillRequest {
    private Long userId;
    
    private String sku;
}