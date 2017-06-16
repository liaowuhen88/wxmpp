package com.baodanyun.websocket.node.dispatcher;

import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.packet.Message;

/**
 * Created by liaowuhen on 2017/6/16.
 */
public interface XmppNodeMsgDeal {
    /**
     * 发送信息到xmpp
     *
     * @param
     * @throws SmackException.NotConnectedException
     */

    void sendMessage(Message xmppMsg) throws SmackException.NotConnectedException;
}
