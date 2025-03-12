package com.cnblog.payment.enums;

import lombok.Getter;

@Getter
public enum PaymentTypeEnum {
    
    ALIPAY("支付宝"),
    JD("京东"),
    WXPAY("微信");
    
    private String desc;
    
    PaymentTypeEnum(String desc) {
        this.desc = desc;
    }
    
    public static PaymentTypeEnum getByName(String name) {
        for (PaymentTypeEnum paymentTypeEnum : PaymentTypeEnum.values()) {
            if (paymentTypeEnum.name().equals(name)) {
                return paymentTypeEnum;
            }
        }
        return null;
    }
}
