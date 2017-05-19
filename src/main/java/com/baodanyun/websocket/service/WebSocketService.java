package com.baodanyun.websocket.service;

import com.baodanyun.websocket.bean.msg.Msg;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;
import java.util.Map;

/**
 * Created by liaowuhen on 2016/11/24.
 */
@Service
public interface WebSocketService {

    void produce(Msg msg) throws InterruptedException;
    // 消费消息，从篮子中取走
    Msg consume() throws InterruptedException;

    boolean sendToWebSocket() throws InterruptedException;

    void synchronizationMsg(WebSocketSession webSocketSession, Msg msg) throws IOException;

    void saveSession(String jid, WebSocketSession webSocketSession);

    void removeSession(String jid, WebSocketSession webSocketSession);

    Map<String, WebSocketSession> getWebSocketSession(String jid);

    WebSocketSession getWebSocketSession(String jid, String id);

    boolean isConnected(String jid, String id);

    boolean hasH5Connected(String jid, Long waitTime);

    boolean hasH5Connected(String jid);

    //boolean send(String jid, Msg msg);

    //boolean send(String jid, String content) ;
}
