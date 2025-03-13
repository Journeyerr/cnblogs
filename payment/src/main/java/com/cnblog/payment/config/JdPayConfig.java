package com.cnblog.payment.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "pay.jdpay")
public class JdPayConfig {
    
    /**
     * 商户号
     */
    private String mchId;
    /**
     * DES密钥
     */
    private String desKey;
    /**
     * MD5密钥
     */
    private String md5key;
    /**
     * SHA密钥
     */
    private String shaKey;
    /**
     * jd分配的userId
     */
    private String userId;
    /**
     * 支付地址
     */
    private String payUrl;
    /**
     * 支付回调地址
     */
    private String notifyUrl;
    /**
     * 支付回调地址
     */
    private String returnUrl;
    /**
     * RSA公钥
     */
    private String rsaPublicKey;
    /**
     * RSA私钥
     */
    private String rsaPrivateKey;
  
}
