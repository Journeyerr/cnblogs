package com.cnblog.seckill.mq;

import com.cnblog.seckill.entity.Order;
import com.cnblog.seckill.repository.OrderMapper;
import com.cnblog.seckill.repository.ProductMapper;
import com.rabbitmq.client.Channel;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class OrderConsumer {
    
    @Autowired
    private ProductMapper productMapper;
    
    @Autowired
    private OrderMapper orderMapper;
    
    @Autowired
    private RedisTemplate<String, String> redisTemplate;
    
    @RabbitListener(queues = "order.queue")
    public void handleOrder(OrderMessage orderMessage, Message message, Channel channel) throws IOException {
        long deliveryTag = message.getMessageProperties().getDeliveryTag();
        try {
            // 扣减数据库库存（乐观锁）
            int affectedRows = productMapper.decrementStock(orderMessage.getSku(), 1);
            if (affectedRows == 0) {
                throw new RuntimeException("库存不足，扣减失败");
            }
            
            // 创建订单
            Order order = new Order();
            order.setOrderId(orderMessage.getOrderId());
            order.setUserId(orderMessage.getUserId());
            order.setSkuCode(orderMessage.getSku());
            order.setStatus("SUCCESS");
            orderMapper.insert(order);
            
            // 成功处理，发送ACK
            channel.basicAck(deliveryTag, false);
        } catch (Exception e) {
            // 回滚Redis库存和用户标记
            String stockKey = "stock:" + orderMessage.getSku();
            redisTemplate.opsForValue().increment(stockKey);
            String userKey = "user_purchased:" + orderMessage.getUserId();
            redisTemplate.delete(userKey);
            
            // 失败处理：拒绝消息并重新入队（可根据业务需求调整）
            channel.basicNack(deliveryTag, false, true);
        }
    }
}
