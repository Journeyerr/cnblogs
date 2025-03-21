package com.cnblog.payment.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "pay.cmb")
public class CmbPayConfig {

    /**
     * 商户号
     */
    private String mchId;
    /**
     * 应用ID
     */
    private String appId;
    /**
     * 应用密钥
     */
    private String secret;
    /**
     * 招行支付回调地址
     */
    private String notifyUrl;
    /**
     * 招行退款回调地址
     */
    private String returnUrl;
    /**
     * 招行公钥
     */
    private String cmbPublicKey;
    /**
     * 商户私钥
     */
    private String mchPrivateKey;
    
    
    
}
