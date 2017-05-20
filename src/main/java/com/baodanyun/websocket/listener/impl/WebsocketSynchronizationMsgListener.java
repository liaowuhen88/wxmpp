package com.baodanyun.websocket.listener.impl;

import com.baodanyun.websocket.bean.user.AbstractUser;
import com.baodanyun.websocket.event.SynchronizationMsgEvent;
import com.baodanyun.websocket.listener.EventBusListener;
import com.baodanyun.websocket.service.UserCacheServer;
import com.baodanyun.websocket.service.WebSocketService;
import com.baodanyun.websocket.util.JSONUtil;
import com.baodanyun.websocket.util.XMPPUtil;
import com.google.common.eventbus.Subscribe;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.WebSocketSession;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 * Created by liaowuhen on 2017/5/15.
 */

@Service
public class WebsocketSynchronizationMsgListener extends AbstarctEventBusListener<SynchronizationMsgEvent> implements EventBusListener<SynchronizationMsgEvent> {
    private static Logger logger = Logger.getLogger(WebsocketSynchronizationMsgListener.class);
    @Autowired
    private WebSocketService webSocketService;
    @Autowired
    private UserCacheServer userCacheServer;


    @Override
    @Subscribe
    public boolean processExpiringEvent(final SynchronizationMsgEvent sme) {
        logger.info(JSONUtil.toJson(sme));

        executorService.execute(new Runnable() {
                                    @Override
                                    public void run() {
                                        try {
                                            if (null != sme.getMsg()) {
                                                Map<String, WebSocketSession> map = webSocketService.getWebSocketSession(sme.getFromJid());
                                                if (null != map && map.size() > 0) {
                                                    String to = XMPPUtil.jidToName(sme.getMsg().getTo());
                                                    AbstractUser visitor = userCacheServer.getVisitorByUidOrOpenID(to);
                                                    if (null != visitor) {
                                                        sme.getMsg().setTo(visitor.getId());
                                                    }
                                                    Set<Map.Entry<String, WebSocketSession>> entries = map.entrySet();
                                                    Iterator<Map.Entry<String, WebSocketSession>> it = entries.iterator();
                                                    while (it.hasNext()) {
                                                        Map.Entry<String, WebSocketSession> mapEntry = it.next();
                                                        String id = mapEntry.getKey();
                                                        WebSocketSession webSocketSession = mapEntry.getValue();

                                                        if (!id.equals(sme.getSessionID())) {
                                                            webSocketService.synchronizationMsg(webSocketSession, sme.getMsg());
                                                        }

                                                    }

                                                }
                                            }
                                        } catch (Exception e) {
                                            logger.error(e);
                                        }
                                    }
                                }

        );
        return false;
    }
}
