package com.baodanyun.websocket.listener.impl;

import com.baodanyun.websocket.enums.MsgStatus;
import com.baodanyun.websocket.event.VisitorLogoutEvent;
import com.baodanyun.websocket.listener.EventBusListener;
import com.baodanyun.websocket.listener.VisitorListener;
import com.baodanyun.websocket.service.UserCacheServer;
import com.baodanyun.websocket.service.WebSocketService;
import com.baodanyun.websocket.service.XmppServer;
import com.google.common.eventbus.Subscribe;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by liaowuhen on 2017/5/15.
 */

@Service
public class VisitorLogoutListener extends AbstarctEventBusListener<VisitorLogoutEvent> implements EventBusListener<VisitorLogoutEvent> {
    private static Logger logger = Logger.getLogger(VisitorLogoutListener.class);

    @Autowired
    public WebSocketService webSocketService;
    @Autowired
    protected UserCacheServer userCacheServer;

    @Autowired
    protected XmppServer xmppServer;

    @Autowired
    protected VisitorListener visitorListener;

    @Override
    @Subscribe
    public boolean processExpiringEvent(final VisitorLogoutEvent ve) {
        logger.info(ve);

        executorService.execute(new Runnable() {
                                    @Override
                                    public void run() {
                                        try {
                                            boolean flag = webSocketService.hasH5Connected(ve.getUser().getId(), 1000 * 5L);
                                            if (!flag) {
                                                // 事件中心注册
                                                visitorListener.logOut(ve.getUser(), ve.getCustomer());

                                                // 通知客户端
                                                ve.getMsgSendService().sendSMMsgToCustomer(ve.getUser(), ve.getCustomer(), MsgStatus.offline);

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
