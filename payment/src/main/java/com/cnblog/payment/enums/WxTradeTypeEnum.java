package com.cnblog.payment.enums;

import lombok.Getter;

@Getter
public enum WxTradeTypeEnum {
    
    APP("APP"),
    PC("NATIVE");
    
    private final String tradeType;
    
    WxTradeTypeEnum(String tradeType) {
        this.tradeType = tradeType;
    }
    
    public static WxTradeTypeEnum getByName(String tradeType) {
        for (WxTradeTypeEnum value : WxTradeTypeEnum.values()) {
            if (value.name().equals(tradeType)) {
                return value;
            }
        }
        return null;
    }
}
