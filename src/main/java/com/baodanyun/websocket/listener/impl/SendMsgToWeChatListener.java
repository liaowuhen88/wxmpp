package com.baodanyun.websocket.listener.impl;

import com.baodanyun.websocket.event.SendMsgToWeChatEvent;
import com.baodanyun.websocket.listener.EventBusListener;
import com.baodanyun.websocket.service.WechatMsgService;
import com.google.common.eventbus.Subscribe;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by liaowuhen on 2017/5/15.
 */

@Service
public class SendMsgToWeChatListener extends AbstarctEventBusListener<SendMsgToWeChatEvent> implements EventBusListener<SendMsgToWeChatEvent> {
    private static Logger logger = LoggerFactory.getLogger(SendMsgToWeChatListener.class);

    @Autowired
    public WechatMsgService wechatMsgService;

    @Override
    @Subscribe
    public boolean processExpiringEvent(final SendMsgToWeChatEvent ve) {
        logger.info(ve.toString());

        executorService.execute(new Runnable() {
                                    @Override
                                    public void run() {
                                        try {
                                            if (null != ve.getWechatMsg()) {
                                                wechatMsgService.insert(ve.getWechatMsg());
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
