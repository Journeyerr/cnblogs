package com.cnblog.payment.service;

import com.alibaba.fastjson.JSONObject;
import com.cnblog.payment.config.UmsPayConfig;
import com.cnblog.payment.constant.PayConstant;
import com.cnblog.payment.dto.Order;
import com.cnblog.payment.dto.request.ums.PayRequest;
import com.cnblog.payment.utils.UmsPayUtil;
import com.cnblog.payment.vo.response.Response;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * @author AnYuan
 * @description 银联商务聚合支付
 */

@Service
@Slf4j
public class UmsPaymentService extends PaymentService{
    
    @Autowired
    private UmsPayConfig umsPayConfig;
    
    @Override
    public Response<?> pay(Order order) {
    
        LocalDateTime nowTime = LocalDateTime.now();
        LocalDateTime expireTime = nowTime.plusMinutes(PayConstant.UMS_TIMEOUT_EXPRESS);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        PayRequest build = PayRequest.builder()
            .mid(umsPayConfig.getMchId())
            .tid(umsPayConfig.getTid())
            .totalAmount(order.getAmount().movePointRight(2).toString())
            .reqTime(nowTime.format(formatter))
            .tranTime(nowTime.format(formatter))
            .expireTime(expireTime.format(formatter))
            .notifyUrl(umsPayConfig.getNotifyUrl())
            .returnUrl(umsPayConfig.getReturnUrl())
            .billNo(PayConstant.UMS_ORDER_PREFIX.concat(order.getOrderNo()))
            .orderDesc(order.getSubject())
            .signType(PayConstant.UMS_SIGN_TYPE)
            .msgType(PayConstant.UMS_MSG_TYPE)
            .msgSrc(umsPayConfig.getMsgSrc())
            .requestTimestamp(nowTime.format(formatter))
            .build();
    
        String sign = UmsPayUtil.getSign(umsPayConfig.getSignKey(), JSONObject.toJSONString(build));
        build.setSign(sign);
        
        // 将 build 对象转换为 JSON 字符串
        String requestBody = JSONObject.toJSONString(build);
    
        try {
            // 发送 POST 请求并获取响应
            String payResult = UmsPayUtil.sendPost(umsPayConfig.getPayUrl(), requestBody);
            // 处理响应
            return Response.success(JSONObject.parseObject(payResult));
        } catch (Exception e) {
            e.printStackTrace();
            log.error("银联商务聚合支付请求失败", e);
            return Response.fail("银联商务聚合支付请求失败");
        }
    }
    
    @Override
    public Response<?> query(String orderNo) {
        return null;
    }
    
    @Override
    public Response<?> refund(Order order) {
        return null;
    }
    
    @Override
    public void handleNotify(HttpServletRequest httpServletRequest) throws Exception {
    
    }
}
