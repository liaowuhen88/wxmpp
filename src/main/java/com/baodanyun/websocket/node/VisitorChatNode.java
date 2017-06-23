package com.baodanyun.websocket.node;

import com.baodanyun.websocket.bean.user.AbstractUser;
import com.baodanyun.websocket.exception.BusinessException;
import com.baodanyun.websocket.service.UserCacheServer;
import com.baodanyun.websocket.util.JSONUtil;
import com.baodanyun.websocket.util.SpringContextUtil;
import org.apache.commons.lang.StringUtils;
import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.chat.Chat;
import org.jivesoftware.smack.packet.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * Created by liaowuhen on 2017/5/23.
 */
public class VisitorChatNode extends AbstarctChatNode {
    private static final Logger logger = LoggerFactory.getLogger(VisitorChatNode.class);
    UserCacheServer userCacheServer = SpringContextUtil.getBean("userCacheServerImpl", UserCacheServer.class);
    WeChatTerminalVisitorFactory weChatTerminalVisitorFactory = SpringContextUtil.getBean("weChatTerminalVisitorFactory", WeChatTerminalVisitorFactory.class);
    private CustomerChatNode currentChatNode;

    VisitorChatNode(AbstractUser visitor, Long lastActiveTime) {
        super(visitor, lastActiveTime);
    }

    public CustomerChatNode getCurrentChatNode() {
        return currentChatNode;
    }

    public void setCurrentChatNode(CustomerChatNode currentChatNode) throws BusinessException {
        this.currentChatNode = currentChatNode;
        if (!StringUtils.isEmpty(this.getAbstractUser().getOpenId())) {
            userCacheServer.addVisitorCustomerOpenId(this.getAbstractUser().getOpenId(), currentChatNode.getAbstractUser().getId());
        } else {
            logger.info("this.getAbstractUser().getOpenId() is null");
        }
    }

    /**
     * 通知当前客服，有用户接入
     *
     * @param node
     * @throws InterruptedException
     * @throws BusinessException
     */
    @Override
    public void online(AbstractTerminal node) throws InterruptedException, BusinessException {
        super.online(node);
        this.getCurrentChatNode().joinQueue(this);
    }


    @Override
    public boolean logout() {
        this.getCurrentChatNode().visitorOffline(this);

        return super.logout();
    }


    @Override
    public void processMessage(Chat chat, Message message) {
        try {
            logger.info(getAbstractUser().getId() + ":xmpp receive message " + JSONUtil.toJson(message));
            if (null != getNodes() && getNodes().size() > 0) {
                for (AbstractTerminal node : getNodes().values()) {
                    node.receiveFromXmpp(message);
                }
            } else {
                String id = weChatTerminalVisitorFactory.getId(this.getAbstractUser());
                AbstractTerminal node = this.getNode(id);
                if (null == node) {
                    ChatNodeAdaptation chatNodeAdaptation = new ChatNodeAdaptation(this);
                    node = weChatTerminalVisitorFactory.getNode(chatNodeAdaptation, this.getAbstractUser());
                    this.addNode(node);
                    node.receiveFromXmpp(message);
                }

                logger.info("joinQueue getNodes() is null");
            }

        } catch (Exception e) {
            logger.error("error", "msgSendControl.sendMsg error", e);
        }
    }

    /**
     *  条件：终端上线后，
     *
     *  更新客服，从旧的客服下线，新的客服上线，
     * @param currentChatNode
     * @return
     * @throws BusinessException
     */
    public ChatNode changeCurrentChatNode(CustomerChatNode currentChatNode) throws BusinessException {
        CustomerChatNode old =  this.currentChatNode;
        setCurrentChatNode(currentChatNode);

        if (null != old && old.getId().equals(currentChatNode.getId())) {
            return currentChatNode;
        }

        if (this.isXmppOnline()) {
            if(null != old){
                if (!old.uninstall(this)) {
                    throw new BusinessException("从当前客服卸载失败");
                }
            }
        }
        logger.info(JSONUtil.toJson(this.getAbstractUser()));


        currentChatNode.joinQueue(this);
        return old;
    }


    @Override
    public boolean login() throws BusinessException, IOException, XMPPException, SmackException {
        boolean flag = super.login();

        initVCard();

        return flag;
    }


    /**
     * 客服上线
     */
    boolean joinQueue() {
        for (AbstractTerminal node : getNodes().values()) {
            if (node instanceof VisitorTerminal) {
                ((VisitorTerminal) node).joinQueue();
            }
        }

        return true;
    }


    /**
     * 客服上线
     */
    boolean customerOnline() throws InterruptedException {
        for (AbstractTerminal node : getNodes().values()) {
            if (node instanceof VisitorTerminal) {
                ((VisitorTerminal) node).customerOnline();
            }
        }

        return true;
    }

    /**
     * 客服上线
     */
    boolean customerOffline() {
        for (AbstractTerminal node : getNodes().values()) {
            if (node instanceof VisitorTerminal) {
                ((VisitorTerminal) node).customerOffline();
            }
        }

        return true;
    }

}
