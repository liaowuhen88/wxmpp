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
public class WebSocketCustomerTerminal extends CustomerTerminal {
    private static final Logger logger = LoggerFactory.getLogger(WebSocketCustomerTerminal.class);
    private WebSocketSession session;

    WebSocketCustomerTerminal(ChatNodeAdaptation chatNodeAdaptation, AbstractUser customer, WebSocketSession session, String id) {
        super(chatNodeAdaptation,customer);
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
        SessionSendUtils.send(getSession(), msg);
        return true;
    }

    @Override
    public boolean joinQueue(AbstractUser abstractUser) throws InterruptedException {

        super.joinQueue(abstractUser);

        Msg hello = getMsgHelloToCustomer(abstractUser);
        Msg online = getSMMsgOnlineSendTOCustomer(abstractUser, MsgStatus.onlineQueueSuccess);

        SessionSendUtils.send(getSession(), online);
        SessionSendUtils.send(getSession(), hello);

        return true;
    }

    @Override
    public boolean messageCallBack(AbstractUser abstractUser, MsgStatus msgStatus) throws InterruptedException {
        StatusMsg msg = getSMMsgSendTOCustomer(msgStatus);
        SessionSendUtils.send(getSession(), msg);
        return false;
    }

    @Override
    public void online() throws InterruptedException, BusinessException {
        super.online();
        StatusMsg msg = getSMMsgSendTOCustomer(MsgStatus.loginSuccess);
        StatusMsg initSuccess = getSMMsgSendTOCustomer(MsgStatus.initSuccess);

        SessionSendUtils.send(getSession(), msg);
        SessionSendUtils.send(getSession(), initSuccess);

    }

    @Override
    void offline() throws InterruptedException, BusinessException {
        super.offline();
        StatusMsg offline = getSMMsgSendTOCustomer(MsgStatus.loginError);

        SessionSendUtils.send(getSession(), offline);
    }
}
