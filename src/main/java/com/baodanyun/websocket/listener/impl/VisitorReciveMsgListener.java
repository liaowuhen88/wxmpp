package com.baodanyun.websocket.listener.impl;

import com.baodanyun.websocket.event.VisitorReciveMsgEvent;
import com.baodanyun.websocket.listener.EventBusListener;
import com.baodanyun.websocket.listener.VisitorListener;
import com.baodanyun.websocket.service.UserCacheServer;
import com.google.common.eventbus.Subscribe;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by liaowuhen on 2017/5/15.
 */

@Service
public class VisitorReciveMsgListener extends AbstarctEventBusListener<VisitorReciveMsgEvent> implements EventBusListener<VisitorReciveMsgEvent> {
    private static Logger logger = Logger.getLogger(VisitorReciveMsgListener.class);

    @Autowired
    public VisitorListener visitorListener;
    @Autowired
    protected UserCacheServer userCacheServer;

    @Override
    @Subscribe
    public boolean processExpiringEvent(final VisitorReciveMsgEvent ve) {
        logger.info(ve);

        executorService.execute(new Runnable() {
                                    @Override
                                    public void run() {
                                        try {
                                            visitorListener.chat(ve.getContent(), ve.getUser(), ve.getCustomer());
                                        } catch (Exception e) {
                                            logger.error(e);
                                        }
                                    }
                                }

        );
        return false;
    }
}
