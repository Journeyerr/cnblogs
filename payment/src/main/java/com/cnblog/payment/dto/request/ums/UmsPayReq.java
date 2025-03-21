package com.cnblog.payment.dto.request.ums;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UmsPayReq {
    
    private String mid;
    private String tid;
    private String totalAmount;
    private String reqTime;
    private String tranTime;
    private String expireTime;
    private String notifyUrl;
    private String returnUrl;
    private String billNo;
    private String orderDesc;
    private String sign;
    private String signType;
    private String msgType;
    private String msgSrc;
    private String requestTimestamp;
    
}
