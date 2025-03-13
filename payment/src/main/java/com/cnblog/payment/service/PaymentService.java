package com.cnblog.payment.service;

import com.cnblog.payment.constant.PayConstant;
import com.cnblog.payment.dto.Order;
import com.cnblog.payment.vo.response.Response;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;

/**
 * 支付服务
 * @author AnYuan
 */

@Service
public abstract class PaymentService {
    
    protected String buildRefundNo(String orderNo) {
        return PayConstant.REFUND_NO_PREFIX.concat(orderNo);
    }
    
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
    
    /**
     * 回调处理
     * @param httpServletRequest
     */
    public abstract void handleNotify(HttpServletRequest httpServletRequest) throws Exception;
}
