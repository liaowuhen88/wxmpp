package com.baodanyun.websocket.node;

import com.alibaba.fastjson.JSON;
import com.baodanyun.websocket.bean.msg.Msg;
import com.baodanyun.websocket.bean.msg.status.StatusMsg;
import com.baodanyun.websocket.bean.user.AbstractUser;
import com.baodanyun.websocket.enums.MsgStatus;
import com.baodanyun.websocket.exception.BusinessException;
import com.baodanyun.websocket.node.sendUtils.SessionSendUtils;
import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.packet.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.socket.WebSocketSession;

/**
 * Created by liaowuhen on 2017/5/23.
 */
public class WebSocketCustomerTerminal extends CustomerTerminal {
    private static final Logger logger = LoggerFactory.getLogger(WebSocketCustomerTerminal.class);
    private WebSocketSession session;

    WebSocketCustomerTerminal(ChatNodeAdaptation chatNodeAdaptation, WebSocketSession session, String id) {
        super(chatNodeAdaptation);
        this.session = session;
        this.id = id;
    }

    public WebSocketSession getSession() {
        return session;
    }

    public void setSession(WebSocketSession session) {
        this.session = session;
    }

    @Override
    public boolean sendMsgToGod(Msg msg) {
        SessionSendUtils.send(this.getAbstractUser(), getSession(), msg);
        return true;
    }

    @Override
    public boolean joinQueue(AbstractUser abstractUser) {
        Msg online = getSMMsgOnlineSendTOCustomer(abstractUser, MsgStatus.onlineQueueSuccess);
        Msg hello = getMsgHelloToCustomer(abstractUser);

        SessionSendUtils.send(this.getAbstractUser(), getSession(), online);
        //SessionSendUtils.send(this.getAbstractUser(), getSession(), hello);//websokect推送客服自己一条仅为知晓(8月16不再显示)

        return true;
    }

    @Override
    public boolean uninstall(AbstractUser abstractUser) {
        Msg online = getSMMsgOnlineSendTOCustomer(abstractUser, MsgStatus.offline);

        SessionSendUtils.send(this.getAbstractUser(), getSession(), online);

        return true;
    }

    @Override
    public boolean visitorOffline(AbstractUser abstractUser) {
        Msg online = getSMMsgOnlineSendTOCustomer(abstractUser, MsgStatus.offline);

        SessionSendUtils.send(this.getAbstractUser(), getSession(), online);

        return true;
    }

    @Override
    public boolean messageCallBack(AbstractUser abstractUser, MsgStatus msgStatus) throws InterruptedException {
        StatusMsg msg = getSMMsgSendTOCustomer(msgStatus);
        SessionSendUtils.send(this.getAbstractUser(), getSession(), msg);
        return false;
    }

    @Override
    public void online() throws InterruptedException, BusinessException {
        super.online();
        StatusMsg msg = getSMMsgSendTOCustomer(MsgStatus.loginSuccess);
        StatusMsg initSuccess = getSMMsgSendTOCustomer(MsgStatus.initSuccess);

        SessionSendUtils.send(this.getAbstractUser(), getSession(), msg);
        SessionSendUtils.send(this.getAbstractUser(), getSession(), initSuccess);

    }

    @Override
    void offline() throws InterruptedException, BusinessException {
        super.offline();
        StatusMsg offline = getSMMsgSendTOCustomer(MsgStatus.offline);

        SessionSendUtils.send(this.getAbstractUser(), getSession(), offline);
    }
}
