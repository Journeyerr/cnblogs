package com.cnblog.seckill.mq;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderMessage implements Serializable {
    
    private String orderId;
    
    private Long userId;
    
    private String sku;
   
}
