package com.baodanyun.websocket.node.terminal;

import com.baodanyun.websocket.bean.msg.Msg;
import com.baodanyun.websocket.exception.BusinessException;
import com.baodanyun.websocket.node.AbstractTerminal;
import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.packet.Message;

/**
 * Created by liaowuhen on 2017/6/16.
 */
public interface TerminalMsgDeal {

    /**
     * 发送消息到用户
     *
     * @param msg
     */

    boolean sendMsgToGod(AbstractTerminal abstractTerminal , Msg msg);

    void receiveFromGod(AbstractTerminal abstractTerminal ,Msg msg) throws InterruptedException, BusinessException, SmackException.NotConnectedException;

    void receiveFromGod(AbstractTerminal abstractTerminal ,String msg) throws InterruptedException, BusinessException, SmackException.NotConnectedException;

    void sendToXmppError(AbstractTerminal abstractTerminal) throws InterruptedException, BusinessException, SmackException.NotConnectedException;


    void receiveFromXmpp(AbstractTerminal abstractTerminal ,Message message);


}
