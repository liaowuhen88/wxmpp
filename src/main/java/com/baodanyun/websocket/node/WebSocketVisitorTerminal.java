package com.baodanyun.websocket.node;

import com.baodanyun.websocket.bean.msg.Msg;
import com.baodanyun.websocket.bean.msg.status.StatusMsg;
import com.baodanyun.websocket.bean.user.AbstractUser;
import com.baodanyun.websocket.enums.MsgStatus;
import com.baodanyun.websocket.exception.BusinessException;
import com.baodanyun.websocket.node.sendUtils.SessionSendUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.socket.WebSocketSession;

/**
 * Created by liaowuhen on 2017/5/23.
 */
public class WebSocketVisitorTerminal extends VisitorTerminal {
    private static final Logger logger = LoggerFactory.getLogger(WeChatTerminal.class);

    private WebSocketSession session;

    WebSocketVisitorTerminal(ChatNodeAdaptation chatNodeAdaptation, WebSocketSession session, String id) {
        super(chatNodeAdaptation);
        this.session = session;
        this.id =id;
    }


    public WebSocketSession getSession() {
        return session;
    }

    public void setSession(WebSocketSession session) {
        this.session = session;
    }

    @Override
    public boolean sendMsgToGod(Msg msg) {
        return SessionSendUtils.send(this.getAbstractUser(), getSession(), msg);
    }

    @Override
    public void online() throws InterruptedException, BusinessException {
        super.online();
        Msg loginSuccess = getSMMsgSendTOVisitor(getAbstractUser(), MsgStatus.loginSuccess);

        Msg initSuccess = getSMMsgSendTOVisitor(getAbstractUser(), MsgStatus.initSuccess);

        SessionSendUtils.send(this.getAbstractUser(), getSession(), loginSuccess);
        SessionSendUtils.send(this.getAbstractUser(), getSession(), initSuccess);

    }

    @Override
    public boolean joinQueue(AbstractUser customer) {

        Msg onlineQueueSuccess = getSMMsgSendTOVisitor(getAbstractUser(), MsgStatus.onlineQueueSuccess);

        Msg hello = getMsgHelloToVisitor(customer);

        SessionSendUtils.send(this.getAbstractUser(), getSession(), onlineQueueSuccess);
        SessionSendUtils.send(this.getAbstractUser(), getSession(), hello);

        return true;
    }

    @Override
    boolean uninstall() throws InterruptedException {
        return false;
    }

    @Override
    boolean customerOnline() {
        Msg customerOnline = getSMMsgSendTOVisitor(getAbstractUser(), MsgStatus.customerOnline);

        SessionSendUtils.send(this.getAbstractUser(), getSession(), customerOnline);

        return true;
    }

    @Override
    boolean customerOffline() {
        Msg customerOnline = getSMMsgSendTOVisitor(getAbstractUser(), MsgStatus.customerOffline);


        SessionSendUtils.send(this.getAbstractUser(), getSession(), customerOnline);

        return false;
    }


    @Override
    void offline() throws InterruptedException, BusinessException {
        super.offline();

        Msg loginError = getSMMsgSendTOVisitor(getAbstractUser(), MsgStatus.offline);

        SessionSendUtils.send(this.getAbstractUser(), getSession(), loginError);
    }

    @Override
    boolean messageCallBack(AbstractUser abstractUser, MsgStatus msgStatus) throws InterruptedException {

        StatusMsg msg = getSMMsgSendTOVisitor(getAbstractUser(), msgStatus);
        SessionSendUtils.send(this.getAbstractUser(), getSession(), msg);
        return false;
    }
}
