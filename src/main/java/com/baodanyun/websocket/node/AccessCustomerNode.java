package com.baodanyun.websocket.node;

import com.baodanyun.websocket.bean.msg.Msg;
import com.baodanyun.websocket.bean.user.AbstractUser;
import com.baodanyun.websocket.bean.user.AcsessCustomer;
import com.baodanyun.websocket.bean.user.Customer;
import com.baodanyun.websocket.exception.BusinessException;
import com.baodanyun.websocket.node.sendUtils.SessionSendUtils;
import com.baodanyun.websocket.service.UserServer;
import com.baodanyun.websocket.util.SpringContextUtil;
import com.baodanyun.websocket.util.XMPPUtil;
import org.apache.log4j.Logger;
import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.packet.Message;
import org.springframework.web.socket.WebSocketSession;

/**
 * Created by liaowuhen on 2017/5/23.
 */
public class AccessCustomerNode extends CustomerNode {
    private static final Logger logger = Logger.getLogger(AccessCustomerNode.class);

    UserServer userServer = SpringContextUtil.getBean("userServerImpl", UserServer.class);
    private WebSocketSession session;

    public AccessCustomerNode(Customer customer) {
        super(customer);
    }

    public WebSocketSession getSession() {
        return session;
    }

    public void setSession(WebSocketSession session) {
        this.session = session;
    }

    @Override
    public void sendMessageTOXmpp(Message message) throws InterruptedException, SmackException.NotConnectedException, BusinessException {

        try {
            String to = XMPPUtil.jidToName(message.getTo());
            AbstractUser visitor = userServer.initVisitorByUid(Long.valueOf(to));
            if (null != visitor) {
                message.setTo(visitor.getId());
            }
        } catch (BusinessException e) {
            logger.error("获取用户失败", e);
        }

        this.getXmppNode().sendMessage(message);
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
    public boolean uninstall(AbstractUser abstractUser) throws InterruptedException {
        return false;
    }

    @Override
    public void online() throws InterruptedException {

    }


   /* @Override
    public Msg getMsg(String content) {
        if (!StringUtils.isEmpty(content)) {
            Msg msg = Msg.handelMsg(content);
            if (msg != null) {
                if (StringUtils.isEmpty(msg.getFrom())) {
                    logger.error("handleSendMsg from is blank");
                } else {
                    if (StringUtils.isEmpty(msg.getTo())) {
                        logger.error("handleSendMsg to is blank");
                    } else {
                        String to = XMPPUtil.jidToName(msg.getTo());

                        // TODO
                        AbstractUser visitor = null;
                        try {
                            visitor = userServer.InitByUidOrNameOrPhone(to);
                        } catch (BusinessException e) {
                            logger.error("获取用户失败", e);
                        }

                        if (null != visitor) {
                            msg.setTo(visitor.getId());
                        }
                        return msg;
                    }
                }
            }

        } else {
            logger.error("msg is blank");
        }
        return null;
    }*/
}
