package com.cnblog.payment.service;

import com.cnblog.payment.config.WxPayProperties;
import com.cnblog.payment.dto.Order;
import com.cnblog.payment.vo.response.Response;
import com.cnblog.payment.enums.TradeTypeEnum;
import com.github.binarywang.wxpay.bean.notify.WxPayOrderNotifyResult;
import com.github.binarywang.wxpay.bean.request.WxPayOrderQueryRequest;
import com.github.binarywang.wxpay.bean.request.WxPayRefundRequest;
import com.github.binarywang.wxpay.bean.request.WxPayUnifiedOrderRequest;
import com.github.binarywang.wxpay.bean.result.WxPayOrderQueryResult;
import com.github.binarywang.wxpay.bean.result.WxPayRefundResult;
import com.github.binarywang.wxpay.bean.result.WxPayUnifiedOrderResult;
import com.github.binarywang.wxpay.exception.WxPayException;
import com.github.binarywang.wxpay.service.WxPayService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;

@Service
@Slf4j
public class WxPaymentService extends PaymentService{
    
    @Resource
    private WxPayService wxPayService;
    @Resource
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
                .tradeType(TradeTypeEnum.getByName(order.getTradeType()).getWxTradeType())
                .notifyUrl(properties.getNotifyUrl())
                .build();
            WxPayUnifiedOrderResult result = wxPayService.unifiedOrder(request);
            result.setXmlString("");
            return Response.success(result);
            
        }catch ( Exception e) {
            log.info("微信请求支付失败:{}", e.getMessage());
            return Response.fail("微信请求支付失败");
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
            log.info("微信请求查询失败:{}", e.getMessage());
            return Response.fail("微信请求查询失败");
        }
    }
    
    @Override
    public Response refund(Order order) {
        WxPayRefundRequest wxPayRefundRequest = new WxPayRefundRequest();
        wxPayRefundRequest.setOutTradeNo(order.getOrderNo());
        wxPayRefundRequest.setOutRefundNo(buildRefundNo(order.getOrderNo()));
        wxPayRefundRequest.setRefundFee(order.getAmount().multiply(new BigDecimal(100)).intValue());
        wxPayRefundRequest.setTotalFee(order.getAmount().multiply(new BigDecimal(100)).intValue());
        wxPayRefundRequest.setRefundDesc("退款");
        wxPayRefundRequest.setNotifyUrl(properties.getRefundNotifyUrl());
    
        try {
            WxPayRefundResult refund = wxPayService.refund(wxPayRefundRequest);
            return Response.success(refund);
        } catch (WxPayException e) {
            log.info("微信请求退款失败:{}", e.getMessage());
            return Response.fail("微信请求退款失败");
        }
    }
    
    @Override
    public void handleNotify(HttpServletRequest httpServletRequest) throws Exception {
        String xmlResult = IOUtils.toString(httpServletRequest.getInputStream(), httpServletRequest.getCharacterEncoding());
    
        // 转换xml格式数据为对象，并验证签名
        WxPayOrderNotifyResult notifyResult = wxPayService.parseOrderNotifyResult(xmlResult);
    
        // 商户订单号
        String outTradeNo = notifyResult.getOutTradeNo();
        
        // 处理订单状态
    }
}
