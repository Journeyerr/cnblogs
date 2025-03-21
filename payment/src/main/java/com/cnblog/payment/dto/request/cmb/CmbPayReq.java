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
 * @description 招商聚合支付请求支付参数
 */

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CmbPayReq {
    
    /**
     * 交易币种，默认156
     */
    private String currencyCode;
    
    /**
     * 商户号
     */
    private String merId;
    
    /**
     * 支付通知地址
     */
    private String notifyUrl;
    /**
     * 订单号
     */
    private String orderId;
    
    /**
     * 支付有效时间
     */
    private String payValidTime;
    
    /**
     * 交易金额,单位为分(必传)
     */
    private String txnAmt;
    
    /**
     *  OFFLINE:线下
     *  INSURANCE：保险
     *  CHARITY：公益
     */
    private String tradeScene;
    
    
    
}
