package com.cnblog.payment.config;

import com.github.binarywang.wxpay.config.WxPayConfig;
import com.github.binarywang.wxpay.service.WxPayService;
import com.github.binarywang.wxpay.service.impl.WxPayServiceImpl;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnClass(WxPayService.class)
@AllArgsConstructor
@Slf4j
public class WxPayConfiguration {
    
    private final WxPayProperties properties;
    
    @Bean
    public WxPayService wxPayService() {
    
        WxPayConfig payConfig = new WxPayConfig();
        payConfig.setAppId(properties.getAppId());
        payConfig.setMchId(properties.getMchId());
        payConfig.setMchKey(properties.getMchKey());
        payConfig.setKeyPath(properties.getKeyPath());
        payConfig.setTradeType(properties.getTradeType());
    
        WxPayServiceImpl wxPayService = new WxPayServiceImpl();
    
        wxPayService.setConfig(payConfig);
        
        return wxPayService;
    }
}
