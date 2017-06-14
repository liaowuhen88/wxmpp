package com.baodanyun.websocket.node.xmpp;

import com.baodanyun.websocket.bean.user.AbstractUser;
import com.baodanyun.websocket.exception.BusinessException;
import com.baodanyun.websocket.node.Node;
import com.baodanyun.websocket.service.VcardService;
import com.baodanyun.websocket.service.XmppServer;
import com.baodanyun.websocket.util.JSONUtil;
import com.baodanyun.websocket.util.SpringContextUtil;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.jivesoftware.smack.AbstractXMPPConnection;
import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.chat.Chat;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Presence;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by liaowuhen on 2017/5/23.
 */
public class AbstarctXmppNode implements XmppNode {
    private static final Logger logger = Logger.getLogger(AbstarctXmppNode.class);

    XmppServer xmppServer = SpringContextUtil.getBean("xmppServer", XmppServer.class);
    VcardService vcardService = SpringContextUtil.getBean("vcardService", VcardService.class);

    private List<Node> nodes = new ArrayList<>();
    private AbstractUser abstractUser;
    private XMPPConnection xmppConnection;

    public AbstarctXmppNode(AbstractUser abstractUser) {
        this.abstractUser = abstractUser;
    }
    @Override
    public AbstractUser getAbstractUser() {
        return abstractUser;
    }

    @Override
    public List<Node> getNodes() {
        logger.info(this.getAbstractUser().getId() + "getNodes[" + nodes.size() + "]");
        return nodes;
    }

    @Override
    public void addNode(Node node) {
        nodes.add(node);
    }

    @Override
    public void removeNode(Node node) throws IOException, XMPPException, SmackException, BusinessException {
        nodes.remove(node);
        try {
            Thread.sleep(2000);
            if (nodes.size() == 0) {
                this.logout();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
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
            logger.error(e);
        }
    }

    @Override
    public void connectionClosed() {
        logger.info(getAbstractUser().getLoginUsername() + ":connectionClosed");
    }

    @Override
    public void connectionClosedOnError(Exception e) {
        logger.error(getAbstractUser().getLoginUsername() + ":connectionClosedOnError", e);
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
        logger.error(getAbstractUser().getLoginUsername() + ":reconnectionFailed", e);
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
                XmppNodeManager.saveXmppNode(this);
            }


        } catch (Exception e) {

            XmppNodeManager.removeXmppNode(user.getId());
            logger.error(e);
            throw e;
        }
        return flag;
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
    public boolean isOnline() {
        if (null != xmppConnection) {
            return xmppConnection.isAuthenticated();
        }
        return false;

    }


    @Override
    public XMPPConnection getXMPPConnection() {
        return xmppConnection;
    }

    @Override
    public void setXmppConnection(XMPPConnection xmppConnection) {
        this.xmppConnection = xmppConnection;
    }

    @Override
    public void chatCreated(Chat chat, boolean b) {
        chat.addMessageListener(this);
    }

    @Override
    public void processMessage(Chat chat, Message message) {
        try {
            logger.info(getAbstractUser().getId() + ":xmpp receive message :" + JSONUtil.toJson(message));

            if (null != getNodes()) {
                logger.info(this.getAbstractUser().getId() + "getNodes()" + getNodes().size());
                for (Node node : getNodes()) {
                    node.receiveFromXmpp(message);
                }
            } else {
                logger.info("joinQueue getNodes() is null");
            }


           /* Msg sendMsg = getMsg(message);
            if (null != sendMsg) {
                // 手机app端发送过来的数据subject 为空
                if (null != getNodes()) {
                    for (Node node : nodes) {
                        node.getMsgSendService().produce(sendMsg);
                    }
                } else {
                    logger.info(getNodes());
                }

                String key = sendMsg.getTo() + "_" + sendMsg.getFrom();
                if (webSocketService.hasH5Connected(key)) {
                    Msg cloneMsg = SerializationUtils.clone(sendMsg);
                    cloneMsg.setTo(key);
                    webSocketService.produce(cloneMsg);
                }
            }*/
        } catch (Exception e) {
            logger.error("msgSendControl.sendMsg error", e);
        }
    }

    @Override
    public void sendMessage(Message xmppMsg) throws SmackException.NotConnectedException {
        logger.info("xmpp send message:" + JSONUtil.toJson(xmppMsg));
        xmppConnection.sendStanza(xmppMsg);
    }

}
