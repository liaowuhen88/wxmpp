package com.baodanyun.websocket.node.xmpp;

import com.baodanyun.websocket.bean.msg.Msg;
import com.baodanyun.websocket.bean.user.AbstractUser;
import com.baodanyun.websocket.exception.BusinessException;
import com.baodanyun.websocket.node.Node;
import com.baodanyun.websocket.service.VcardService;
import com.baodanyun.websocket.service.WebSocketService;
import com.baodanyun.websocket.service.XmppServer;
import com.baodanyun.websocket.util.JSONUtil;
import com.baodanyun.websocket.util.SpringContextUtil;
import com.baodanyun.websocket.util.XMPPUtil;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.SerializationUtils;
import org.apache.log4j.Logger;
import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.chat.Chat;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Presence;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by liaowuhen on 2017/5/23.
 */
public class AbstarctXmppNode implements XmppNode {
    private static final Logger logger = Logger.getLogger(AbstarctXmppNode.class);
    XmppServer xmppServer = SpringContextUtil.getBean("xmppServer", XmppServer.class);
    VcardService vcardService = SpringContextUtil.getBean("vcardService", VcardService.class);
    WebSocketService webSocketService = SpringContextUtil.getBean("webSocketServiceImpl", WebSocketService.class);
    private List<Node> nodes = new ArrayList<>();
    private AbstractUser user;

    public AbstarctXmppNode(AbstractUser user) {
        this.user = user;
    }

    @Override
    public AbstractUser getAbstractUser() {
        return user;
    }

    @Override
    public List<Node> getNodes() {
        return nodes;
    }

    @Override
    public void addNode(Node node) {
        nodes.add(node);
    }

    @Override
    public void initVCard() {

    }


    @Override
    public void connected(XMPPConnection xmppConnection) {

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

    }

    @Override
    public void connectionClosedOnError(Exception e) {

    }

    @Override
    public void reconnectionSuccessful() {

    }

    @Override
    public void reconnectingIn(int i) {

    }

    @Override
    public void reconnectionFailed(Exception e) {

    }

    @Override
    public boolean login() throws BusinessException, IOException, XMPPException, SmackException {
        AbstractUser user = getAbstractUser();
        if (StringUtils.isBlank(user.getLoginUsername())) {
            throw new BusinessException("用户名不能为空");
        }

        if (StringUtils.isBlank(user.getPassWord())) {
            throw new BusinessException("密码不能为空");
        }

        if (!StringUtils.isEmpty(user.getId()) && xmppServer.isAuthenticated(user.getId())) {
            return true;
        }
        return xmppServer.login(this);
    }

    @Override
    public boolean logout() throws BusinessException, IOException, XMPPException, SmackException {
        if (null != getNodes()) {
            for (Node node : getNodes()) {
                try {
                    node.logout();
                } catch (InterruptedException e) {
                    logger.error(e);
                }
            }
        }
        return true;
    }

    @Override
    public boolean isOnline() {
        return xmppServer.isAuthenticated(getAbstractUser().getId());
    }


    @Override
    public XMPPConnection getXMPPConnection() {
        return xmppServer.getXMPPConnection(getAbstractUser().getId());
    }

    @Override
    public void chatCreated(Chat chat, boolean b) {
        chat.addMessageListener(this);
    }

    @Override
    public void processMessage(Chat chat, Message message) {
        try {
            logger.info(getAbstractUser().getId() + ":xmpp receive message :" + JSONUtil.toJson(message));
            Msg sendMsg = getMsg(message);
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
            }
        } catch (Exception e) {
            logger.error("msgSendControl.sendMsg error", e);
        }
    }


    private Msg getMsg(Message msg) {
        Msg sendMsg = null;
        String body = msg.getBody();
        if (StringUtils.isNotBlank(body)) {
            sendMsg = new Msg(body);
            String from = XMPPUtil.removeSource(msg.getFrom());
            String to = XMPPUtil.removeSource(msg.getTo());
            String type = Msg.Type.msg.toString();
            Long ct = new Date().getTime();

            sendMsg.setType(type);
            sendMsg.setFrom(from);
            sendMsg.setTo(to);
            sendMsg.setCt(ct);
            sendMsg.setOpenId(user.getOpenId());

            if (StringUtils.isEmpty(msg.getSubject())) {
                sendMsg.setContentType("text");
            } else {
                sendMsg.setContentType(msg.getSubject());
            }
        }
        return sendMsg;
    }
}
