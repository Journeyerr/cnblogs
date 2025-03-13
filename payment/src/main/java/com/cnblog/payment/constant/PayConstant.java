package com.cnblog.payment.constant;

/**
 * @author AnYuan
 * @desc 支付常量
 */

public class PayConstant {
    
    /**
     * 退款单号前缀
     */
    public static final String REFUND_NO_PREFIX = "R";
    
    
    /**
     * 支付宝------------
     */
    public static final String ALI_QR_PAY_MODE = "4";
    public static final String ALI_TIMEOUT_EXPRESS = "5m";
    
    
    /**
     * 京东------------
     */
    
    public static final String JD_VERSION_2_0 = "V2.0";
    public static final String JD_CURRENCY_CNY = "CNY";
    /**
     * 订单类型：0 实物订单
     */
    public static final String JD_ORDER_TYPE = "0";
    public static final String JD_TRADE_NAME = "商品支付";
    /**
     * 支付成功状态码
     */
    public static final String JD_PAY_STATUS_SUCCESS = "000000";
    /**
     * 订单有效时间 300秒
     */
    public static final String JD_ORDER_VALID_TIME = "300";
}
