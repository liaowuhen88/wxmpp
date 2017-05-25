package com.baodanyun.websocket.node.sendUtils;

import com.baodanyun.websocket.bean.msg.Msg;
import com.baodanyun.websocket.util.JSONUtil;
import org.apache.log4j.Logger;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

/**
 * Created by liaowuhen on 2017/5/24.
 */
public class SessionSendUtils {
    private static Logger logger = Logger.getLogger(SessionSendUtils.class);

    public static boolean send(WebSocketSession session, Msg sendMsg) {
        boolean flag = false;
        try {
            if (session.isOpen()) {
                String content = JSONUtil.toJson(sendMsg);
                session.sendMessage(new TextMessage(content));
                flag = true;
                logger.info("msg---" + JSONUtil.toJson(content) + "send to webSocket --->:" + flag);
            }
        } catch (Exception e) {
            logger.error("发送失败", e);
        }
        return flag;
    }
}
