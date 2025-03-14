package com.cnblog.payment.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "pay.ums")
public class UmsPayConfig {
    
    /**
     * 商户号 服务商分配字段
     */
    private String mchId;
    /**
     * 系统来源 服务商分配字段
     */
    private String msgSrc;
    /**
     * 签名key 服务商分配字段
     */
    private String signKey;
    /**
     * 终端号 服务商分配字段
     */
    private String tid;
    /**
     * 支付回调地址
     */
    private String notifyUrl;
    /**
     * 支付回调地址
     */
    private String returnUrl;
    /**
     * 支付接口
     */
    private String payUrl;
    
    
  
}
