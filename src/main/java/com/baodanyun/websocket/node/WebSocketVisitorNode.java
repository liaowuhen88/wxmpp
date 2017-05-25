package com.baodanyun.websocket.node;

import com.baodanyun.websocket.bean.msg.Msg;
import com.baodanyun.websocket.bean.user.Visitor;
import com.baodanyun.websocket.enums.MsgStatus;
import com.baodanyun.websocket.node.sendUtils.SessionSendUtils;
import org.apache.log4j.Logger;
import org.springframework.web.socket.WebSocketSession;

/**
 * Created by liaowuhen on 2017/5/23.
 */
public class WebSocketVisitorNode extends VisitorNode {
    private static final Logger logger = Logger.getLogger(WeChatNode.class);

    private WebSocketSession session;

    public WebSocketVisitorNode(Visitor visitor) {
        super(visitor);
    }

    public WebSocketSession getSession() {
        return session;
    }

    public void setSession(WebSocketSession session) {
        this.session = session;
    }

    @Override
    public boolean sendMsgToGod(Msg msg) {
        return SessionSendUtils.send(getSession(), msg);
    }

    @Override
    public void online() throws InterruptedException {
        super.online();
        Msg loginSuccess = getSMMsgSendTOVisitor(getAbstractUser(), MsgStatus.loginSuccess);

        Msg initSuccess = getSMMsgSendTOVisitor(getAbstractUser(), MsgStatus.initSuccess);

        SessionSendUtils.send(getSession(), loginSuccess);
        SessionSendUtils.send(getSession(), initSuccess);


        joinQueue();
    }

    @Override
    public boolean joinQueue() throws InterruptedException {
        Msg onlineQueueSuccess = getSMMsgSendTOVisitor(getAbstractUser(), MsgStatus.onlineQueueSuccess);

        Msg hello = getMsgHelloToVisitor(((Visitor) getAbstractUser()));

        SessionSendUtils.send(getSession(), onlineQueueSuccess);
        SessionSendUtils.send(getSession(), hello);

        return super.joinQueue();
    }
}
