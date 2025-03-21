package com.cnblog.payment.dto.request.cmb;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Created with IntelliJ IDEA.
 *
 * @User: AnYuan
 * @Date: 2025/03/
 * @description 招商聚合支付请求退款参数
 */

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CmbRefundReq {
    
    /**
     * 商户号
     */
    private String merId;
    
    /**
     * 订单号
     */
    private String orderId;
    
    /**
     * 交易金额,单位为分(必传)
     */
    private String txnAmt;
    
    /**
     * 需要退款金额,单位为分(必传)
     */
    private String refundAmt;
    
    /**
     * 交易通知地址(必传)
     */
    private String notifyUrl;
    
    /**
     * 招行订单号
     */
    private String origCmbOrderId;
    
}
