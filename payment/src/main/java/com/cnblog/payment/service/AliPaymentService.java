package com.cnblog.payment.service;

import com.alibaba.fastjson2.JSONObject;
import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.AlipayResponse;
import com.alipay.api.domain.AlipayTradePagePayModel;
import com.alipay.api.request.AlipayTradePagePayRequest;
import com.alipay.api.response.AlipayTradeAppPayResponse;
import com.alipay.api.response.AlipayTradePagePayResponse;
import com.cnblog.payment.config.AlipayConfig;
import com.cnblog.payment.dto.Order;
import com.cnblog.payment.dto.response.Response;
import com.cnblog.payment.enums.TradeTypeEnum;
import com.github.binarywang.wxpay.bean.result.WxPayOrderQueryResult;
import com.github.binarywang.wxpay.bean.result.WxPayUnifiedOrderResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class AliPaymentService extends PaymentService{
    
    private static final String FAST_INSTANT_TRADE_PAY = "FAST_INSTANT_TRADE_PAY";
    
    private static final String QR_PAY_MODE = "4";
    
    private static final String TIMEOUT_EXPRESS = "5m";
    
    @Autowired
    private AlipayClient alipayClient;
    
    @Autowired
    private AlipayConfig alipayConfig;
    
    @Override
    public Response<AlipayResponse> pay(Order order) {
        
        AlipayTradePagePayRequest alipayRequest = new AlipayTradePagePayRequest();
        alipayRequest.setReturnUrl(alipayConfig.getReturnUrl());
        alipayRequest.setNotifyUrl(alipayConfig.getNotifyUrl());
    
        AlipayTradePagePayModel pagePayModel = new AlipayTradePagePayModel();
        pagePayModel.setOutTradeNo(order.getOrderNo());
        pagePayModel.setSubject(order.getSubject());
        pagePayModel.setTotalAmount(order.getAmount().toString());
    
        //销售产品码，与支付宝签约的产品码名称。注：目前电脑支付场景下仅支持FAST_INSTANT_TRADE_PAY
        pagePayModel.setProductCode(TradeTypeEnum.getByName(order.getTradeType()).getAliTradeType());
        // 订单超时时间
        pagePayModel.setTimeoutExpress(TIMEOUT_EXPRESS);
    
        if (TradeTypeEnum.PC.name().equals(order.getTradeType())) {
            //PC扫码支付的方式:订单码-可定义宽度的嵌入式二维码
            pagePayModel.setQrPayMode(QR_PAY_MODE);
            // 商户自定义二维码宽度
//        pagePayModel.setQrcodeWidth(PayConstant.AliPayConstants.ALIPAY_PC_QR_WIDTH);
        }
    
        alipayRequest.setBizModel(pagePayModel);
        try {
    
            AlipayResponse response;
            if (TradeTypeEnum.PC.name().equals(order.getTradeType())) {
                response = alipayClient.pageExecute(alipayRequest);
                return Response.success(response);
                
            } else if (TradeTypeEnum.APP.name().equals(order.getTradeType())) {
    
                response = alipayClient.sdkExecute(alipayRequest);
                return Response.success(response);
                
            } else {
                return Response.fail("不支持的支付类型");
            }
        }catch (Exception e) {
            e.printStackTrace();
            return Response.fail(e.getMessage());
        }
    }
    
    @Override
    public Response<WxPayOrderQueryResult> query(String orderNo) {
        return null;
    }
    
    @Override
    public Response refund(Order order) {
        return null;
    }
    
    private AlipayTradePagePayResponse aliPageOrder(Order order) throws AlipayApiException{
        
        AlipayTradePagePayRequest alipayRequest = new AlipayTradePagePayRequest();
        alipayRequest.setReturnUrl(alipayConfig.getReturnUrl());
        alipayRequest.setNotifyUrl(alipayConfig.getNotifyUrl());
    
        AlipayTradePagePayModel pagePayModel = new AlipayTradePagePayModel();
        pagePayModel.setOutTradeNo(order.getOrderNo());
        pagePayModel.setSubject(order.getSubject());
        pagePayModel.setTotalAmount(order.getAmount().toString());
        
        //销售产品码，与支付宝签约的产品码名称。注：目前电脑支付场景下仅支持FAST_INSTANT_TRADE_PAY
        pagePayModel.setProductCode(FAST_INSTANT_TRADE_PAY);
        //PC扫码支付的方式:订单码-可定义宽度的嵌入式二维码
        pagePayModel.setQrPayMode(QR_PAY_MODE);
        // 订单超时时间
        pagePayModel.setTimeoutExpress(TIMEOUT_EXPRESS);
    
        // 商户自定义二维码宽度
//        pagePayModel.setQrcodeWidth(PayConstant.AliPayConstants.ALIPAY_PC_QR_WIDTH);
    
        alipayRequest.setBizModel(pagePayModel);
    
        return alipayClient.execute(alipayRequest);
    }
    
}
