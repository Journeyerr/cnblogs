package com.cnblog.payment.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class Order {
    
    /**
     * 订单编号
     */
    private String orderNo;
    
    /**
     * 订单金额
     */
    private BigDecimal amount;
    
    /**
     * 订单标题
     */
    private String subject;
    
    /**
     * 支付方式
     */
    private String paymentType;
    
    /**
     * 交易类型 PC APP
     */
    private String tradeType;
    
    /**
     * 渠道支付交易流水号
     */
    private String tradeNo;

}
