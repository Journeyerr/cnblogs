package com.cnblog.payment.service;

import com.alibaba.fastjson2.JSONObject;
import com.alipay.api.AlipayClient;
import com.alipay.api.AlipayResponse;
import com.alipay.api.domain.AlipayTradePagePayModel;
import com.alipay.api.internal.util.AlipaySignature;
import com.alipay.api.request.AlipayTradePagePayRequest;
import com.alipay.api.request.AlipayTradeQueryRequest;
import com.alipay.api.request.AlipayTradeRefundRequest;
import com.alipay.api.response.AlipayTradeQueryResponse;
import com.alipay.api.response.AlipayTradeRefundResponse;
import com.cnblog.payment.config.AliPayConfig;
import com.cnblog.payment.constant.PayConstant;
import com.cnblog.payment.dto.Order;
import com.cnblog.payment.vo.response.Response;
import com.cnblog.payment.enums.TradeTypeEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

/**
 * @author AnYuan
 * @description 支付宝原生支付服务
 */

@Service
@Slf4j
public class AliPaymentService extends PaymentService{
    
    @Autowired
    private AlipayClient alipayClient;
    @Autowired
    private AliPayConfig alipayConfig;
    
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
        pagePayModel.setTimeoutExpress(PayConstant.ALI_TIMEOUT_EXPRESS);
    
        if (TradeTypeEnum.PC.name().equals(order.getTradeType())) {
            //PC扫码支付的方式:订单码-可定义宽度的嵌入式二维码
            pagePayModel.setQrPayMode(PayConstant.ALI_QR_PAY_MODE);
            // 商户自定义二维码宽度
//        pagePayModel.setQrcodeWidth(PayConstant.AliPayConstants.ALIPAY_PC_QR_WIDTH);
        }
    
        alipayRequest.setBizModel(pagePayModel);
        try {
    
            AlipayResponse response;
            if (TradeTypeEnum.PC.name().equals(order.getTradeType())) {
                
                log.info("支付宝支付请求参数：{}", JSONObject.toJSONString(alipayRequest));
                response = alipayClient.pageExecute(alipayRequest);
                log.info("支付宝支付请求结果：{}", JSONObject.toJSONString(alipayRequest));
                
                return Response.success(response);
                
            } else if (TradeTypeEnum.APP.name().equals(order.getTradeType())) {
    
                response = alipayClient.sdkExecute(alipayRequest);
                return Response.success(response);
                
            } else {
                return Response.fail("不支持的支付类型");
            }
        }catch (Exception e) {
            log.info("支付宝请求支付失败:{}", e.getMessage());
            return Response.fail("支付宝请求支付失败");
        }
    }
    
    @Override
    public Response<AlipayTradeQueryResponse> query(String orderNo) {
        AlipayTradeQueryRequest request = new AlipayTradeQueryRequest();
        JSONObject bizContent = new JSONObject();
        bizContent.put("out_trade_no", orderNo);
        request.setBizContent(bizContent.toJSONString());
    
        try {
            log.info("支付宝查询请求参数：{}", JSONObject.toJSONString(request));
            AlipayTradeQueryResponse response = alipayClient.execute(request);
            return Response.success(response);
        } catch (Exception e) {
            log.info("支付宝请求查询失败:{}", e.getMessage());
            return Response.fail("支付宝请求查询失败");
        }
    }
    
    @Override
    public Response refund(Order order) {
        AlipayTradeRefundRequest request = new AlipayTradeRefundRequest();
        JSONObject bizContent = new JSONObject();
        bizContent.put("out_trade_no", order.getOrderNo());
        bizContent.put("refund_amount", order.getAmount());
        
        request.setReturnUrl(alipayConfig.getReturnUrl());
        request.setBizContent(bizContent.toJSONString());
    
        try {
            log.info("支付宝退款请求参数：{}", JSONObject.toJSONString(request));
            AlipayTradeRefundResponse response = alipayClient.execute(request);
            return Response.success(response);
        } catch (Exception e) {
            log.info("支付宝请求退款失败:{}", e.getMessage());
            return Response.fail("支付宝请求退款失败");
        }
    }
    
    @Override
    public void handleNotify(HttpServletRequest httpServletRequest) throws Exception {
        Map<String, String> params = new HashMap<>();
        Enumeration<String> paramNames = httpServletRequest.getParameterNames();
        while (paramNames.hasMoreElements()) {
            String paramName = paramNames.nextElement();
            String paramValue = httpServletRequest.getParameter(paramName);
            params.put(paramName, paramValue);
        }
    
        try {
            boolean signVerified = AlipaySignature.rsaCheckV1( params, alipayConfig.getPublicKey(), PayConstant.UTF_8, PayConstant.SIGN_TYPE_RSA2);
        
            if (!signVerified) {
                log.info("支付宝回调通知签名验证失败: {}", params);
                throw new Exception("签名验证失败");
            }
            
            // 商户订单号
            String orderNO = params.get("out_trade_no");
            // 支付状态
            String tradeStatus = params.get("trade_status");
            
            // 处理订单状态
        
        } catch (Exception e) {
            log.info("支付宝回调通知失败:{}", e.getMessage());
            throw new Exception("支付宝回调通知处理失败");
        }
    }
}
