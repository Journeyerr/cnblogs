package com.cnblog.webcoket.server;

import com.alibaba.fastjson2.JSONObject;
import com.cnblog.webcoket.dto.WebsocketMsgDTO;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.Strings;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * WebSocketServer服务
 * @author AnYuan
 */

@ServerEndpoint(value = "/webSocket/{uid}")
@Component
@Slf4j
public class WebSocketServer {
    
    /**
     * 机器人发言名称
     */
    private static final String SPOKESMAN_ADMIN = "机器人";
    
    /**
     * concurrent包的线程安全Set
     * 用来存放每个客户端对应的Session对象
     */
    private static final ConcurrentHashMap<String, Session> SESSION_POOLS = new ConcurrentHashMap<>();
    
    /**
     * 静态变量，用来记录当前在线连接数。
     * 应该把它设计成线程安全的。
     */
    private static final AtomicInteger ONLINE_NUM = new AtomicInteger();
    
    /**
     * 获取在线用户列表
     * @return List<String>
     */
    private List<String> getOnlineUsers() {
        return new ArrayList<>(SESSION_POOLS.keySet());
    }
    
    /**
     * 用户建立连接成功调用
     * @param session 用户集合
     * @param uid     用户标志
     */
    @OnOpen
    public void onOpen(Session session, @PathParam(value = "uid") String uid) {
        // 将加入连接的用户加入SESSION_POOLS集合
        SESSION_POOLS.put(uid, session);
        // 在线用户+1
        ONLINE_NUM.incrementAndGet();
        sendToAll(new WebsocketMsgDTO(SPOKESMAN_ADMIN, uid + " 加入连接！", getOnlineUsers()));
    }
    
    /**
     * 用户关闭连接时调用
     * @param uid 用户标志
     */
    @OnClose
    public void onClose(@PathParam(value = "uid") String uid) {
        // 将加入连接的用户移除SESSION_POOLS集合
        SESSION_POOLS.remove(uid);
        // 在线用户-1
        ONLINE_NUM.decrementAndGet();
        sendToAll(new WebsocketMsgDTO(SPOKESMAN_ADMIN, uid + " 断开连接！", getOnlineUsers()));
    }
    
    /**
     * 服务端收到客户端信息
     * @param message 客户端发来的string
     * @param uid     uid 用户标志
     */
    @OnMessage
    public void onMessage(String message, @PathParam(value = "uid") String uid) {
        log.info("Client:[{}]， Message: [{}]", uid, message);
        
        // 接收并解析前端消息并加上时间，最后根据是否有接收用户，区别发送所有用户还是单个用户
        WebsocketMsgDTO msgDTO = JSONObject.parseObject(message, WebsocketMsgDTO.class);
        msgDTO.setDateTime(localDateTimeToString());
        
        // 如果有接收用户就发送单个用户
        if (Strings.isNotBlank(msgDTO.getToUId())) {
            sendMsgByUid(msgDTO);
            return;
        }
        // 否则发送所有人
        sendToAll(msgDTO);
    }
    
    /**
     * 给所有人发送消息
     * @param msgDTO msgDTO
     */
    private void sendToAll(WebsocketMsgDTO msgDTO) {
        //构建json消息体
        String content = JSONObject.toJSONString(msgDTO);
        // 遍历发送所有在线用户
        SESSION_POOLS.forEach((k, session) ->  sendMessage(session, content));
    }
    
    /**
     * 给指定用户发送信息
     */
    private void sendMsgByUid(WebsocketMsgDTO msgDTO) {
        sendMessage(SESSION_POOLS.get(msgDTO.getToUId()), JSONObject.toJSONString(msgDTO));
    }
    
    /**
     * 发送消息方法
     * @param session 用户
     * @param content 消息
     */
    private void sendMessage(Session session, String content){
        try {
            if (Objects.nonNull(session)) {
                // 使用Synchronized锁防止多次发送消息
                synchronized (session) {
                    // 发送消息
                    session.getBasicRemote().sendText(content);
                }
            }
        } catch (IOException ioException) {
            log.info("发送消息失败：{}", ioException.getMessage());
            ioException.printStackTrace();
        }
    }
    
    /**
     * 获取当前时间
     * @return String 12:00:00
     */
    private String localDateTimeToString() {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");
        return dateTimeFormatter.format( LocalDateTime.now());
    }
}