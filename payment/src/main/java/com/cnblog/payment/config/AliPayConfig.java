package com.cnblog.payment.config;

import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "pay.alipay")
public class AliPayConfig {
    
    /**
     * 支付宝开放平台分配的appId
     */
    private String appId;
    /**
     * 支付宝开放平台分配的私钥
     */
    private String privateKey;
    /**
     * 支付宝开放平台分配的公钥
     */
    private String publicKey;
    /**
     * 支付宝开放平台分配的网关地址
     */
    private String serverUrl;
    /**
     * 支付回调通知地址
     */
    private String notifyUrl;
    /**
     * 支付成功后跳转地址
     */
    private String returnUrl;
    /**
     * 签名方式
     */
    private String signType = "RSA2";
    private String charset = "UTF-8";
    private String format = "json";
    
    @Bean
    public AlipayClient alipayClient() {
        return new DefaultAlipayClient(
            serverUrl,
            appId,
            privateKey,
            format,
            charset,
            publicKey,
            signType
        );
    }
}
