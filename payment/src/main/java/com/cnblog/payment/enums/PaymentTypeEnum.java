package com.cnblog.payment.enums;

import lombok.Getter;

@Getter
public enum PaymentTypeEnum {
    
    ALIPAY("支付宝支付"),
    JD("京东原生支付"),
    WXPAY("微信支付"),
    UMS("银联聚合支付"),
    CMB("招商聚合支付");
    
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
