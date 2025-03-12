package com.cnblog.payment.service;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.XmlUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alipay.api.response.AlipayTradeQueryResponse;
import com.cnblog.payment.config.JdPayConfig;
import com.cnblog.payment.constant.PayConstant;
import com.cnblog.payment.dto.Order;
import com.cnblog.payment.dto.response.Response;
import com.cnblog.payment.vo.JdPayVO;
import com.ijpay.jdpay.JdPayApi;
import com.ijpay.jdpay.kit.JdPayKit;
import com.ijpay.jdpay.model.UniOrderModel;
import com.ijpay.jdpay.util.RsaUtil;
import com.ijpay.jdpay.util.ThreeDesUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.net.URLEncoder;
import java.util.Base64;
import java.util.Date;
import java.util.Map;

@Service
@Slf4j
public class JdPaymentService extends PaymentService{
    
    @Autowired
    private JdPayConfig jdPayConfig;
    
    @Override
    public Response<JdPayVO> pay(Order order) {
        
        UniOrderModel orderModel = UniOrderModel.builder().version(PayConstant.JD_VERSION_2_0)
            .merchant(jdPayConfig.getMchId())
            .tradeNum(order.getOrderNo())
            .orderType(PayConstant.JD_ORDER_TYPE) // 实物订单
            .currency(PayConstant.JD_CURRENCY_CNY)
            .notifyUrl(jdPayConfig.getNotifyUrl())
            .userId(jdPayConfig.getUserId())
            .tradeName(PayConstant.JD_TRADE_NAME)
            .amount(order.getAmount().movePointRight(2).toString())
            .expireTime(PayConstant.JD_ORDER_VALID_TIME)
            .tradeTime(DateUtil.format(new Date(), "yyyyMMddHHmmss"))
            .build();
    
        log.info("京东原生支付请求参数：{}", JSONObject.toJSONString(orderModel));
    
        try {
            String reqXml = orderModel.genReqXml(jdPayConfig.getRsaPrivateKey(), jdPayConfig.getDesKey(), PayConstant.JD_VERSION_2_0, jdPayConfig.getMchId());
    
            // 发送请求
            String response = JdPayApi.uniOrder(reqXml);
            // 解析并响应结果
            return Response.success(handleResponse(response));
            
        } catch (Exception e) {
            e.printStackTrace();
            return Response.fail("京东支付请求失败");
        }
    }
    
    @Override
    public Response<AlipayTradeQueryResponse> query(String orderNo) {
        return null;
    }
    
    @Override
    public Response refund(Order order) {
        return null;
    }
    
    @Override
    public void handleNotify(HttpServletRequest httpServletRequest) throws Exception {
    
    }
    
    
    public JdPayVO handleResponse(String response) throws Exception {
    
        // 解析响应的 xml 数据
        Map<String, String> map = JdPayKit.parseResp(response);
        if (!PayConstant.JD_PAY_STATUS_SUCCESS.equals(map.get("code"))) {
            throw new Exception("desc");
        }
    
        // 解密结果
        Base64.Decoder decoder = Base64.getDecoder();
        String encryptData = new String(decoder.decode(map.get("encrypt")), "UTF-8");
        String decryptedData = ThreeDesUtil.decrypt4HexStr(RsaUtil.decryptBASE64(jdPayConfig.getDesKey()), encryptData);
        
        // 将解密后的数据转换为Map
        Map<String, Object> stringObjectMap = XmlUtil.xmlToMap(decryptedData);
        JdPayVO jdPayVO = JSON.parseObject(JSON.toJSONString(stringObjectMap), JdPayVO.class);
    
        log.info("京东支付解密结果：{}", JSONObject.toJSONString(jdPayVO));

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
