package com.cnblog.payment.vo;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class JdPayVO {
    
    /**
     * 支付链接
     */
    private String payUrl;
    
    /**
     *  金额
     */
    private BigDecimal amount;
    
    /**
     * 过期时间
     */
    private String expireTime;
    
    /**
     * 订单号
     */
    private String orderId;
    
    /**
     * 签名
     */
    private String sign;
    
    /**
     * 商户订单号
     */
    private String tradeNum;
    
}
