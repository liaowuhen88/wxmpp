package com.baodanyun.websocket.node;

import com.baodanyun.websocket.bean.msg.Msg;
import com.baodanyun.websocket.bean.msg.status.StatusMsg;
import com.baodanyun.websocket.bean.user.AbstractUser;
import com.baodanyun.websocket.bean.user.AcsessCustomer;
import com.baodanyun.websocket.enums.MsgStatus;
import com.baodanyun.websocket.event.SynchronizationMsgEvent;
import com.baodanyun.websocket.exception.BusinessException;
import com.baodanyun.websocket.node.sendUtils.SessionSendUtils;
import com.baodanyun.websocket.service.UserServer;
import com.baodanyun.websocket.util.EventBusUtils;
import com.baodanyun.websocket.util.SpringContextUtil;
import org.apache.commons.lang.SerializationUtils;
import org.jivesoftware.smack.SmackException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.socket.WebSocketSession;

/**
 * Created by liaowuhen on 2017/5/23.
 */
public class AccessCustomerTerminal extends CustomerTerminal {
    private static final Logger logger = LoggerFactory.getLogger(AccessCustomerTerminal.class);

    UserServer userServer = SpringContextUtil.getBean("userServerImpl", UserServer.class);
    private WebSocketSession session;

    AccessCustomerTerminal(ChatNodeAdaptation chatNodeAdaptation, AbstractUser customer, WebSocketSession session, String id) {
        super(chatNodeAdaptation, customer);
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
        Msg clone = (Msg) SerializationUtils.clone(msg);
        clone.setIcon(null);
        SynchronizationMsgEvent sme = new SynchronizationMsgEvent(clone, this);

        EventBusUtils.post(sme);
    }

    @Override
    public boolean sendMsgToGod(Msg msg) {
        String to = ((AcsessCustomer) getAbstractUser()).getTo();

        if (msg.getFrom().equals(to)) {
            return SessionSendUtils.send(getSession(), msg);
        } else {
            logger.info("AccessCustomerTerminal[" + to + "]  --------- msg.getTo() [" + msg.getFrom() + "]");
        }

        return true;
    }

    @Override
    public void online() throws InterruptedException {

    }

    @Override
    public boolean messageCallBack(AbstractUser abstractUser, MsgStatus msgStatus) throws InterruptedException {
        StatusMsg msg = getSMMsgSendTOCustomer(msgStatus);
        SessionSendUtils.send(getSession(), msg);
        return false;
    }
}
