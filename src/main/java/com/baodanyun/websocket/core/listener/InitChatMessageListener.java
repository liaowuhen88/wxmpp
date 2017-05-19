package com.baodanyun.websocket.core.listener;

import com.baodanyun.websocket.bean.msg.Msg;
import com.baodanyun.websocket.bean.user.AbstractUser;
import com.baodanyun.websocket.service.MsgSendControl;
import com.baodanyun.websocket.util.JSONUtil;
import org.apache.log4j.Logger;
import org.jivesoftware.smack.chat.Chat;
import org.jivesoftware.smack.chat.ChatMessageListener;
import org.jivesoftware.smack.packet.Message;

/**
 * Created by liaowuhen on 2017/5/11.
 */
public class InitChatMessageListener implements ChatMessageListener {
    private static Logger logger = Logger.getLogger(InitChatMessageListener.class);

    private MsgSendControl msgSendControl;
    private AbstractUser user;

    public InitChatMessageListener(AbstractUser user, MsgSendControl msgSendControl) {
        this.user = user;
        this.msgSendControl = msgSendControl;
    }

    @Override
    public void processMessage(Chat chat, Message msg) {
        try {
            logger.info(user.getId() + ":xmpp receive message :" + JSONUtil.toJson(msg));
            Msg sendMsg = msgSendControl.getMsg(msg, user);
            if (null != sendMsg) {
                // 手机app端发送过来的数据subject 为空
                sendMsg.setOpenId(user.getOpenId());
                msgSendControl.sendMsg(sendMsg);
            }
        } catch (Exception e) {
            logger.error("msgSendControl.sendMsg error", e);
        }
    }
}
