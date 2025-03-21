package com.cnblog.payment.dto.request.cmb;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Created with IntelliJ IDEA.
 *
 * @User: AnYuan
 * @Date: 2025/03/
 * @description 招商聚合支付请求查询参数
 */

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CmbQueryReq {
    
    /**
     * 商户号
     */
    private String merId;
    
    /**
     * 订单号
     */
    private String orderId;
    
}
