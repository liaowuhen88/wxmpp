package com.baodanyun.websocket.node;

import com.baodanyun.websocket.bean.user.AbstractUser;
import com.baodanyun.websocket.enums.AlarmEnum;
import com.baodanyun.websocket.enums.MsgStatus;
import com.baodanyun.websocket.event.AlarmEvent;
import com.baodanyun.websocket.exception.BusinessException;
import com.baodanyun.websocket.model.ConversationCustomer;
import com.baodanyun.websocket.node.dispatcher.CustomerDispather;
import com.baodanyun.websocket.service.ConversationCustomerService;
import com.baodanyun.websocket.service.CustomerDispatcherTactics;
import com.baodanyun.websocket.service.XmppUserOnlineServer;
import com.baodanyun.websocket.util.EventBusUtils;
import com.baodanyun.websocket.util.JSONUtil;
import com.baodanyun.websocket.util.SpringContextUtil;
import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.chat.Chat;
import org.jivesoftware.smack.packet.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by liaowuhen on 2017/5/23.
 */
public class CustomerChatNode extends AbstarctChatNode implements CustomerDispather {

    private static final Logger logger = LoggerFactory.getLogger(CustomerChatNode.class);

    // 加入到客服的访客列表
    private static final Map<String, VisitorChatNode> VISITOR_CHAT_NODE_MAP = new ConcurrentHashMap<>();
    ConversationCustomerService conversationCustomerService = SpringContextUtil.getBean("conversationCustomerServiceImpl", ConversationCustomerService.class);
    CustomerDispatcherTactics customerDispatcherTactics = SpringContextUtil.getBean("customerDispatcherTacticsImpl", CustomerDispatcherTactics.class);
    XmppUserOnlineServer xmppUserOnlineServer = SpringContextUtil.getBean("xmppUserOnlineServer", XmppUserOnlineServer.class);

    public CustomerChatNode(AbstractUser customer, Long lastActiveTime) {
        super(customer, lastActiveTime);
    }


    /**
     * 当客服所有节点移除，默认两秒之后并且没有新的节点加入时，关闭客服节点，确保消息不丢失
     *
     * @param
     * @return
     * @throws IOException
     * @throws XMPPException
     * @throws SmackException
     * @throws BusinessException
     */



    @Override
    public boolean logout() {

        customerDispatcherTactics.deleteCustomer(this.getAbstractUser().getId());

        // 通知访客上线
        for (VisitorChatNode visitorChatNode : VISITOR_CHAT_NODE_MAP.values()) {
            visitorChatNode.customerOffline();
        }

        return super.logout();
    }

    public boolean joinQueue(VisitorChatNode visitorChatNode) {
        ConversationCustomer cc = new ConversationCustomer();
        cc.setCjid(this.getAbstractUser().getId());
        cc.setVjid(visitorChatNode.getAbstractUser().getId());
        cc.setVisitor(JSONUtil.toJson(visitorChatNode.getAbstractUser()));
        conversationCustomerService.insert(cc);

        VISITOR_CHAT_NODE_MAP.put(visitorChatNode.getId(), visitorChatNode);

        if (null != getNodes()) {
            for (AbstractTerminal node : getNodes().values()) {
                try {
                    ((CustomerTerminal) node).joinQueue(visitorChatNode.getAbstractUser());
                } catch (Exception e) {
                    logger.error("error", e);
                }
            }
        } else {
            logger.info("joinQueue getNodes() is null");
        }

        visitorChatNode.joinQueue();
        return true;
    }


    public boolean uninstall(VisitorChatNode visitorChatNode) {
        ConversationCustomer cc = new ConversationCustomer();
        cc.setCjid(this.getAbstractUser().getId());
        cc.setVjid(visitorChatNode.getAbstractUser().getId());
        conversationCustomerService.delete(cc);
        VISITOR_CHAT_NODE_MAP.put(visitorChatNode.getId(), visitorChatNode);
        if (null != getNodes()) {
            for (AbstractTerminal node : getNodes().values()) {
                try {
                    ((CustomerTerminal) node).uninstall(visitorChatNode.getAbstractUser());
                } catch (Exception e) {
                    logger.error("error", e);
                }
            }
        } else {
            logger.info("joinQueue getNodes() is null");
        }

        return true;
    }


    public boolean visitorOffline(VisitorChatNode visitorChatNode) {
        if (null != getNodes()) {
            for (AbstractTerminal node : getNodes().values()) {
                try {
                    ((CustomerTerminal) node).visitorOffline(visitorChatNode.getAbstractUser());
                } catch (Exception e) {
                    logger.error("error", e);
                }
            }
        } else {
            logger.info("joinQueue getNodes() is null");
        }

        return true;
    }

    @Override
    public boolean messageCallBack(AbstractUser abstractUser, MsgStatus msgStatus) throws InterruptedException {
        if (null != getNodes()) {
            for (AbstractTerminal node : getNodes().values()) {
                try {
                    node.messageCallBack(abstractUser, msgStatus);
                } catch (InterruptedException e) {
                    logger.error("error", e);
                }
            }
        } else {
            logger.info("joinQueue getNodes() is null");
        }

        return true;
    }

    /**
     * 登录，并且保存所有客服，以及接入用户客服
     * @return
     * @throws BusinessException
     * @throws IOException
     * @throws XMPPException
     * @throws SmackException
     */
    @Override
    public boolean login() throws BusinessException, IOException, XMPPException, SmackException {
        // 是否为接入用户客服
        boolean flag = super.login();
        if (flag) {
            customerDispatcherTactics.saveCustomer(this.getAbstractUser());
        }
        return flag;
    }

    @Override
    public void online(AbstractTerminal node) throws InterruptedException, BusinessException {
        super.online(node);
        // 通知访客上线
        for (VisitorChatNode visitorChatNode : VISITOR_CHAT_NODE_MAP.values()) {
            visitorChatNode.customerOnline();
        }
    }

    public boolean xmppOnlineServer() throws InterruptedException, BusinessException {
        boolean cFlag = xmppServer.isAuthenticated(this.getAbstractUser().getId());
        if (!cFlag) {
            cFlag = xmppUserOnlineServer.isOnline(this.getAbstractUser().getLoginUsername());
        }
        return cFlag;
    }

    @Override
    public void processMessage(Chat chat, Message message) {
        super.processMessage(chat, message);

        AlarmEvent alarmEvent = new AlarmEvent(AlarmEnum.VISITOR, message); //告警
        EventBusUtils.post(alarmEvent);

    }
}
