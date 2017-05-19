package com.baodanyun.websocket.listener.impl;

import com.baodanyun.websocket.enums.MsgStatus;
import com.baodanyun.websocket.event.VisitorJoinEvent;
import com.baodanyun.websocket.listener.EventBusListener;
import com.baodanyun.websocket.listener.VisitorListener;
import com.baodanyun.websocket.service.UserCacheServer;
import com.baodanyun.websocket.util.CommonConfig;
import com.google.common.eventbus.Subscribe;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by liaowuhen on 2017/5/15.
 */

@Service
public class VisitorJoinListener extends AbstarctEventBusListener<VisitorJoinEvent> implements EventBusListener<VisitorJoinEvent> {
    private static Logger logger = Logger.getLogger(VisitorJoinListener.class);

    @Autowired
    public VisitorListener visitorListener;
    @Autowired
    protected UserCacheServer userCacheServer;

    @Override
    @Subscribe
    public boolean processExpiringEvent(final VisitorJoinEvent ve) {
        logger.info(ve);

        executorService.execute(new Runnable() {
                                    @Override
                                    public void run() {
                                        try {

                                            ve.getMsgSendService().sendSMMsgToVisitor(ve.getVisitor(), ve.getCustomer(), MsgStatus.onlineQueueSuccess);
                                            ve.getMsgSendService().sendSMMsgToCustomer(ve.getVisitor(), ve.getCustomer(), MsgStatus.onlineQueueSuccess);
                                            ve.getMsgSendService().sendHelloToCustomer(ve.getVisitor(), ve.getCustomer());
                                            ve.getMsgSendService().sendHelloToVisitor(ve.getVisitor(), ve.getCustomer());

                                            logger.info("保存到缓存--->" + userCacheServer.add(CommonConfig.USER_VISITOR, ve.getVisitor()));

                                            logger.info("保存到缓存--->" + userCacheServer.addCid(CommonConfig.USER_ONLINE, ve.getCustomer().getId(), ve.getVisitor()));


                                        } catch (Exception e) {
                                            logger.error(e);
                                        }
                                    }
                                }

        );
        return false;
    }
}
