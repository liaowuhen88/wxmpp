package com.baodanyun.websocket.node;

import com.baodanyun.websocket.bean.msg.Msg;
import com.baodanyun.websocket.bean.msg.status.StatusMsg;
import com.baodanyun.websocket.bean.user.AbstractUser;
import com.baodanyun.websocket.bean.user.AcsessCustomer;
import com.baodanyun.websocket.bean.user.Customer;
import com.baodanyun.websocket.enums.MsgStatus;
import com.baodanyun.websocket.exception.BusinessException;
import com.baodanyun.websocket.node.sendUtils.SessionSendUtils;
import com.baodanyun.websocket.node.xmpp.ChatNodeAdaptation;
import com.baodanyun.websocket.service.UserServer;
import com.baodanyun.websocket.util.SpringContextUtil;
import com.baodanyun.websocket.util.XMPPUtil;
import org.apache.commons.lang.StringUtils;
import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.packet.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.socket.WebSocketSession;

/**
 * Created by liaowuhen on 2017/5/23.
 */
public class AccessCustomerNode extends CustomerNode {
    private static final Logger logger = LoggerFactory.getLogger(AccessCustomerNode.class);

    UserServer userServer = SpringContextUtil.getBean("userServerImpl", UserServer.class);
    private WebSocketSession session;

    public AccessCustomerNode(ChatNodeAdaptation chatNodeAdaptation,AbstractUser customer, WebSocketSession session, String id) {
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
    public boolean isOnline() {
        return false;
    }

    @Override
    public Message receiveFromGod(Msg msg) throws InterruptedException, BusinessException, SmackException.NotConnectedException {
        return super.receiveFromGod(msg);
    }

    @Override
    public Message receiveFromGod(String content) throws InterruptedException, BusinessException, SmackException.NotConnectedException {
        Message message = super.receiveFromGod(content);
        try {
            String to = XMPPUtil.jidToName(message.getTo());
            AbstractUser visitor;
            if (StringUtils.isNumeric(to)) {
                visitor = userServer.initVisitorByUid(Long.parseLong(to));
            } else {
                visitor = userServer.initUserByOpenId(to);
            }

            if (null != visitor) {
                message.setTo(visitor.getId());
            }
        } catch (BusinessException e) {
            logger.error("error", "获取用户失败", e);
        }
        return message;
    }

    @Override
    public boolean sendMsgToGod(Msg msg) {
        String to = ((AcsessCustomer) getAbstractUser()).getTo();

        if (msg.getFrom().equals(to)) {
            return SessionSendUtils.send(getSession(), msg);
        } else {
            logger.info("AccessCustomerNode[" + to + "]  --------- msg.getTo() [" + msg.getFrom() + "]");
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
