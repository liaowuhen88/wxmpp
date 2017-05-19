package com.baodanyun.wxmpp.test;

import com.baodanyun.websocket.util.JSONUtil;
import org.jivesoftware.smack.chat.Chat;
import org.jivesoftware.smack.chat.ChatMessageListener;
import org.jivesoftware.smack.packet.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by liaowuhen on 2016/11/10.
 */

/**
 * 监听消息发送
 */
public class ChatMessageListenerTest implements ChatMessageListener {
    protected static final Logger logger = LoggerFactory.getLogger(ChatMessageListenerTest.class);

    @Override
    public void processMessage(Chat chat, Message message) {
        logger.info("chat--" + chat.toString() + "-------" + JSONUtil.toJson(message) + "收到消息");
    }
}
