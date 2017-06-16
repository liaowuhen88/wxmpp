package com.baodanyun.websocket.node.terminal;

import com.baodanyun.websocket.bean.msg.Msg;
import com.baodanyun.websocket.exception.BusinessException;
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

    boolean sendMsgToGod(Msg msg);

    Message receiveFromGod(Msg msg) throws InterruptedException, BusinessException, SmackException.NotConnectedException;

    Message receiveFromGod(String msg) throws InterruptedException, BusinessException, SmackException.NotConnectedException;

    void receiveFromXmpp(Message message);


}
