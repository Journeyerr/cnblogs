package com.cnblog.payment.dto.res.cmb;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CmbBaseRes {
    
    private String version;
    private String encoding;
    private String returnCode;
    private String respCode;
    private String respMsg;
    
    @JsonProperty("biz_content")
    private String bizContent ;
}
