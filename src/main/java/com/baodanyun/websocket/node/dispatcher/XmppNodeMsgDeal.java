package com.baodanyun.websocket.node.dispatcher;

import com.baodanyun.websocket.bean.msg.Msg;
import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.packet.Message;

/**
 * Created by liaowuhen on 2017/6/16.
 */
public interface XmppNodeMsgDeal {

    /**
     * 同步消息到终端
     *
     * @param msg
     */

    boolean synchronizationMsg(String id,Msg msg);


    /**
     * 发送信息到xmpp
     *
     * @param
     * @throws SmackException.NotConnectedException
     */

    void sendMessageTOXmpp(Message xmppMsg) throws SmackException.NotConnectedException;
}
