package com.baodanyun.websocket.listener.impl;

import com.baodanyun.websocket.event.VisitorReciveMsgEvent;
import com.baodanyun.websocket.listener.EventBusListener;
import com.baodanyun.websocket.listener.VisitorListener;
import com.baodanyun.websocket.service.UserCacheServer;
import com.baodanyun.websocket.util.JSONUtil;
import com.google.common.eventbus.Subscribe;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by liaowuhen on 2017/5/15.
 */

@Service
public class VisitorReciveMsgListener extends AbstarctEventBusListener<VisitorReciveMsgEvent> implements EventBusListener<VisitorReciveMsgEvent> {
    private static Logger logger = LoggerFactory.getLogger(VisitorReciveMsgListener.class);

    @Autowired
    public VisitorListener visitorListener;
    @Autowired
    protected UserCacheServer userCacheServer;

    @Override
    @Subscribe
    public boolean processExpiringEvent(final VisitorReciveMsgEvent ve) {
        logger.info(JSONUtil.toJson(ve));

        executorService.execute(new Runnable() {
                                    @Override
                                    public void run() {
                                        try {
                                            visitorListener.chat(ve.getContent(), ve.getUser(), ve.getCustomer(), ve.getEventNum());
                                        } catch (Exception e) {
                                            logger.error("error", e);
                                        }
                                    }
                                }

        );
        return false;
    }
}
