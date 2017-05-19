package com.baodanyun.wxmpp.test;

import org.jivesoftware.smack.ConnectionListener;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.chat.ChatManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by liaowuhen on 2016/11/10.
 */
public class ConnectionListenerTest implements ConnectionListener {
    protected static final Logger logger = LoggerFactory.getLogger(ConnectionListenerTest.class);

    @Override
    public void connected(XMPPConnection xmppConnection) {


        /*ChatManager chatmanager = ChatManager.getInstanceFor(xmppConnection);

        ChatManagerListenerTest ct = new ChatManagerListenerTest();

        chatmanager.addChatListener(ct);*/


        logger.info("connected:" + xmppConnection.getStreamId());
    }

    @Override
    public void authenticated(XMPPConnection xmppConnection, boolean b) {
        logger.info("authenticated:-->" + xmppConnection.getUser());
    }

    @Override
    public void connectionClosed() {
        logger.info("connectionClosed");
    }

    @Override
    public void connectionClosedOnError(Exception e) {
        logger.info("connectionClosedOnError");
    }

    @Override
    public void reconnectionSuccessful() {
        logger.info("reconnectionSuccessful");
    }

    @Override
    public void reconnectingIn(int i) {
        logger.info("reconnectingIn");
    }

    @Override
    public void reconnectionFailed(Exception e) {
        logger.info("reconnectionFailed");
    }
}
