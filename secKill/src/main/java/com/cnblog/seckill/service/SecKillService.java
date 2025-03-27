package com.cnblog.seckill.service;

import org.springframework.stereotype.Service;

@Service
public interface SecKillService {
    
    String processSecKill(Long userId, String sku);
}
