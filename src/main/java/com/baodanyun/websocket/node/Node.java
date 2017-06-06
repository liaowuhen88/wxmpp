package com.baodanyun.websocket.node;

import com.baodanyun.websocket.bean.msg.Msg;
import com.baodanyun.websocket.bean.user.AbstractUser;
import com.baodanyun.websocket.exception.BusinessException;
import com.baodanyun.websocket.node.dispatcher.Dispatcher;
import com.baodanyun.websocket.node.xmpp.XmppNode;
import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.packet.Message;

/**
 * Created by liaowuhen on 2017/5/23.
 */
public interface Node extends Dispatcher {

    /**
     * 发送消息到Xmpp
     *
     * @param
     * @return
     * @throws InterruptedException
     * @throws SmackException.NotConnectedException
     * @throws BusinessException
     */

    void sendMessageTOXmpp(Message message) throws InterruptedException, SmackException.NotConnectedException, BusinessException;

    /**
     * 发送消息到用户
     * @param msg
     */

    boolean sendMsgToGod(Msg msg);

    void receiveFromGod(Msg msg) throws InterruptedException, BusinessException, SmackException.NotConnectedException;

    void receiveFromGod(String msg) throws InterruptedException, BusinessException, SmackException.NotConnectedException;

    void receiveFromXmpp(Message message);


    XmppNode getXmppNode();

    void setXmppNode(XmppNode xmppNode);

    AbstractUser getAbstractUser();

    void online() throws InterruptedException, BusinessException;

   /* *//**
     * 获取消息通知器
     *//*
    MsgSendService getMsgSendService();*/

}
