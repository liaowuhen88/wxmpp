package com.baodanyun.websocket.event;

import com.baodanyun.websocket.bean.user.AbstractUser;
import com.baodanyun.websocket.service.MsgSendService;

/**
 * Created by liaowuhen on 2017/5/15.
 */
public class VisitorJoinEvent {
    private AbstractUser visitor;
    private AbstractUser customer;
    private MsgSendService msgSendService;

    public VisitorJoinEvent(AbstractUser visitor, AbstractUser customer, MsgSendService msgSendService) {
        this.visitor = visitor;
        this.customer = customer;
        this.msgSendService = msgSendService;
    }

    public AbstractUser getVisitor() {
        return visitor;
    }

    public void setVisitor(AbstractUser visitor) {
        this.visitor = visitor;
    }

    public AbstractUser getCustomer() {
        return customer;
    }

    public void setCustomer(AbstractUser customer) {
        this.customer = customer;
    }

    public MsgSendService getMsgSendService() {
        return msgSendService;
    }

    public void setMsgSendService(MsgSendService msgSendService) {
        this.msgSendService = msgSendService;
    }
}
