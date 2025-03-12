package com.cnblog.payment.controller;

import com.cnblog.payment.dto.Order;
import com.cnblog.payment.dto.response.Response;
import com.cnblog.payment.enums.PaymentTypeEnum;
import com.cnblog.payment.factory.PaymentFactory;
import com.cnblog.payment.service.AliPaymentService;
import com.cnblog.payment.service.PaymentService;
import com.cnblog.payment.service.WxPaymentService;
import com.github.binarywang.wxpay.bean.notify.WxPayNotifyResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * 支付控制器
 * author: AnYuan
 */

@RestController
@RequestMapping("/api")
public class PaymentController {
    
    @Autowired
    private PaymentFactory paymentFactory;
    @Autowired
    private WxPaymentService wxPaymentService;
    @Autowired
    private AliPaymentService aliPaymentService;
    
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
    
    @PostMapping("/notify/wx")
    public String wxHandleNotify(HttpServletRequest request) {
        try {
            wxPaymentService.handleNotify(request);
            return WxPayNotifyResponse.success("回调处理成功");
        } catch (Exception e) {
            return WxPayNotifyResponse.failResp("回调处理失败");
        }
    }
    
    @PostMapping("/notify/ali")
    public String aliHandleNotify(HttpServletRequest request) {
        try {
            aliPaymentService.handleNotify(request);
            return "success";
        } catch (Exception e) {
            return "failure";
        }
    }
}
