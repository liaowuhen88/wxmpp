package com.baodanyun.websocket.service.impl;

import com.baodanyun.websocket.bean.msg.Msg;
import com.baodanyun.websocket.bean.msg.status.StatusMsg;
import com.baodanyun.websocket.bean.user.AbstractUser;
import com.baodanyun.websocket.bean.user.Visitor;
import com.baodanyun.websocket.enums.MsgStatus;
import com.baodanyun.websocket.service.MsgSendControl;
import com.baodanyun.websocket.service.MsgSendService;
import com.baodanyun.websocket.service.WebSocketService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;

/**
 * Created by liaowuhen on 2017/3/7.
 */
public class MsgSendServiceImpl implements MsgSendService {

    @Autowired
    private WebSocketService webSocketService;

    @Autowired
    private MsgSendControl msgSendControl;

    @Override
    public StatusMsg getSMMsgSendTOVisitor(AbstractUser visitor, AbstractUser customer) {
        StatusMsg statusSysMsg = StatusMsg.buildStatus(Msg.Type.status);
        if(null != visitor){
            statusSysMsg.setToName(visitor.getNickName());
            statusSysMsg.setTo(visitor.getId());
            statusSysMsg.setLoginUsername(visitor.getLoginUsername());
            statusSysMsg.setCt(new Date().getTime());
            statusSysMsg.setOpenId(visitor.getOpenId());
        }
        if(null != customer){
            statusSysMsg.setFromName(customer.getNickName());
            statusSysMsg.setFrom(customer.getId());
        }

        return statusSysMsg;
    }

    @Override
    public StatusMsg getSMMsgSendTOVisitor(AbstractUser customer) {
        return getSMMsgSendTOVisitor(null,customer);
    }

    @Override
    public StatusMsg getSMMsgSendTOCustomer(AbstractUser visitor, AbstractUser customer) {
        StatusMsg statusSysMsg = StatusMsg.buildStatus(Msg.Type.status);
        if(null != visitor){
            statusSysMsg.setFromName(visitor.getNickName());
            statusSysMsg.setFrom(visitor.getId());
            statusSysMsg.setIcon(visitor.getIcon());
            statusSysMsg.setOpenId(visitor.getOpenId());
            statusSysMsg.setLoginUsername(visitor.getLoginUsername());
            statusSysMsg.setLoginTime(visitor.getLoginTime());
        }
        if(null != customer){
            statusSysMsg.setTo(customer.getId());
        }

        return statusSysMsg;
    }

    public StatusMsg getSMMsgSendTOCustomer(AbstractUser customer) {
        StatusMsg statusSysMsg = StatusMsg.buildStatus(Msg.Type.status);
        statusSysMsg.setTo(customer.getId());
        return statusSysMsg;
    }

    @Override
    public void sendSMMsgToVisitor(AbstractUser visitor, AbstractUser customer, MsgStatus status) throws InterruptedException {
        StatusMsg statusSysMsg = getSMMsgSendTOVisitor(visitor,customer);
        statusSysMsg.setStatus(status);
        webSocketService.produce(statusSysMsg);
    }

    @Override
    public void sendSMMsgToVisitor(AbstractUser customer, MsgStatus status) throws InterruptedException {
        StatusMsg statusSysMsg = getSMMsgSendTOVisitor(customer);
        statusSysMsg.setStatus(status);
        webSocketService.produce(statusSysMsg);
    }

    @Override
    public void sendSMMsgToCustomer(AbstractUser visitor, AbstractUser customer, MsgStatus status) throws InterruptedException {
        StatusMsg statusSysMsg = getSMMsgSendTOCustomer(visitor,customer);
        statusSysMsg.setStatus(status);
        webSocketService.produce(statusSysMsg);
    }

    @Override
    public void sendSMMsgToCustomer(AbstractUser customer, MsgStatus status) throws InterruptedException {
        StatusMsg statusSysMsg = getSMMsgSendTOCustomer(customer);
        statusSysMsg.setStatus(status);
        webSocketService.produce(statusSysMsg);
    }

    @Override
    public void sendHelloToVisitor(AbstractUser visitor, AbstractUser customer) throws InterruptedException {
        Msg msg = msgSendControl.getMsgHelloToVisitor((Visitor) visitor, customer);
        webSocketService.produce(msg);

    }

    @Override
    public void sendHelloToCustomer(AbstractUser visitor, AbstractUser customer) throws InterruptedException {
        Msg msg = msgSendControl.getMsgHelloToCustomer(visitor, customer);
        webSocketService.produce(msg);
    }

    @Override
    public void produce(Msg msgBean) throws InterruptedException {
        webSocketService.produce(msgBean);
    }
}
