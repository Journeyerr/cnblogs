package com.cnblog.redisdemo.redis;


import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;

import java.util.concurrent.TimeUnit;

@SpringBootTest
public class RedisTest {

    /**
     * 当一个接口有多个实现类的时候
     * 将 @Qualifier 注解与我们想要使用的 Spring Bean 名称一起进行装配
     * Spring 框架就能从多个相同类型并满足装配要求的 Bean 中找到我们想要的
     *
     * 这里绑定初始化里面注入的Bean：redisTemplateUser
     */

    /**
     * 第二个数据源的配置并指定String数据类型 UserRedis
     */
    @Qualifier("redisTemplateUser")
    @Autowired
    private RedisTemplate<String, String> redisTemplateUser;

    /**
     * 默认数据源配置并指定String数据类型的Redis
     */
    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    /**
     * 默认数据源配置的String数据类型的Redis
     */
    @Autowired
    private StringRedisTemplate stringRedisTemplate;


    @Test
    public void redisTest() {
        System.out.println("----------Start----------");

        // 端口6380的Redis
        redisTemplateUser.opsForValue().set("redisTemplateUser", "success", 1, TimeUnit.DAYS);

        // 端口6379默认Redis
        redisTemplate.opsForValue().set("redisTemplate", "success", 1, TimeUnit.DAYS);

        // 端口6379并指定String数据类型的默认Redis
        stringRedisTemplate.opsForValue().set("stringRedisTemplate", "success", 1, TimeUnit.DAYS);

        System.out.println("----------End----------");

    }
}
