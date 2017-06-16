/*
package com.baodanyun.websocket.core.sender;

import com.baodanyun.websocket.bean.msg.Msg;
import com.baodanyun.websocket.core.handle.ISender;
import com.baodanyun.websocket.util.XMPPUtil;
import org.slf4j.Logger;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.util.concurrent.*;

public class NormalSender implements ISender {
    private static Logger logger = LoggerFactory.getLogger(NormalSender.class);

    private static final ExecutorService SEND_POOL = Executors.newCachedThreadPool();

    private WebSocketSession webSocketSession;

    public NormalSender(WebSocketSession webSocketSession) {
        this.webSocketSession = webSocketSession;
    }

    */
/**
     * 2秒内判定消息是否发送成功
     *
     * @param msg
     * @return
     *//*

    private boolean sendWithFuture(final Msg msg) {
        Future<Boolean> future = SEND_POOL.submit(new SendCallWithFuture(msg));
        try {
            return future.get(2000l, TimeUnit.MILLISECONDS);
        } catch (Exception e) {
            logger.error("error","发送消息失败", e);
        }
        return false;
    }

    private boolean sendWithoutFuture(final Msg msg) {
        SEND_POOL.execute(new SendCallNotWithFuture(msg));
        return true;
    }

    @Override
    public boolean send(Msg msg, Boolean isFuture) {
        if (isFuture) {
            return sendWithFuture(msg);
        } else {
            return sendWithoutFuture(msg);
        }
    }

    */
/**
     * 支持发送的线程
     *//*

    private class SendCallNotWithFuture implements Runnable {
        private Msg msg;

        public SendCallNotWithFuture(Msg msg) {
            this.msg = msg;
        }

        @Override
        public void run() {
            try {
                System.out.println("websessionidbegin:" + webSocketSession.getId());
                if (null != webSocketSession && webSocketSession.isOpen()) {
                    System.out.println("has send:" + webSocketSession.getId());
                    webSocketSession.sendMessage(new TextMessage(XMPPUtil.buildJson(msg)));
                } else {
                    System.out.println("not opened:" + webSocketSession.getId());
                }

            } catch (Exception e) {
                //TODO
                logger.error("error","发送消息失败", e);
            }
        }
    }

    */
/**
     * 支持待发送结果的线程
     *//*

    private class SendCallWithFuture implements Callable<Boolean> {
        private Msg msg;

        public SendCallWithFuture(Msg msg) {
            this.msg = msg;
        }

        @Override
        public Boolean call() throws Exception {
            try {
                if (null != webSocketSession && webSocketSession.isOpen()) {
                    webSocketSession.sendMessage(new TextMessage(XMPPUtil.buildJson(msg)));
                    return true;
                }
            } catch (Exception e) {
                logger.error("error","message send error");
                return false;
            }
            return false;
        }
    }
}*/
