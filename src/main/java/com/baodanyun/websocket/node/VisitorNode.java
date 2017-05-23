package com.baodanyun.websocket.node;

import com.baodanyun.websocket.bean.msg.Msg;
import com.baodanyun.websocket.bean.user.AbstractUser;
import com.baodanyun.websocket.bean.user.Visitor;
import com.baodanyun.websocket.event.VisitorJoinEvent;
import com.baodanyun.websocket.event.VisitorLoginEvent;
import com.baodanyun.websocket.exception.BusinessException;
import com.baodanyun.websocket.util.EventBusUtils;
import com.baodanyun.websocket.util.JSONUtil;
import org.apache.log4j.Logger;
import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.XMPPException;

import java.io.IOException;

/**
 * Created by liaowuhen on 2017/5/23.
 */
public abstract class VisitorNode extends AbstractNode {
    private static final Logger logger = Logger.getLogger(VisitorNode.class);
    private Visitor visitor;

    public VisitorNode(Visitor visitor) {
        this.visitor = visitor;
    }

    @Override
    public AbstractUser getAbstractUser() {
        return visitor;
    }

    @Override
    public boolean login() throws IOException, XMPPException, SmackException, BusinessException {
        return this.getXmppNode().login();
    }

    @Override
    public void logout() throws InterruptedException {
        this.getXmppNode().getNodes().remove(this);

    }


    @Override
    public Msg receiveMessage(String content) throws InterruptedException, SmackException.NotConnectedException, BusinessException {
        Msg msg = super.receiveMessage(content);

        return msg;
    }


    @Override
    public Msg receiveMessage(Msg msg) throws InterruptedException, SmackException.NotConnectedException, BusinessException {
        return null;
    }

    @Override
    public boolean joinQueue() throws InterruptedException {
        VisitorJoinEvent ve = new VisitorJoinEvent(visitor, visitor.getCustomer(), this.getMsgSendService());

        EventBusUtils.post(ve);

        return true;
    }

    @Override
    public boolean uninstall() throws InterruptedException {
        return false;
    }

    @Override
    public boolean isOnline() {
        return true;
    }

    @Override
    public void onlinePush() throws BusinessException, InterruptedException {
        VisitorLoginEvent vl = new VisitorLoginEvent(visitor, visitor.getCustomer(), this.getMsgSendService());

        EventBusUtils.post(vl);


        joinQueue();

        pushOfflineMsg();

    }

    @Override
    public Msg getMsg(String content) {
        try {
            Msg msg = JSONUtil.toObject(Msg.class, content);
            msg.setTo(visitor.getCustomer().getId());
            msg.setFrom(visitor.getId());
            return msg;
        } catch (Exception e) {
            logger.error(e);
        }
        return null;
    }

}
