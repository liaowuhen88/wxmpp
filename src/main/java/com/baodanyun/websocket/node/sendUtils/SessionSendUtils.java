package com.baodanyun.websocket.node.sendUtils;

import com.baodanyun.websocket.bean.msg.Msg;
import com.baodanyun.websocket.bean.user.AbstractUser;
import com.baodanyun.websocket.enums.TeminalTypeEnum;
import com.baodanyun.websocket.util.Config;
import com.baodanyun.websocket.util.JSONUtil;
import com.baodanyun.websocket.util.MsgSourceUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

/**
 * Created by liaowuhen on 2017/5/24.
 */
public class SessionSendUtils {
    private static Logger logger = LoggerFactory.getLogger(SessionSendUtils.class);

    public static boolean send(AbstractUser abstractUser, WebSocketSession session, Msg sendMsg) {
        boolean flag = false;
        try {
            if (session.isOpen()) {

                //从XMPP中的消息没法设置来源属性，从发消息后设置到了缓存中
                if (sendMsg.getFrom() != null && sendMsg.getTo() != null) {
                    Integer source = MsgSourceUtil.get(sendMsg.getFrom(), sendMsg.getTo());//消息来源
                    if (null != source) {
                        sendMsg.setSource(source);
                        sendMsg.setSourceDesc(TeminalTypeEnum.H5.getDesc(source));
                    }
                }

                String content = JSONUtil.toJson(sendMsg);
                session.sendMessage(new TextMessage(content));
                flag = true;

                if (String.valueOf(sendMsg.getContent()).equals(Config.greetingWord)) {
                    MsgSourceUtil.remove(sendMsg.getTo()); //删除标识
                }
                logger.info("user {} session {} msg ---" + JSONUtil.toJson(content) + "send to webSocket --->:" + flag, abstractUser.getId(), session.getId());
            }
        } catch (Exception e) {
            logger.error("error", "发送失败", e);
        }
        return flag;
    }
}
