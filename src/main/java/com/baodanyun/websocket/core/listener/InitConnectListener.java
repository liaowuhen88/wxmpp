package com.baodanyun.websocket.core.listener;

import com.baodanyun.websocket.bean.user.AbstractUser;
import org.jivesoftware.smack.ConnectionListener;
import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.packet.Presence;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 处理xmpp接收到的消息，并且转换成webSocket消息发给自己
 * 离线消息只包括文本，图片类型
 */
public class InitConnectListener implements ConnectionListener {

    private static Logger logger = LoggerFactory.getLogger(InitConnectListener.class);

    private AbstractUser user;

    public InitConnectListener(AbstractUser user) {
        this.user = user;
    }

    @Override
    public void connected(XMPPConnection xmppConnection) {
        logger.info(user.getLoginUsername() + ":connected");
    }

    @Override
    public void authenticated(XMPPConnection xmppConnection, boolean b) {
        logger.info(user.getLoginUsername() + ":authenticated");
        Presence presence = new Presence(Presence.Type.available);
        try {
            xmppConnection.sendStanza(presence);
        } catch (SmackException.NotConnectedException e) {
            logger.error("error", e);
        }
    }

    @Override
    public void connectionClosed() {
        logger.info(user.getLoginUsername() + ":connectionClosed");
    }

    @Override
    public void connectionClosedOnError(Exception e) {
        logger.error("error", user.getLoginUsername() + ":connectionClosedOnError", e);
    }

    @Override
    public void reconnectionSuccessful() {
        logger.warn(user.getLoginUsername() + "reconnectionSuccessful");
    }

    @Override
    public void reconnectingIn(int i) {
        logger.warn(user.getLoginUsername() + "reconnectingIn:" + i);
    }

    @Override
    public void reconnectionFailed(Exception e) {
        logger.error("error", user.getLoginUsername() + "reconnectionFailed", e);

    }
}