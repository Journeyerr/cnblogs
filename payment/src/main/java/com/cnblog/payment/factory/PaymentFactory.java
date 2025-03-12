package com.cnblog.payment.factory;

import com.cnblog.payment.dto.Order;
import com.cnblog.payment.enums.PaymentTypeEnum;
import com.cnblog.payment.service.AliPaymentService;
import com.cnblog.payment.service.JdPaymentService;
import com.cnblog.payment.service.PaymentService;
import com.cnblog.payment.service.WxPaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PaymentFactory {
    @Autowired
    private  WxPaymentService wxPaymentService;
    @Autowired
    private AliPaymentService alipayService;
    @Autowired
    private JdPaymentService jdPaymentService;
    
    public void checkOrder(Order order) {
        // 校验参数
    }
    
    public PaymentService getPaymentService(PaymentTypeEnum paymentTypeEnum) {
        switch (paymentTypeEnum) {
            case WXPAY:
                return wxPaymentService;
            case ALIPAY:
                return alipayService;
            case JD:
                return jdPaymentService;
            default:
                throw new IllegalArgumentException("不支持的支付类型");
        }
    }
}
