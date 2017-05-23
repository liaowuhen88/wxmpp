package com.baodanyun.websocket.node;

import com.baodanyun.websocket.bean.msg.Msg;
import com.baodanyun.websocket.bean.msg.msg.ImgMsg;
import com.baodanyun.websocket.bean.msg.msg.TextMsg;
import com.baodanyun.websocket.bean.user.AbstractUser;
import com.baodanyun.websocket.exception.BusinessException;
import com.baodanyun.websocket.node.xmpp.XmppNode;
import com.baodanyun.websocket.service.MsgSendService;
import com.baodanyun.websocket.service.UserCacheServer;
import com.baodanyun.websocket.service.XmppServer;
import com.baodanyun.websocket.util.SpringContextUtil;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smackx.offline.OfflineMessageManager;
import org.springframework.util.CollectionUtils;

import java.io.IOException;
import java.util.List;

/**
 * Created by liaowuhen on 2017/5/23.
 */
public abstract class AbstractNode implements Node {
    private static final Logger logger = Logger.getLogger(AbstractNode.class);
    XmppServer xmppServer = SpringContextUtil.getBean("xmppServer", XmppServer.class);
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

    @Override
    public AbstractUser getAbstractUser() {
        return null;
    }

    @Override
    public MsgSendService getMsgSendService() {
        return null;
    }

    public boolean pushOfflineMsg() throws BusinessException {
        //加载离线记录
        XMPPConnection xmppConnection = this.getXmppNode().getXMPPConnection();
        OfflineMessageManager offlineManager = new OfflineMessageManager(xmppConnection);
        try {
            List<Message> msgList = offlineManager.getMessages();
            if (!CollectionUtils.isEmpty(msgList)) {
                for (Message message : msgList) {
                    String body = message.getBody();
                    if (StringUtils.isNotBlank(body)) {
                        Msg msgBean = Msg.handelMsg(body);
                        if (msgBean != null) {
                            //只接收文本,图片类型消息
                            if (msgBean instanceof TextMsg || msgBean instanceof ImgMsg) {
                                getMsgSendService().produce(msgBean);
                                //只有接受到消息后 才删除离线消息
                                offlineManager.deleteMessages();

                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            logger.error("offline msg error");
        }

        return true;
    }

    @Override
    public boolean login() throws IOException, XMPPException, SmackException, BusinessException {
        return false;
    }

    @Override
    public void logout() throws InterruptedException {

    }

    @Override
    public Msg receiveMessage(String content) throws InterruptedException, SmackException.NotConnectedException, BusinessException {
        Msg msg = getMsg(content);
        if (null != msg) {
            xmppServer.sendMessage(msg);
        }
        return msg;
    }

    @Override
    public Msg receiveMessage(Msg msg) throws InterruptedException, SmackException.NotConnectedException, BusinessException {
        return null;
    }

    @Override
    public void sendMsg(Msg msgBean) {
        if (isOnline()) {
            try {
                getMsgSendService().produce(msgBean);
            } catch (InterruptedException e) {
                logger.error(e);
            }
        }
    }

    @Override
    public boolean joinQueue() throws InterruptedException {
        return false;
    }

    @Override
    public boolean uninstall() throws InterruptedException {
        return false;
    }

    @Override
    public boolean isOnline() {
        return false;
    }

    @Override
    public void onlinePush() throws BusinessException, InterruptedException {

    }

    public abstract Msg getMsg(String content);
}
