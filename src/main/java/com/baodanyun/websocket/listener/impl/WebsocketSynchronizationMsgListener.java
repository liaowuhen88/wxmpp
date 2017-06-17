package com.baodanyun.websocket.listener.impl;

import com.baodanyun.websocket.event.SynchronizationMsgEvent;
import com.baodanyun.websocket.listener.EventBusListener;
import com.baodanyun.websocket.service.UserCacheServer;
import com.baodanyun.websocket.service.WebSocketService;
import com.google.common.eventbus.Subscribe;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by liaowuhen on 2017/5/15.
 */

@Service
public class WebsocketSynchronizationMsgListener extends AbstarctEventBusListener<SynchronizationMsgEvent> implements EventBusListener<SynchronizationMsgEvent> {
    private static Logger logger = LoggerFactory.getLogger(WebsocketSynchronizationMsgListener.class);
    @Autowired
    private WebSocketService webSocketService;
    @Autowired
    private UserCacheServer userCacheServer;


    @Override
    @Subscribe
    public boolean processExpiringEvent(final SynchronizationMsgEvent sme) {
        logger.info(sme.getMsg().toString());

        executorService.execute(new Runnable() {
                                    @Override
                                    public void run() {
                                        try {
                                            if (null != sme.getMsg()) {

                                                sme.getNode().getChatNodeAdaptation().synchronizationMsg(sme.getNode().getId(),sme.getMsg());
                                            }
                                        } catch (Exception e) {
                                            logger.error("error", e);
                                        }
                                    }
                                }

        );
        return false;
    }
}
