package com.cnblog.payment.enums;

import lombok.Getter;

@Getter
public enum PaymentTypeEnum {
    
    ALIPAY("支付宝"),
    WECHAT("微信");
    
    private String name;
    
    PaymentTypeEnum(String name) {
        this.name = name;
    }
    
    public static PaymentTypeEnum getByName(String name) {
        for (PaymentTypeEnum paymentTypeEnum : PaymentTypeEnum.values()) {
            if (paymentTypeEnum.getName().equals(name)) {
                return paymentTypeEnum;
            }
        }
        return WECHAT;
    }
}
