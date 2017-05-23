package com.baodanyun.websocket.event;

import com.baodanyun.websocket.bean.user.AbstractUser;
import com.baodanyun.websocket.service.MsgSendService;

/**
 * Created by liaowuhen on 2017/5/15.
 */
public class VisitorLoginEvent {

    private AbstractUser user;
    private AbstractUser customer;
    private transient MsgSendService msgSendService;

    public VisitorLoginEvent(AbstractUser user, AbstractUser customer, MsgSendService msgSendService) {
        this.user = user;
        this.customer = customer;
        this.msgSendService = msgSendService;
    }

    public MsgSendService getMsgSendService() {
        return msgSendService;
    }

    public void setMsgSendService(MsgSendService msgSendService) {
        this.msgSendService = msgSendService;
    }

    public AbstractUser getUser() {
        return user;
    }

    public void setUser(AbstractUser user) {
        this.user = user;
    }

    public AbstractUser getCustomer() {
        return customer;
    }

    public void setCustomer(AbstractUser customer) {
        this.customer = customer;
    }
}
