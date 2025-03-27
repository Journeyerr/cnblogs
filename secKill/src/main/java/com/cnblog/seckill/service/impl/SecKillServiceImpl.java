package com.cnblog.seckill.service.impl;

import com.cnblog.seckill.mq.OrderMessage;
import com.cnblog.seckill.service.SecKillService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.scripting.support.ResourceScriptSource;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.UUID;

@Slf4j
@Service
public class SecKillServiceImpl implements SecKillService {
    
    @Autowired
    private RedisTemplate<String, String> redisTemplate;
    @Autowired
    private RabbitTemplate rabbitTemplate;
    
    private static final DefaultRedisScript<Long> SECKILL_SCRIPT;
    
    static {
        SECKILL_SCRIPT = new DefaultRedisScript<>();
        SECKILL_SCRIPT.setScriptSource(new ResourceScriptSource(new ClassPathResource("seckill.lua")));
        SECKILL_SCRIPT.setResultType(Long.class);
    }
    
    @Override
    public String processSecKill(Long userId, String sku) {
        String stockKey = "stock:" + sku;
        String userKey = "user_purchased:" + userId;
        
        Long result = redisTemplate.execute(
            SECKILL_SCRIPT,
            Arrays.asList(stockKey, userKey),
            userId.toString()
        );
        log.info("result：{}", result);
        if (result == 1) {
            String orderId = UUID.randomUUID().toString();
            rabbitTemplate.convertAndSend("order.exchange", "order.key",
                new OrderMessage(orderId, userId, sku));
            return orderId;
        } else {
            throw new RuntimeException("秒杀失败");
        }
    }
}
