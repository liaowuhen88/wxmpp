package com.baodanyun.wxmpp.test;

import org.jivesoftware.smack.chat.Chat;
import org.jivesoftware.smack.chat.ChatManagerListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * Created by liaowuhen on 2016/11/10.
 */
public class ChatManagerListenerTest implements ChatManagerListener {
    protected static final Logger logger = LoggerFactory.getLogger(ChatManagerListenerTest.class);

    @Override
    public void chatCreated(Chat chat, boolean createdLocally) {
        logger.info("chat--" + chat.toString() + ":created");
    }
}
