package com.baodanyun.websocket.node;

import com.baodanyun.websocket.bean.msg.Msg;
import com.baodanyun.websocket.exception.BusinessException;
import com.baodanyun.websocket.node.xmpp.XmppNode;
import com.baodanyun.websocket.service.UserCacheServer;
import com.baodanyun.websocket.util.SpringContextUtil;
import com.baodanyun.websocket.util.XMPPUtil;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smackx.offline.OfflineMessageManager;
import org.springframework.util.CollectionUtils;

import java.io.IOException;
import java.util.Date;
import java.util.List;

/**
 * Created by liaowuhen on 2017/5/23.
 */
public abstract class AbstractNode implements Node {
    private static final Logger logger = Logger.getLogger(AbstractNode.class);

    UserCacheServer userCacheServer = SpringContextUtil.getBean("userCacheServerImpl", UserCacheServer.class);

    private XmppNode xmppNode;

    @Override
    public XmppNode getXmppNode() {
        return xmppNode;
    }

    @Override
    public void setXmppNode(XmppNode xmppNode) {
        this.xmppNode = xmppNode;
    }

    public boolean pushOfflineMsg() throws BusinessException {
        //加载离线记录
        XMPPConnection xmppConnection = this.getXmppNode().getXMPPConnection();
        OfflineMessageManager offlineManager = new OfflineMessageManager(xmppConnection);
        try {
            List<Message> msgList = offlineManager.getMessages();
            if (!CollectionUtils.isEmpty(msgList)) {
                for (Message message : msgList) {
                    receiveFromXmpp(message);
                    offlineManager.deleteMessages();
                }
            }
        } catch (Exception e) {
            logger.error("offline msg error");
        }

        return true;
    }

    @Override
    public void sendMessageTOXmpp(Message message) throws InterruptedException, SmackException.NotConnectedException, BusinessException {
        this.getXmppNode().sendMessage(message);
    }

    @Override
    public void receiveFromGod(String content) throws InterruptedException, BusinessException, SmackException.NotConnectedException {
        if (!StringUtils.isEmpty(content)) {
            Msg msg = Msg.handelMsg(content);
            if (msg != null) {
                receiveFromGod(msg);
            }
        } else {
            logger.error("msg is blank");
        }


    }

    @Override
    public void receiveFromGod(Msg msg) throws InterruptedException, BusinessException, SmackException.NotConnectedException {
        Message xmppMsg = new Message();

        xmppMsg.setFrom(msg.getFrom());
        xmppMsg.setTo(msg.getTo());
        xmppMsg.setType(Message.Type.chat);
        xmppMsg.setBody(msg.getContent().toString());
        xmppMsg.setSubject(msg.getContentType());
        sendMessageTOXmpp(xmppMsg);
    }

    @Override
    public void receiveFromXmpp(Message message) {
        Msg sendMsg = null;
        String body = message.getBody();
        if (StringUtils.isNotBlank(body)) {
            sendMsg = new Msg(body);
            String from = XMPPUtil.removeSource(message.getFrom());
            String to = XMPPUtil.removeSource(message.getTo());
            String type = Msg.Type.msg.toString();
            Long ct = new Date().getTime();

            sendMsg.setType(type);
            sendMsg.setFrom(from);
            sendMsg.setTo(to);
            sendMsg.setCt(ct);
            sendMsg.setOpenId(this.getAbstractUser().getOpenId());

            if (StringUtils.isEmpty(message.getSubject())) {
                sendMsg.setContentType("text");
            } else {
                sendMsg.setContentType(message.getSubject());
            }
        }

        sendMsgToGod(sendMsg);
    }


    @Override
    public boolean logout() throws BusinessException, IOException, XMPPException, SmackException {
        return getXmppNode().logout();
    }

    @Override
    public boolean login() throws BusinessException, IOException, XMPPException, SmackException {
        return getXmppNode().login();
    }



}
