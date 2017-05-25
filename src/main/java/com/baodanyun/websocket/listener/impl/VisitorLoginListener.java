package com.baodanyun.websocket.listener.impl;

import com.baodanyun.websocket.event.VisitorLoginEvent;
import com.baodanyun.websocket.listener.EventBusListener;
import com.baodanyun.websocket.listener.VisitorListener;
import com.baodanyun.websocket.service.UserCacheServer;
import com.baodanyun.websocket.util.JSONUtil;
import com.google.common.eventbus.Subscribe;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by liaowuhen on 2017/5/15.
 */

@Service
public class VisitorLoginListener extends AbstarctEventBusListener<VisitorLoginEvent> implements EventBusListener<VisitorLoginEvent> {
    private static Logger logger = Logger.getLogger(VisitorLoginListener.class);

    @Autowired
    public VisitorListener visitorListener;
    @Autowired
    protected UserCacheServer userCacheServer;

    @Override
    @Subscribe
    public boolean processExpiringEvent(final VisitorLoginEvent ve) {
        logger.info(JSONUtil.toJson(ve));

        executorService.execute(new Runnable() {
                                    @Override
                                    public void run() {
                                        try {
                                           /* ve.getMsgSendService().sendSMMsgToVisitor(ve.getUser(), ve.getCustomer(), MsgStatus.loginSuccess);

                                            ve.getMsgSendService().sendSMMsgToVisitor(ve.getUser(), ve.getCustomer(), MsgStatus.initSuccess);*/

                                            visitorListener.login(ve.getUser(), ve.getCustomer());
                                        } catch (Exception e) {
                                            logger.error(e);
                                        }
                                    }
                                }

        );
        return false;
    }
}
