package com.cnblog.payment.dto.res.cmb;

import lombok.Data;

@Data
public class CmbOrderRes {
    
    private String merId;
    private String orderId;
    private String cmbOrderId;
    private String qrCode;
    private String txnTime;
    private String tradeState;
    
    /**
     * 请求支付成功示例：
     *
     * {
     *     "version": "0.0.1",
     *     "encoding": "UTF-8",
     *     "returnCode": "SUCCESS",
     *     "respCode": "SUCCESS",
     *     "biz_content": "{\"merId\":\"30899xxxxx\",\"orderId\":\"DEVxxx\",\"cmbOrderId\":\"100xxxxxxxx\",\"qrCode\":\"https://qr.95516.com/xxxxxx\",\"txnTime\":\"20250xxxxxxx\"}"
     * }
     */
    
    /**
     *
     * 请求支付失败示例：
     * {
     *      "version": "0.0.1",
     *      "encoding": "UTF-8",
     *      "returnCode": "SUCCESS",
     *      "respCode": "FAIL",
     *      "errCode": "ORDERID_DUPLICATION",
     *      "respMsg": "商户订单号重复:PCMBDAA",
     *      "biz_content": "{\"merId\":\"30899xxxxx\",\"orderId\":\"DEVxxx\"}"
     * }
     */
    
    /**
     * 请求查询示例:
     * {
     *     "version": "0.0.1",
     *     "encoding": "UTF-8",
     *     "returnCode": "SUCCESS",
     *     "respCode": "SUCCESS",
     *     "biz_content": "{\"merId\":\"30899xxxxx\",\"orderId\":\"DEVxxx\",\"cmbOrderId\":\"100xxxxxxxx\",\"txnAmt\":\"200\",\"currencyCode\":\"156\",\"tradeState\":\"C\",\"txnTime\":\"20250xxxxxxx\"}"
     * }
     */
    
    
}
