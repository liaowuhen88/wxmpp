package com.baodanyun.websocket.node;

import com.baodanyun.websocket.bean.msg.Msg;
import com.baodanyun.websocket.bean.user.AbstractUser;
import com.baodanyun.websocket.bean.user.Customer;
import com.baodanyun.websocket.enums.MsgStatus;
import com.baodanyun.websocket.exception.BusinessException;
import com.baodanyun.websocket.service.MsgSendService;
import com.baodanyun.websocket.util.CommonConfig;
import org.apache.log4j.Logger;
import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.XMPPException;

import java.io.IOException;

/**
 * Created by liaowuhen on 2017/5/23.
 */
public abstract class CustomerNode extends AbstractNode {
    private static final Logger logger = Logger.getLogger(CustomerNode.class);
    private Customer customer;


    public CustomerNode(Customer customer) {
        this.customer = customer;
    }

    @Override
    public AbstractUser getAbstractUser() {
        return customer;
    }

    @Override
    public MsgSendService getMsgSendService() {
        return null;
    }

    @Override
    public boolean login() throws IOException, XMPPException, SmackException, BusinessException {
        return this.getXmppNode().login();
    }


    @Override
    public Msg receiveMessage(Msg msg) throws InterruptedException, SmackException.NotConnectedException, BusinessException {
        return null;
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
    public void onlinePush() throws InterruptedException, BusinessException {

        getMsgSendService().sendSMMsgToCustomer(getAbstractUser(), MsgStatus.loginSuccess);

        getMsgSendService().sendSMMsgToCustomer(getAbstractUser(), MsgStatus.initSuccess);

        logger.info("保存到缓存[USER_CUSTOMER][" + getAbstractUser().getId() + "]--->" + userCacheServer.add(CommonConfig.USER_CUSTOMER, getAbstractUser()));
    }

}
