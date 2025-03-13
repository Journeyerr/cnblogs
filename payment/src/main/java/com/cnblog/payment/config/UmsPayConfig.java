package com.cnblog.payment.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "pay.ums")
public class UmsPayConfig {
    
    /**
     * 商户号
     */
    private String mchId;
    /**
     * 终端号
     */
    private String tid;
    /**
     * 合并单号前缀
     */
    private String msgId;
    /**
     * 支付回调地址
     */
    private String notifyUrl;
    /**
     * 支付回调地址
     */
    private String returnUrl;
  
}
