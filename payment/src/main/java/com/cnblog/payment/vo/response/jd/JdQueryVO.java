package com.cnblog.payment.vo.response.jd;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class JdQueryVO {
    
    /**
     *  金额
     */
    private BigDecimal amount;
    
    /**
     * 订单支付状态
     */
    private String status;
    
    
    /**
     * 商户订单号
     */
    private String tradeNum;
    
}
