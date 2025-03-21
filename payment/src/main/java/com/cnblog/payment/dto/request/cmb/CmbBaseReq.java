package com.cnblog.payment.dto.request.cmb;

import com.cnblog.payment.constant.PayConstant;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CmbBaseReq {
    
    private String version;
    private String encoding;
    private String signMethod;
    
    @JsonProperty("biz_content")
    private String bizContent;
    @JsonProperty("req_url")
    private String reqUrl;
    
    
    public CmbBaseReq(String bizContent, String reqUrl) {
        
        this.version = PayConstant.CMB_VERSION;
        this.encoding = PayConstant.UTF_8;
        this.signMethod = PayConstant.CMB_SIGN_METHOD;
        this.bizContent = bizContent;
        this.reqUrl = reqUrl;
    }
}
