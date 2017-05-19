package com.baodanyun.websocket.service;

import com.baodanyun.websocket.util.JSONUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by liaowuhen on 2017/1/12.
 */

@Service
public class MsgConsumer implements Runnable {
    protected final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    public WebSocketService webSocketService;

    @Override
    public void run() {
        while (true) {
            try {
                 webSocketService.sendToWebSocket();

            } catch (InterruptedException e) {
                logger.error("失败", e);
            }
        }
    }
}
