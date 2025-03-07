package com.cnblog.payment.service;

import com.cnblog.payment.dto.Order;
import com.cnblog.payment.dto.response.Response;
import org.springframework.stereotype.Service;

/**
 * 支付服务
 * @author AnYuan
 */

@Service
public abstract class PaymentService {
    
    /**
     * 支付
     * @param order 订单信息
     * @return 支付结果
     */
    public abstract Response<?> pay(Order order);
    
    /**
     * 查询
     * @param orderNo 订单号
     * @return 查询结果
     */
    public abstract Response<?> query(String orderNo);
    
    /**
     * 退款
     * @param order 订单信息
     * @return 结果
     */
    public abstract Response<?> refund(Order order);
}
