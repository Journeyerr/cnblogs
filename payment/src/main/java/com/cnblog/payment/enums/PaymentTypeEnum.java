package com.cnblog.payment.enums;

import lombok.Getter;

import java.util.Arrays;
import java.util.List;

@Getter
public enum PaymentTypeEnum {
    
    ALIPAY("支付宝"),
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
    
    public static List<String> names() {
        return Arrays.asList(ALIPAY.name(), WXPAY.name());
    }
}
