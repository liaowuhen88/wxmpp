package com.baodanyun.websocket.core.listener;

import com.baodanyun.websocket.bean.user.AbstractUser;
import com.baodanyun.websocket.service.MsgSendControl;
import org.jivesoftware.smack.chat.Chat;
import org.jivesoftware.smack.chat.ChatManagerListener;
import org.jivesoftware.smack.chat.ChatMessageListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by liaowuhen on 2017/5/11.
 */
public class InitChatManagerListener implements ChatManagerListener {

    private static Logger logger = LoggerFactory.getLogger(InitChatManagerListener.class);

    private MsgSendControl msgSendControl;
    private AbstractUser user;

    public InitChatManagerListener(AbstractUser user, MsgSendControl msgSendControl) {
        this.user = user;
        this.msgSendControl = msgSendControl;
    }

    @Override
    public void chatCreated(Chat chat, boolean b) {
        logger.info(user.getId() + "chatCreated");
        // 添加消息监听
        ChatMessageListener chatMessageListener = new InitChatMessageListener(user, msgSendControl);
        chat.addMessageListener(chatMessageListener);

    }
}
