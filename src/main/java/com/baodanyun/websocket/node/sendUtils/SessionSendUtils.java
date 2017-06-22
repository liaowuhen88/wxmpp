package com.baodanyun.websocket.node.sendUtils;

import com.baodanyun.websocket.bean.msg.Msg;
import com.baodanyun.websocket.util.JSONUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

/**
 * Created by liaowuhen on 2017/5/24.
 */
public class SessionSendUtils {
    private static Logger logger = LoggerFactory.getLogger(SessionSendUtils.class);

    public static boolean send(WebSocketSession session, Msg sendMsg) {
        boolean flag = false;
        try {
            if (session.isOpen()) {
                String content = JSONUtil.toJson(sendMsg);
                session.sendMessage(new TextMessage(content));
                flag = true;
                logger.info("session {} msg---" + JSONUtil.toJson(content) + "send to webSocket --->:" + flag, session.getId());
            }
        } catch (Exception e) {
            logger.error("error", "发送失败", e);
        }
        return flag;
    }
}
