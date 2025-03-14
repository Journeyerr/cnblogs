package com.cnblog.payment.service;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.XmlUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.cnblog.payment.config.JdPayConfig;
import com.cnblog.payment.constant.PayConstant;
import com.cnblog.payment.dto.Order;
import com.cnblog.payment.vo.response.Response;
import com.cnblog.payment.vo.JdPayVO;
import com.cnblog.payment.vo.JdQueryVO;
import com.ijpay.jdpay.JdPayApi;
import com.ijpay.jdpay.kit.JdPayKit;
import com.ijpay.jdpay.model.JdBaseModel;
import com.ijpay.jdpay.model.QueryOrderModel;
import com.ijpay.jdpay.model.RefundModel;
import com.ijpay.jdpay.model.UniOrderModel;
import com.ijpay.jdpay.util.RsaUtil;
import com.ijpay.jdpay.util.ThreeDesUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.net.URLEncoder;
import java.util.Base64;
import java.util.Date;
import java.util.Map;
import java.util.Objects;

@Service
@Slf4j
public class JdPaymentService extends PaymentService{
    
    @Autowired
    private JdPayConfig jdPayConfig;
    
    private String modelConvertToXml(JdBaseModel jdBaseModel) {
        return jdBaseModel.genReqXml(jdPayConfig.getRsaPrivateKey(),
            jdPayConfig.getDesKey(),
            PayConstant.JD_VERSION_2_0,
            jdPayConfig.getMchId());
    }
    
    private Map<String, Object> decryptAndConvertToMap(String response) throws Exception {
        // 解析响应的 xml 数据
        Map<String, String> map = JdPayKit.parseResp(response);
        log.info("京东请求加密结果：{}", JSONObject.toJSONString(map));
        if (!PayConstant.JD_PAY_STATUS_SUCCESS.equals(map.get("code"))) {
            throw new Exception(map.get("desc"));
        }
    
        // 解密结果
        Base64.Decoder decoder = Base64.getDecoder();
        String encryptData = new String(decoder.decode(map.get("encrypt")), "UTF-8");
        String decryptedData = ThreeDesUtil.decrypt4HexStr(RsaUtil.decryptBASE64(jdPayConfig.getDesKey()), encryptData);
    
        // 将解密后的数据转换为Map
        Map<String, Object> stringObjectMap = XmlUtil.xmlToMap(decryptedData);
        log.info("京东请求解密结果：{}", JSONObject.toJSONString(stringObjectMap));
        
        return stringObjectMap;
    }
    
    @Override
    public Response<JdPayVO> pay(Order order) {
        
        UniOrderModel orderModel = UniOrderModel.builder().version(PayConstant.JD_VERSION_2_0)
            .merchant(jdPayConfig.getMchId())
            .tradeNum(order.getOrderNo())
            .orderType(PayConstant.JD_ORDER_TYPE) // 实物订单
            .currency(PayConstant.JD_CURRENCY_CNY)
            .notifyUrl(jdPayConfig.getNotifyUrl())
            .userId(jdPayConfig.getUserId())
            .tradeName(PayConstant.PRODUCT_NAME)
            .amount(order.getAmount().movePointRight(2).toString())
            .expireTime(PayConstant.JD_ORDER_VALID_TIME)
            .tradeTime(DateUtil.format(new Date(), "yyyyMMddHHmmss"))
            .build();
    
        log.info("京东原生支付请求参数：{}", JSONObject.toJSONString(orderModel));
    
        try {
            String reqXml = modelConvertToXml(orderModel);
            // 发送支付请求
            String response = JdPayApi.uniOrder(reqXml);
            // 解析并构建支付链接
            return Response.success(handlePayResponse(response));
            
        } catch (Exception e) {
            e.printStackTrace();
            return Response.fail("京东支付请求失败");
        }
    }
    
    @Override
    public Response<JdQueryVO> query(String orderNo) {
        QueryOrderModel queryOrderModel = QueryOrderModel.builder()
            .version(PayConstant.JD_VERSION_2_0)
            .merchant(jdPayConfig.getMchId())
            .tradeNum(orderNo)
            .build();
        String reqXml = modelConvertToXml(queryOrderModel);
    
        try {
            // 发送支付请求
            String response = JdPayApi.queryOrder(reqXml);
            // 解析查询结果
            Map<String, Object> queryOrderResult = decryptAndConvertToMap(response);
            JdQueryVO JdQueryVO = JSON.parseObject(JSON.toJSONString(queryOrderResult), JdQueryVO.class);
            
            return Response.success(JdQueryVO);
            
        }catch ( Exception e) {
            log.info("京东请求查询失败:{}", e.getMessage());
            return Response.fail("京东请求查询失败");
        }
    }
    
    @Override
    public Response<?> refund(Order order) {
        
        RefundModel refundModel = RefundModel.builder()
            .version(PayConstant.JD_VERSION_2_0)
            .merchant(jdPayConfig.getMchId())
            .tradeNum(buildRefundNo(order.getOrderNo()))
            .oTradeNum(order.getOrderNo())
            .amount(order.getAmount().movePointRight(2).toString())
            .currency(PayConstant.JD_CURRENCY_CNY)
            .notifyUrl(jdPayConfig.getReturnUrl())
            .build();
    
        try {
            // 构建退款数据
            String refundXml = modelConvertToXml(refundModel);
            
            // 发送退款请求
            String refundResult = JdPayApi.refund(refundXml);
            
            // 解析退款结果
            Map<String, Object> refundResultMap = decryptAndConvertToMap(refundResult);
            
            return Response.success(refundResultMap);
            
        }catch ( Exception e) {
            e.printStackTrace();
            log.info("京东请求退款失败:{}", e.getMessage());
            return Response.fail("京东请求退款失败");
        }
    }
    
    @Override
    public void handleNotify(HttpServletRequest httpServletRequest) throws Exception {
        String xmlResult = IOUtils.toString(httpServletRequest.getInputStream(), httpServletRequest.getCharacterEncoding());
        Map<String, Object> stringObjectMap = decryptAndConvertToMap(xmlResult);
    
        // 支付商户号
        Object tradeNum = stringObjectMap.get("tradeNum");
        if (Objects.isNull(tradeNum)) {
            throw new Exception("京东支付回调通知失败");
        }
        
        // 处理订单状态
    }
    
    
    public JdPayVO handlePayResponse(String response) throws Exception {
    
        Map<String, Object> stringObjectMap = decryptAndConvertToMap(response);
        JdPayVO jdPayVO = JSON.parseObject(JSON.toJSONString(stringObjectMap), JdPayVO.class);
        
        // 构建sign
        String signStr = "merchant=" + jdPayConfig.getMchId() +
            "&orderId=" + jdPayVO.getOrderId() +
            "&key=" + jdPayConfig.getMd5key();
        String sign = JdPayKit.md5LowerCase(signStr);
    
        // 拼接支付链接返回前端
        String payUrl = jdPayConfig.getPayUrl() +
            "?" +
            "merchant=" + URLEncoder.encode(jdPayConfig.getMchId(), "UTF-8") +
            "&orderId=" + URLEncoder.encode(jdPayVO.getOrderId(), "UTF-8") +
            "&sign=" + URLEncoder.encode(sign, "UTF-8");
        
        jdPayVO.setPayUrl(payUrl);
        
        return jdPayVO;
    }
}
