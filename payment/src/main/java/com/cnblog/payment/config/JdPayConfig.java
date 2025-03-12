package com.cnblog.payment.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "pay.jdpay")
public class JdPayConfig {
    
    private String mchId;
    private String desKey;
    private String md5key;
    private String shaKey;
    private String userId;
    private String payUrl;
    private String notifyUrl;
    private String returnUrl;
    private String rsaPublicKey;
    private String rsaPrivateKey;
  
}
