package com.cnblog.payment.controller;

import com.cnblog.payment.dto.Order;
import com.cnblog.payment.dto.response.Response;
import com.cnblog.payment.enums.PaymentTypeEnum;
import com.cnblog.payment.factory.PaymentFactory;
import com.cnblog.payment.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 支付控制器
 * author: AnYuan
 */

@RestController
@RequestMapping("/api")
public class PaymentController {
    
    @Autowired
    private PaymentFactory paymentFactory;
    
    @PostMapping("/pay")
    public Response<?> pay(@RequestBody Order order){
    
        // 校验订单参数
        paymentFactory.checkOrder(order);
        
        PaymentService paymentService = paymentFactory.getPaymentService(PaymentTypeEnum.getByName(order.getPaymentType()));
        return paymentService.pay(order);
    }
    
    @GetMapping("/query/{orderNo}/{paymentType}")
    public Response<?> query(@PathVariable("orderNo") String orderNo, @PathVariable("paymentType") String paymentType) {
        PaymentService paymentService = paymentFactory.getPaymentService(PaymentTypeEnum.getByName(paymentType));
        return paymentService.query(orderNo);
    }
    
    @PostMapping("/refund")
    public Response<?> refund(@RequestBody Order order) {
        PaymentService paymentService = paymentFactory.getPaymentService(PaymentTypeEnum.getByName(order.getPaymentType()));
        return paymentService.refund(order);
    }
}
