package com.baodanyun.websocket.service.impl;

import com.baodanyun.websocket.bean.msg.Msg;
import com.baodanyun.websocket.service.WebSocketService;
import com.baodanyun.websocket.service.XmppServer;
import com.baodanyun.websocket.util.JSONUtil;
import com.baodanyun.websocket.util.XMPPUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Created by liaowuhen on 2016/11/24.
 */
@Service
public class WebSocketServiceImpl implements WebSocketService {
    private static final Map<String, Map<String, WebSocketSession>> WEB_SOCKET_SESSION_MAP = new ConcurrentHashMap<>();
    protected final Logger logger = LoggerFactory.getLogger(this.getClass());
    BlockingQueue<Msg> basket = new LinkedBlockingQueue<>(100);
    /**
     * key 为用户id
     */
    @Autowired
    private XmppServer xmppServer;

    // 生产消息，放入篮子
    public void produce(Msg msg) throws InterruptedException {
        // put方法放入一个苹果，若basket满了，等到basket有位置
        if (null != msg) {
            basket.put(msg);
            // boolean flag = send(msg.getTo(), msg);
            //logger.info("msg---" + JSONUtil.toJson(msg) + "send to webSocket --->:"+flag);
        } else {
            logger.info("msg is null");
        }
    }

    // 消费消息，从篮子中取走
    public Msg consume() throws InterruptedException {
        // take方法取出一个苹果，若basket为空，等到basket有消息为止(获取并移除此队列的头部)
        return basket.take();
    }

    public boolean sendToWebSocket() throws InterruptedException {
        Msg msg = consume();
        boolean flag = send(msg.getTo(), msg);
        logger.info("msg---" + JSONUtil.toJson(msg) + "send to webSocket --->:" + flag);
        return flag;
    }

    @Override
    public void synchronizationMsg(WebSocketSession webSocketSession, Msg msg) throws IOException {
        if (webSocketSession.isOpen()) {
            webSocketSession.sendMessage(new TextMessage(JSONUtil.toJson(msg)));
        }
    }

    public void saveSession(String jid, WebSocketSession webSocketSession) {
        Map<String, WebSocketSession> map = WEB_SOCKET_SESSION_MAP.get(jid);
        if (null == map) {
            map = new ConcurrentHashMap<>();
            WEB_SOCKET_SESSION_MAP.put(jid, map);
        }
        map.put(webSocketSession.getId(), webSocketSession);
    }

    @Override
    public void removeSession(String jid, WebSocketSession webSocketSession) {
        Map<String, WebSocketSession> map = WEB_SOCKET_SESSION_MAP.get(jid);
        if (null != map) {
            map.remove(webSocketSession.getId());
        }
    }

    @Override
    public Map<String, WebSocketSession> getWebSocketSession(String jid) {
        return WEB_SOCKET_SESSION_MAP.get(jid);
    }

    public WebSocketSession getWebSocketSession(String jid, String id) {
        Map<String, WebSocketSession> map = WEB_SOCKET_SESSION_MAP.get(jid);
        if (null != map) {
            return map.get(id);
        }
        return null;
    }

    public boolean isConnected(String jid, String id) {
        WebSocketSession webSocketSession = getWebSocketSession(jid, id);
        return isConnected(jid, webSocketSession);
    }

    @Override
    public boolean hasH5Connected(String jid) {
        Map<String, WebSocketSession> map = getWebSocketSession(jid);
        if (null != map) {
            Set<Map.Entry<String, WebSocketSession>> entries = map.entrySet();
            Iterator<Map.Entry<String, WebSocketSession>> it = entries.iterator();
            while (it.hasNext()) {
                Map.Entry<String, WebSocketSession> mapEntry = it.next();
                String id = mapEntry.getKey();
                if (isConnected(jid, id)) {
                    return true;

                }
            }
        }
        return false;
    }

    public boolean isConnected(String jid, WebSocketSession webSocketSession) {
        if (null != webSocketSession) {
            if (webSocketSession.isOpen()) {
                logger.info("jid:[" + jid + "] session id [" + webSocketSession.getId() + "] is open");
                return true;
            } else {
                removeSession(jid, webSocketSession);
                logger.info("jid:[" + jid + "]  sessionId [" + webSocketSession.getId() + "] session is closed");
                return false;
            }
        } else {
            logger.info("jid:[" + jid + "] session is null");
            return false;
        }
    }


    @Override
    public boolean hasH5Connected(String jid, Long waitTime) {

        try {
            if (null != waitTime) {
                Thread.sleep(waitTime);
            }
        } catch (Exception e) {
            logger.error("error", e.toString());
        }

        return hasH5Connected(jid);
    }

    /**
     * @param jid
     * @return
     * @throws IOException
     * @throws InterruptedException
     */
    private boolean closedAndXmpp(String jid, String id) throws IOException, InterruptedException {
        // 为了防止刷新 需要延迟关闭
        Thread.sleep(1000);

        if (isConnected(jid, id)) {
            logger.info("fresh ignore closed");
            return false;
        } else {
            logger.info("closed xmpp and webSocket");
            boolean f1 = closed(jid, id);
            boolean f2 = xmppServer.closed(jid);

            return f1 && f2;
        }

    }


    /**
     * @param jid
     * @return
     * @throws IOException
     * @throws InterruptedException
     */
    public boolean isCloseded(String jid, String id) throws IOException, InterruptedException {
        // 为了防止刷新 需要延迟关闭
        Thread.sleep(1000);

        if (isConnected(jid, id)) {
            logger.info("fresh ignore closed");
            return false;
        } else {

            return true;
        }

    }

    public boolean closed(String jid, String id) throws IOException {
        WebSocketSession webSocketSession = getWebSocketSession(jid, id);
        if (null != webSocketSession) {
            if (webSocketSession.isOpen()) {
                webSocketSession.close();
            }
            WEB_SOCKET_SESSION_MAP.remove(jid);
            return true;
        } else {
            logger.info("jid:[" + jid + "] session is closed or session is null");
            return false;
        }

    }

    private boolean send(String jid, Msg msg) {
        String content = XMPPUtil.buildJson(msg);
        return send(jid, content);
    }

    private boolean send(String jid, String content) {

        try {
            Map<String, WebSocketSession> map = WEB_SOCKET_SESSION_MAP.get(jid);
            if (null != map) {
                Set<Map.Entry<String, WebSocketSession>> entries = map.entrySet();
                Iterator<Map.Entry<String, WebSocketSession>> it = entries.iterator();
                while (it.hasNext()) {
                    Map.Entry<String, WebSocketSession> mapEntry = it.next();
                    String id = mapEntry.getKey();
                    if (isConnected(jid, id)) {
                        WebSocketSession webSocketSession = getWebSocketSession(jid, id);
                        webSocketSession.sendMessage(new TextMessage(content));

                    }
                }
                return true;
            }
        } catch (Exception e) {
            logger.error("error", "webSocketSession send error", e);
        }
        return false;
    }
}
