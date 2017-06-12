package com.baodanyun.websocket.listener.impl;

import com.baodanyun.websocket.event.SendMsgToWeChatEvent;
import com.baodanyun.websocket.listener.EventBusListener;
import com.baodanyun.websocket.service.WechatMsgService;
import com.google.common.eventbus.Subscribe;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by liaowuhen on 2017/5/15.
 */

@Service
public class SendMsgToWeChatListener extends AbstarctEventBusListener<SendMsgToWeChatEvent> implements EventBusListener<SendMsgToWeChatEvent> {
    private static Logger logger = Logger.getLogger(SendMsgToWeChatListener.class);

    @Autowired
    public WechatMsgService wechatMsgService;

    @Override
    @Subscribe
    public boolean processExpiringEvent(final SendMsgToWeChatEvent ve) {
        logger.info(ve);

        executorService.execute(new Runnable() {
                                    @Override
                                    public void run() {
                                        try {
                                            if (null != ve.getWechatMsg()) {
                                                wechatMsgService.insert(ve.getWechatMsg());
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
