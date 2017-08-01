package com.baodanyun.websocket.node;

import com.baodanyun.websocket.bean.msg.Msg;
import com.baodanyun.websocket.bean.user.AbstractUser;
import com.baodanyun.websocket.enums.MsgStatus;
import com.baodanyun.websocket.exception.BusinessException;
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
import org.jivesoftware.smackx.offline.OfflineMessageManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by liaowuhen on 2017/5/23.
 */
public class AbstarctChatNode implements ChatNode {
    private static final Logger logger = LoggerFactory.getLogger(AbstarctChatNode.class);

    XmppServer xmppServer = SpringContextUtil.getBean("xmppServer", XmppServer.class);
    VcardService vcardService = SpringContextUtil.getBean("vcardService", VcardService.class);

    private Map<String, AbstractTerminal> nodes = new ConcurrentHashMap<>();

    private AbstractUser abstractUser;
    private XMPPConnection xmppConnection;
    private Long lastActiveTime;

    public AbstarctChatNode(AbstractUser abstractUser, Long lastActiveTime) {
        this.abstractUser = abstractUser;
        this.lastActiveTime = lastActiveTime;
    }

    public Long getLastActiveTime() {
        return lastActiveTime;
    }

    public void setLastActiveTime(Long lastActiveTime) {
        this.lastActiveTime = lastActiveTime;
    }

    @Override
    public AbstractUser getAbstractUser() {
        return abstractUser;
    }

    public void setAbstractUser(AbstractUser abstractUser) {
        this.abstractUser = abstractUser;
    }

    @Override
    public void setXmppConnection(XMPPConnection xmppConnection) {
        this.xmppConnection = xmppConnection;
    }

    @Override
    public void addNode(AbstractTerminal node) {
        getNodes().put(node.getId(), node);
    }


    /**
     * 延时五秒关闭
     *
     * @param id
     * @return
     * @throws IOException
     * @throws XMPPException
     * @throws SmackException
     * @throws BusinessException
     */
    @Override
    public AbstractTerminal removeNode(String id) throws IOException, XMPPException, SmackException, BusinessException {
        AbstractTerminal at = getNodes().remove(id);

        try {
            Thread.sleep(1000 * 5);
        } catch (InterruptedException e) {
            logger.error("error", e);
        }

        if (this.getNodes().size() < 1) {
            logger.info("{} no Terminal，xmpp closed", this.getAbstractUser().getId());
            this.logout();
        } else {
            logger.info("node size {} ", getNodes());
        }

        return at;
    }

    @Override
    public AbstractTerminal getNode(String id) {
        return getNodes().get(id);
    }

    @Override
    public void connected(XMPPConnection xmppConnection) {
        logger.info(getAbstractUser().getLoginUsername() + ":connected");
    }

    @Override
    public void authenticated(XMPPConnection xmppConnection, boolean b) {
        logger.info(getAbstractUser().getLoginUsername() + ":authenticated");
        setXmppConnection(xmppConnection);
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
        //logout();
    }

    @Override
    public void connectionClosedOnError(Exception e) {
        logger.error("error", getAbstractUser().getLoginUsername() + ":connectionClosedOnError", e);
        //logout();
    }

    public void xmppClosed() {
        for (AbstractTerminal node : getNodes().values()) {
            try {
                node.offline();
            } catch (Exception e) {
                logger.error("", e);
            }
        }
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
        if (this.isXmppOnline()) {
            return this.isXmppOnline();
        }
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
    public boolean logout() {
        logger.info("[" + this.getAbstractUser().getId() + "]logout ");
        if (null != xmppConnection) {
            if (xmppConnection.isConnected()) {
                ((AbstractXMPPConnection) xmppConnection).disconnect();
            }
        } else {
            logger.info("jid:[" + this.getAbstractUser().getId() + "] xMPPConnection is closed or xMPPConnection is null");
        }

        ChatNodeManager.removeXmppNode(this.getId());
        return true;
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
        return this.isXmppOnline();
    }

    @Override
    public boolean messageCallBack(AbstractUser abstractUser, MsgStatus msgStatus) throws InterruptedException {
        return false;
    }

    /**
     * 终端加入当前节点，并且调用节点的online 方法
     *
     * @param node
     * @throws InterruptedException
     * @throws BusinessException
     */
    @Override
    public void online(AbstractTerminal node) throws InterruptedException, BusinessException {
         addNode(node);
         node.online();
        //pushOfflineMsg();

    }

    public boolean pushOfflineMsg() throws BusinessException {
        //加载离线记录
        logger.info("pushOfflineMsg");
        OfflineMessageManager offlineManager = new OfflineMessageManager(this.xmppConnection);
        try {
            Thread.sleep(1000 * 10);
            List<Message> msgList = offlineManager.getMessages();
            if (!CollectionUtils.isEmpty(msgList)) {
                for (Message message : msgList) {
                    processMessage(message);
                    offlineManager.deleteMessages();
                }
            }
        } catch (Exception e) {
            logger.error("error", "offline msg error");
        }
        return true;
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
        processMessage(message);
    }


    /**
     * 接收消息从xmpp
     *
     * @param message
     */
    private void processMessage(Message message) {
        try {
            logger.info(getAbstractUser().getId() + ":xmpp receive message " + JSONUtil.toJson(message));

            this.setLastActiveTime(System.currentTimeMillis());
            if (null != getNodes() && getNodes().size() > 0) {
                logger.info(this.getAbstractUser().getId() + "getNodes()" + getNodes().size());
                for (AbstractTerminal node : getNodes().values()) {
                    node.receiveFromXmpp(message);
                }
            } else {
                logger.info("joinQueue getNodes() is null");
            }

        } catch (Exception e) {
            logger.error("error", "msgSendControl.sendMsg error", e);
        }
    }

    public void sendMessageTOXmpp(Message xmppMsg) throws SmackException.NotConnectedException {
        xmppConnection.sendStanza(xmppMsg);
        logger.info(this.getAbstractUser().getId() + "xmpp send message:" + JSONUtil.toJson(xmppMsg));

    }

    public Map<String, AbstractTerminal> getNodes() {
        logger.info("{},{}", this.getAbstractUser().getId(), nodes.keySet().toString());
        return nodes;
    }


    /**
     * 同步消息到不同终端
     * @param msg
     * @return
     */
    @Override
    public boolean synchronizationMsg(String id,Msg msg) {
        for (AbstractTerminal node : getNodes().values()) {
            if (id.equals(node.getId())) {
                node.sendMsgToGod(msg);
            } else {
                logger.info("相同客户端忽略");
            }
        }
        return  true;
    }

    @Override
    public boolean sendMsgToGod(AbstractTerminal abstractTerminal, Msg msg) {
        return abstractTerminal.sendMsgToGod(msg);
    }

    @Override
    public void receiveFromGod(AbstractTerminal abstractTerminal, Msg msg) throws InterruptedException, BusinessException, SmackException.NotConnectedException {
        abstractTerminal.receiveFromGod(msg);
    }

    /**
     * 从终端接收消息发送到xmpp
     * @param abstractTerminal
     * @param msg
     * @throws InterruptedException
     * @throws BusinessException
     * @throws SmackException.NotConnectedException
     */
    @Override
    public void receiveFromGod(AbstractTerminal abstractTerminal, String msg) throws InterruptedException, BusinessException, SmackException.NotConnectedException {
        this.setLastActiveTime(System.currentTimeMillis());
        abstractTerminal.receiveFromGod(msg);
    }

    @Override
    public void sendToXmppError(AbstractTerminal abstractTerminal) throws InterruptedException, BusinessException, SmackException.NotConnectedException {
        abstractTerminal.messageCallBack(this.getAbstractUser(), MsgStatus.msgFail);
    }

    @Override
    public void receiveFromXmpp(AbstractTerminal abstractTerminal, Message message) {
        abstractTerminal.receiveFromXmpp(message);
    }

    /**
     * 发送信息到xmpp
     *
     * @param
     * @throws SmackException.NotConnectedException
     */

}
