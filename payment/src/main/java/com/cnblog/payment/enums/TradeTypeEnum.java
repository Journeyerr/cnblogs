package com.cnblog.payment.enums;

import lombok.Getter;

@Getter
public enum TradeTypeEnum {
    
    APP("APP", "QUICK_MSECURITY_PAY"),
    PC("NATIVE", "FAST_INSTANT_TRADE_PAY");
    
    private final String wxTradeType;
    private final String aliTradeType;
    
    TradeTypeEnum(String wxTradeType, String aliTradeType) {
        this.wxTradeType = wxTradeType;
        this.aliTradeType = aliTradeType;
    }
    
    public static TradeTypeEnum getByName(String tradeType) {
        for (TradeTypeEnum value : TradeTypeEnum.values()) {
            if (value.name().equals(tradeType)) {
                return value;
            }
        }
        return null;
    }
}
