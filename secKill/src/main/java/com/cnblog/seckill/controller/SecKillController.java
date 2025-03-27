package com.cnblog.seckill.controller;

import com.cnblog.seckill.dto.Result;
import com.cnblog.seckill.dto.SecKillRequest;
import com.cnblog.seckill.service.SecKillService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/secKill")
public class SecKillController {
    
    @Autowired
    private SecKillService seckillService;
    
    @PostMapping("")
    public Result<String> seckill(@RequestBody SecKillRequest request) {
        String orderId = seckillService.processSecKill(request.getUserId(), request.getSku());
        return Result.success(orderId);
    }
}
