package com.cnblog.payment.service;

import com.cnblog.payment.config.WxPayProperties;
import com.cnblog.payment.dto.Order;
import com.cnblog.payment.dto.response.Response;
import com.github.binarywang.wxpay.bean.request.WxPayOrderQueryRequest;
import com.github.binarywang.wxpay.bean.request.WxPayRefundRequest;
import com.github.binarywang.wxpay.bean.request.WxPayUnifiedOrderRequest;
import com.github.binarywang.wxpay.bean.result.WxPayOrderQueryResult;
import com.github.binarywang.wxpay.bean.result.WxPayRefundResult;
import com.github.binarywang.wxpay.bean.result.WxPayUnifiedOrderResult;
import com.github.binarywang.wxpay.exception.WxPayException;
import com.github.binarywang.wxpay.service.WxPayService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@Slf4j
public class WxPaymentService extends PaymentService{
    
    @Autowired
    private WxPayService wxPayService;
    @Autowired
    private WxPayProperties properties;
    
    @Override
    public Response<WxPayUnifiedOrderResult> pay(Order order) {
        try {
            WxPayUnifiedOrderRequest request = WxPayUnifiedOrderRequest.newBuilder()
                .outTradeNo(order.getOrderNo())
                .productId(order.getOrderNo())
                .totalFee(order.getAmount().multiply(BigDecimal.valueOf(100)).intValue())
                .body(order.getSubject())
                .spbillCreateIp("127.0.0.1")
                .notifyUrl(properties.getNotifyUrl())
                .tradeType(properties.getTradeType())
                .build();
            WxPayUnifiedOrderResult result = wxPayService.unifiedOrder(request);
    
            return Response.success(result);
            
        }catch ( Exception e) {
            return Response.fail(e.getMessage());
        }
    }
    
    @Override
    public Response<WxPayOrderQueryResult> query(String orderNo) {
        WxPayOrderQueryRequest request = new WxPayOrderQueryRequest();
        request.setOutTradeNo(orderNo);
    
        try {
            WxPayOrderQueryResult result = wxPayService.queryOrder(request);
            return Response.success(result);
        }catch ( Exception e){
            return Response.fail(e.getMessage());
        }
    }
    
    @Override
    public Response refund(Order order) {
        WxPayRefundRequest wxPayRefundRequest = new WxPayRefundRequest();
        wxPayRefundRequest.setOutTradeNo(order.getOrderNo());
        wxPayRefundRequest.setOutRefundNo("R".concat(order.getOrderNo()));
        wxPayRefundRequest.setRefundFee(order.getAmount().multiply(new BigDecimal(100)).intValue());
        wxPayRefundRequest.setTotalFee(order.getAmount().multiply(new BigDecimal(100)).intValue());
        wxPayRefundRequest.setRefundDesc("退款");
        wxPayRefundRequest.setNotifyUrl(properties.getRefundNotifyUrl());
    
        try {
            WxPayRefundResult refund = wxPayService.refund(wxPayRefundRequest);
            return Response.success(refund);
        } catch (WxPayException e) {
            return Response.fail(e.getMessage());
        }
    }
}
