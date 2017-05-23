package com.baodanyun.websocket.node;

import com.baodanyun.websocket.bean.msg.Msg;
import com.baodanyun.websocket.bean.user.AbstractUser;
import com.baodanyun.websocket.exception.BusinessException;
import com.baodanyun.websocket.node.xmpp.XmppNode;
import com.baodanyun.websocket.service.MsgSendService;
import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.XMPPException;

import java.io.IOException;

/**
 * Created by liaowuhen on 2017/5/23.
 */
public interface Node {

    boolean login() throws IOException, XMPPException, SmackException, BusinessException;

    void logout() throws InterruptedException;

    Msg receiveMessage(String content) throws InterruptedException, SmackException.NotConnectedException, BusinessException;

    Msg receiveMessage(Msg msg) throws InterruptedException, SmackException.NotConnectedException, BusinessException;

    void sendMsg(Msg msgBean);

    boolean joinQueue() throws InterruptedException;

    boolean uninstall() throws InterruptedException;

    boolean isOnline();

    // 上线后的push信息
    void onlinePush() throws BusinessException, InterruptedException;

    XmppNode getXmppNode();

    void setXmppNode(XmppNode xmppNode);

    AbstractUser getAbstractUser();

    /**
     * 获取消息通知器
     */
    MsgSendService getMsgSendService();

}
