package com.baodanyun.websocket.node;

import com.baodanyun.websocket.bean.msg.Msg;
import com.baodanyun.websocket.bean.msg.status.StatusMsg;
import com.baodanyun.websocket.bean.user.AbstractUser;
import com.baodanyun.websocket.bean.user.Customer;
import com.baodanyun.websocket.enums.MsgStatus;
import com.baodanyun.websocket.exception.BusinessException;
import com.baodanyun.websocket.node.sendUtils.SessionSendUtils;
import com.baodanyun.websocket.util.JSONUtil;
import com.baodanyun.websocket.util.XMPPUtil;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.packet.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.socket.WebSocketSession;

/**
 * Created by liaowuhen on 2017/5/23.
 */
public class AccessCustomerTerminal extends CustomerTerminal {
    private static final Logger logger = LoggerFactory.getLogger(AccessCustomerTerminal.class);

    private WebSocketSession session;

    AccessCustomerTerminal(ChatNodeAdaptation chatNodeAdaptation, WebSocketSession session, String id) {
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
    public void receiveFromGod(Msg msg) throws InterruptedException, BusinessException, SmackException.NotConnectedException {
        super.receiveFromGod(msg);
       /* Msg clone = (Msg) SerializationUtils.clone(msg);
        clone.setIcon(null);
        SynchronizationMsgEvent sme = new SynchronizationMsgEvent(clone, this);

        EventBusUtils.post(sme);*/
    }

    @Override
    void sendMessageTOXmpp(Message xmppMsg) throws SmackException.NotConnectedException {
        if (!StringUtils.isEmpty(xmppMsg.getTo())) {
            String realTo = XMPPUtil.jidToName(xmppMsg.getTo());
            if (NumberUtils.isNumber(realTo)) {
                if (this.getAbstractUser() instanceof Customer) {
                    logger.info(JSONUtil.toJson(this.getAbstractUser()));
                    xmppMsg.setTo(((Customer) this.getAbstractUser()).getTo());
                    logger.info(" change {} to {} ", realTo, ((Customer) this.getAbstractUser()).getTo());
                }
            }
        }
        super.sendMessageTOXmpp(xmppMsg);
    }

    @Override
    public boolean sendMsgToGod(Msg msg) {
        String to = ((Customer) getAbstractUser()).getTo();

        if (msg.getFrom().equals(to)) {
            return SessionSendUtils.send(this.getAbstractUser(), getSession(), msg);
        } else {
            logger.info("AccessCustomerTerminal[" + to + "]  --------- msg.getTo() [" + msg.getFrom() + "]");
        }

        return true;
    }

    @Override
    public boolean messageCallBack(AbstractUser abstractUser, MsgStatus msgStatus) throws InterruptedException {
        StatusMsg msg = getSMMsgSendTOCustomer(msgStatus);
        SessionSendUtils.send(this.getAbstractUser(), getSession(), msg);
        return false;
    }

    @Override
    public boolean joinQueue(AbstractUser abstractUser) {
        return false;
    }

    @Override
    public boolean uninstall(AbstractUser abstractUser) {
        return false;
    }

    @Override
    public boolean visitorOffline(AbstractUser abstractUser) {
        return false;
    }
}
