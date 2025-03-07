package com.cnblog.payment.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "pay.wechat")
@Data
public class WxPayProperties {
    
    /**
     * 微信appId
     */
    private String appId;
    /**
     * 微信商户号
     */
    private String mchId;
    /**
     * 微信商户密钥
     */
    private String mchKey;
    /**
     * 微信支付证书路径
     */
    private String keyPath;
    /**
     * 微信回调地址
     */
    private String notifyUrl;
    /**
     * 微信退款回调地址
     */
    private String refundNotifyUrl;
    /**
     * 微信交易类型  JSAPI/NATIVE/APP
     */
    private String tradeType;
    
}
