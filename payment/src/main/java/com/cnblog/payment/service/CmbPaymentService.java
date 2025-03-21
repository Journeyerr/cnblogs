package com.cnblog.payment.service;

import com.alibaba.fastjson2.JSONObject;
import com.cnblog.payment.config.CmbPayConfig;
import com.cnblog.payment.constant.PayConstant;
import com.cnblog.payment.dto.Order;
import com.cnblog.payment.dto.request.cmb.CmbBaseReq;
import com.cnblog.payment.dto.request.cmb.CmbPayReq;
import com.cnblog.payment.dto.request.cmb.CmbQueryReq;
import com.cnblog.payment.dto.request.cmb.CmbRefundReq;
import com.cnblog.payment.dto.res.cmb.CmbBaseRes;
import com.cnblog.payment.dto.res.cmb.CmbOrderRes;
import com.cnblog.payment.utils.CmbUtil;
import com.cnblog.payment.vo.response.Response;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 * @author AnYuan
 * @description 招商聚合支付
 */

@Service
@Slf4j
public class CmbPaymentService extends PaymentService{
    
    @Autowired
    private CmbPayConfig cmbPayConfig;
    
    @Override
    public Response<?> pay(Order order) {
    
        try {
    
            CmbPayReq cmbPayReq = CmbPayReq.builder()
                .currencyCode(PayConstant.CMB_CURRENCY_CODE)
                .tradeScene(PayConstant.CMB_TRADE_SCENE)
                .currencyCode(PayConstant.CMB_CURRENCY_CODE)
                .payValidTime(PayConstant.CMB_ORDER_VALID_TIME)
                .merId(cmbPayConfig.getMchId())
                .orderId(order.getOrderNo())
                .notifyUrl(cmbPayConfig.getNotifyUrl())
                .txnAmt(order.getAmount().multiply(BigDecimal.valueOf(100)).toString())
                .build();
    
            CmbOrderRes cmbOrderRes = request(PayConstant.CMB_PAY_URL, JSONObject.toJSONString(cmbPayReq), CmbOrderRes.class);
    
            return Response.success(cmbOrderRes);
            
        }catch (RuntimeException e) {
            log.info("招商聚合请求支付失败:{}", e.getMessage());
            return Response.fail(e.getMessage());
        }catch (Exception e) {
            log.info("招商聚合请求支付失败");
            return Response.fail("招商聚合请求支付失败");
        }
    }
    
    @Override
    public Response<?> query(String orderNo) {
    
        try {
            CmbQueryReq cmbQueryReq = CmbQueryReq.builder()
                .merId(cmbPayConfig.getMchId())
                .orderId(orderNo)
                .build();
    
            CmbOrderRes cmbOrderRes = request(PayConstant.CMB_QUERY_URL, JSONObject.toJSONString(cmbQueryReq), CmbOrderRes.class);
    
            return Response.success(cmbOrderRes);
        } catch (Exception e) {
            log.info("招商聚合请求查询失败");
            return Response.fail("招商聚合请求查询失败");
        }
    }
    
    @Override
    public Response<?> refund(Order order) {
    
        try {
    
            CmbRefundReq cmbRefundReq = CmbRefundReq.builder()
                .merId(cmbPayConfig.getMchId())
                .orderId(order.getOrderNo())
                .txnAmt(order.getAmount().multiply(BigDecimal.valueOf(100)).toString())
                .refundAmt(order.getAmount().multiply(BigDecimal.valueOf(100)).toString())
                .notifyUrl(cmbPayConfig.getReturnUrl())
                .origCmbOrderId(order.getTradeNo())
                .build();
    
            CmbOrderRes cmbOrderRes = request(PayConstant.CMB_REFUND_URL, JSONObject.toJSONString(cmbRefundReq), CmbOrderRes.class);
    
            return Response.success(cmbOrderRes);
        } catch (Exception e) {
            log.info("招商聚合请求退款失败");
            return Response.fail("招商聚合请求查询失败");
        }
    }
    
    @Override
    public void handleNotify(HttpServletRequest httpServletRequest) throws Exception {
        // TODO
    }
    
    private <T> T request(String url, String reqBody, Class<T> tClass) throws Exception {
    
        String timestamp = String.valueOf(new Date().getTime() / 1000);
        String appId = cmbPayConfig.getAppId();
        String secret = cmbPayConfig.getSecret();
        
        String requestBody = JSONObject.toJSONString(new CmbBaseReq(reqBody, url));
    
        // 计算 sign 的值：用来计算header的 apiSign
        String sign = CmbUtil.sm3Signature2(requestBody);
        //拼接参数
        List<String> list = new ArrayList<>();
        list.add("appid=" + appId);
        list.add("secret=" + secret);
        list.add("sign=" + sign);
        list.add("timestamp=" + timestamp);
        String src = String.join("&", list);
    
        // 计算 apiSign 的值: 放在header位置的加密参数
        String apiSign = CmbUtil.sm3withsm2Signature2(cmbPayConfig.getMchPrivateKey(), src);
        
        RestTemplate client = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        client.getMessageConverters().add(0, new StringHttpMessageConverter(StandardCharsets.UTF_8));
    
        // 以json的方式提交
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add("appid", appId);
        headers.add("timestamp", timestamp);
        headers.add("sign", sign);
        headers.add("apisign", apiSign.toUpperCase());
        headers.add("verify", "SM3withSM2");
    
        HttpEntity<String> requestEntity = new HttpEntity<>(requestBody, headers);
    
        log.info("招商聚合请求接口：{}, 请求参数：{}", url, requestBody);
        String body = client.postForEntity(url, requestEntity, String.class).getBody();
        log.info("招商聚合请求接口：{}, 请求结果：{}", url, body);
    
        CmbBaseRes cmbBaseRes = JSONObject.parseObject(body, CmbBaseRes.class);
        if (Objects.isNull(cmbBaseRes) || !Objects.equals(PayConstant.CMB_SUCCESS, cmbBaseRes.getReturnCode())) {
            throw new Exception("招商聚合请求失败");
        }
        
        if (!Objects.equals(PayConstant.CMB_SUCCESS, cmbBaseRes.getRespCode())) {
            throw new RuntimeException(cmbBaseRes.getRespMsg());
        }
    
        return JSONObject.parseObject(cmbBaseRes.getBizContent(), tClass);
    }
}
