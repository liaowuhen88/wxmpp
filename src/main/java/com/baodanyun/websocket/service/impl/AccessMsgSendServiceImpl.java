package com.baodanyun.websocket.service.impl;

import com.baodanyun.websocket.bean.msg.Msg;
import com.baodanyun.websocket.bean.msg.status.StatusMsg;
import com.baodanyun.websocket.bean.user.AbstractUser;
import com.baodanyun.websocket.enums.MsgStatus;
import com.baodanyun.websocket.service.MessageSendToWeixin;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by liaowuhen on 2017/3/6.
 */
@Service("accessMsgSendService")
public class AccessMsgSendServiceImpl extends MsgSendServiceImpl {
    @Autowired
    public MessageSendToWeixin messageSendToWeixin;

    @Override
    public StatusMsg getSMMsgSendTOVisitor(AbstractUser visitor, AbstractUser customer) {
        return null;
    }

    @Override
    public void sendSMMsgToVisitor(AbstractUser visitor, AbstractUser customer, MsgStatus status) throws InterruptedException {

    }

    @Override
    public void sendSMMsgToCustomer(AbstractUser customer, MsgStatus status) throws InterruptedException {

    }

    @Override
    public void sendHelloToVisitor(AbstractUser visitor, AbstractUser customer) {
    }

    @Override
    public void sendHelloToCustomer(AbstractUser visitor, AbstractUser customer) throws InterruptedException {

    }

    @Override
    public void produce(Msg msgBean) throws Exception {
        messageSendToWeixin.send(msgBean, msgBean.getFrom(), "sys");
    }
}
