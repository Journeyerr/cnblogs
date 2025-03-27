package com.cnblog.seckill.controller;

import com.cnblog.seckill.dto.Result;
import com.cnblog.seckill.entity.Order;
import com.cnblog.seckill.repository.OrderMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class OrderController {
    @Autowired
    private OrderMapper orderMapper;
    
    @GetMapping("/order/status/{orderId}")
    public Result<String> getOrderStatus(@PathVariable String orderId) {
        Order order = orderMapper.selectByOrderId(orderId);
        if (order == null) return Result.error("订单不存在");
        return Result.success(order.getStatus());
    }
}
