package com.baodanyun.websocket.node.xmpp;

import com.baodanyun.websocket.bean.msg.Msg;
import com.baodanyun.websocket.bean.user.AbstractUser;
import com.baodanyun.websocket.exception.BusinessException;
import com.baodanyun.websocket.node.AbstractNode;
import com.baodanyun.websocket.service.VcardService;
import com.baodanyun.websocket.service.XmppServer;
import com.baodanyun.websocket.util.JSONUtil;
import com.baodanyun.websocket.util.SpringContextUtil;
import org.apache.commons.lang.StringUtils;
import org.jivesoftware.smack.AbstractXMPPConnection;
import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.chat.Chat;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Presence;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by liaowuhen on 2017/5/23.
 */
public class AbstarctChatNode implements ChatNode {
    private static final Logger logger = LoggerFactory.getLogger(AbstarctChatNode.class);

    XmppServer xmppServer = SpringContextUtil.getBean("xmppServer", XmppServer.class);
    VcardService vcardService = SpringContextUtil.getBean("vcardService", VcardService.class);

    private Map<String, AbstractNode> nodes = new ConcurrentHashMap<>();

    private AbstractUser abstractUser;
    private XMPPConnection xmppConnection;


    public AbstarctChatNode(AbstractUser abstractUser) {
        this.abstractUser = abstractUser;
    }

    @Override
    public AbstractUser getAbstractUser() {
        return abstractUser;
    }


    @Override
    public void setXmppConnection(XMPPConnection xmppConnection) {
        this.xmppConnection = xmppConnection;
    }

    @Override
    public void addNode(AbstractNode node) {
        nodes.put(node.getId(), node);
    }

    @Override
    public AbstractNode removeNode(String id) {
        return nodes.remove(id);
    }

    @Override
    public AbstractNode getNode(String id) {
        return nodes.get(id);
    }

    @Override
    public void connected(XMPPConnection xmppConnection) {
        logger.info(getAbstractUser().getLoginUsername() + ":connected");
    }

    @Override
    public void authenticated(XMPPConnection xmppConnection, boolean b) {
        logger.info(getAbstractUser().getLoginUsername() + ":authenticated");
        Presence presence = new Presence(Presence.Type.available);
        try {
            xmppConnection.sendStanza(presence);
        } catch (SmackException.NotConnectedException e) {
            logger.error("error", e);
        }
    }

    @Override
    public void connectionClosed() {
        logger.info(getAbstractUser().getLoginUsername() + ":connectionClosed");
    }

    @Override
    public void connectionClosedOnError(Exception e) {
        logger.error("error", getAbstractUser().getLoginUsername() + ":connectionClosedOnError", e);
    }

    @Override
    public void reconnectionSuccessful() {
        logger.info(getAbstractUser().getLoginUsername() + ":reconnectionSuccessful");
    }

    @Override
    public void reconnectingIn(int i) {
        logger.info(getAbstractUser().getLoginUsername() + ":reconnectingIn [" + i + "]");
    }

    @Override
    public void reconnectionFailed(Exception e) {
        logger.error("error", getAbstractUser().getLoginUsername() + ":reconnectionFailed", e);
    }

    @Override
    public boolean login() throws BusinessException, IOException, XMPPException, SmackException {
        AbstractUser user = getAbstractUser();
        boolean flag;
        try {
            if (StringUtils.isBlank(user.getLoginUsername())) {
                throw new BusinessException("用户名不能为空");
            }

            if (StringUtils.isBlank(user.getPassWord())) {
                throw new BusinessException("密码不能为空");
            }

            if (!StringUtils.isEmpty(user.getId()) && xmppServer.isAuthenticated(user.getId())) {
                return true;
            }
            flag = xmppServer.login(this);

            if (flag) {
                ChatNodeManager.saveXmppNode(this);
            }


        } catch (Exception e) {

            ChatNodeManager.removeXmppNode(user.getId());
            logger.error("error", e);
            throw e;
        }
        return flag;
    }

    @Override
    public void online() throws InterruptedException, BusinessException {

    }

    @Override
    public boolean logout() throws BusinessException, IOException, XMPPException, SmackException {
        logger.info("[" + this.getAbstractUser().getId() + "]logout ");
        if (null != xmppConnection) {
            if (xmppConnection.isConnected()) {
                ((AbstractXMPPConnection) xmppConnection).disconnect();
            }
            return true;
        } else {
            logger.info("jid:[" + this.getAbstractUser().getId() + "] xMPPConnection is closed or xMPPConnection is null");
            return false;
        }
    }

    @Override
    public void initVCard() {
        vcardService.updateBaseVCard(getAbstractUser().getId(), getAbstractUser());
    }

    @Override
    public String getId() {
        return this.getAbstractUser().getId();
    }

    @Override
    public boolean isOnline() {
        if (null != xmppConnection) {
            return xmppConnection.isAuthenticated();
        }
        return false;
    }

    @Override
    public boolean isXmppOnline() {
        if (null != xmppConnection) {
            return xmppConnection.isAuthenticated();
        }
        return false;
    }

    @Override
    public void chatCreated(Chat chat, boolean b) {
        chat.addMessageListener(this);
    }

    @Override
    public void processMessage(Chat chat, Message message) {
        try {
            logger.info(getAbstractUser().getId() + ":xmpp receive message " + JSONUtil.toJson(message));

            if (null != getNodes()) {
                logger.info(this.getAbstractUser().getId() + "getNodes()" + getNodes().size());
                for (AbstractNode node : getNodes().values()) {
                    node.receiveFromXmpp(message);
                }
            } else {
                logger.info("joinQueue getNodes() is null");
            }

        } catch (Exception e) {
            logger.error("error", "msgSendControl.sendMsg error", e);
        }
    }

    @Override
    public void sendMessage(Message xmppMsg) throws SmackException.NotConnectedException {
        logger.info("xmpp send message:" + JSONUtil.toJson(xmppMsg));
        xmppConnection.sendStanza(xmppMsg);
    }

    public Map<String, AbstractNode> getNodes() {
        return nodes;
    }

    public void setNodes(Map<String, AbstractNode> nodes) {
        this.nodes = nodes;
    }

    @Override
    public boolean sendMsgToGod(Msg msg) {
        return false;
    }

    /**
     * 同步消息到不同终端
     * @param msg
     * @return
     */
    @Override
    public boolean synchronizationMsg(String id,Msg msg) {
        for (AbstractNode node : nodes.values()) {
            if (id.equals(node.getId())) {
                node.sendMsgToGod(msg);
            } else {
                logger.info("相同客户端忽略");
            }
        }
        return  true;
    }

    @Override
    public Message receiveFromGod(Msg msg) throws InterruptedException, BusinessException, SmackException.NotConnectedException {
        return null;
    }

    @Override
    public Message receiveFromGod(String msg) throws InterruptedException, BusinessException, SmackException.NotConnectedException {
        return null;
    }

    @Override
    public void receiveFromXmpp(Message message) {

    }
}
