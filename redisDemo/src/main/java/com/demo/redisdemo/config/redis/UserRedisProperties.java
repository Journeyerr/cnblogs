package com.demo.redisdemo.config.redis;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author AnYuan
 */

@Data
@Component
@ConfigurationProperties( prefix = "spring.redis-user")
public class UserRedisProperties {

    /**
     * 配置里面的 '-' 转为驼峰即可读取配置
     */

    private String host;
    private String password;
    private int port;
    private int timeout;
    private int database;
    private int maxWait;
    private int maxActive;
    private int maxIdle;
    private int minIdle;
}
