package com.baodanyun.websocket.service.impl;

import com.baodanyun.websocket.bean.msg.Msg;
import com.baodanyun.websocket.bean.user.AbstractUser;
import com.baodanyun.websocket.listener.VisitorListener;
import com.baodanyun.websocket.model.MessageModel;
import com.baodanyun.websocket.node.VisitorChatNode;
import com.baodanyun.websocket.service.MessageSendToWeixin;
import com.baodanyun.websocket.service.MessageServer;
import com.baodanyun.websocket.service.OffLineMessageService;
import com.baodanyun.websocket.util.Config;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by liaowuhen on 2017/8/24.
 */

@Service
public class OffLineMessageServiceImpl implements OffLineMessageService {

    @Autowired
    private MessageSendToWeixin messageSendToWeixin;

    @Autowired
    private MessageServer messageServer;

    @Autowired
    private VisitorListener visitorListener;

    @Override
    public void dealOfflineMessage(VisitorChatNode visitorChatNode, String leaveMsg, String returnMsg) {
        AbstractUser visitor = visitorChatNode.getAbstractUser();
        AbstractUser customer = visitorChatNode.getCurrentChatNode().getAbstractUser();

        Msg sendMsg = new Msg(returnMsg);
        sendMsg.setContentType(Msg.MsgContentType.text.toString());
        Long ct = System.currentTimeMillis();
        sendMsg.setTo(customer.getId());
        sendMsg.setCt(ct);
        messageSendToWeixin.send(sendMsg, visitor.getOpenId());

        // 记录留言并且通知事件中心
        MessageModel mm = new MessageModel();
        mm.setContent(leaveMsg);
        mm.setOpenId(visitor.getOpenId());
        mm.setCid(customer.getId());
        mm.setPhone(visitor.getUserName());
        mm.setUsername(visitor.getNickName());

        int stu = messageServer.addMessage(mm);
        visitorListener.leaveMessage(visitor.getUid() + "", mm.getContent());

    }

    @Override
    public void dealOfflineMessage(VisitorChatNode visitorChatNode, String leaveMsg) {
        dealOfflineMessage(visitorChatNode, leaveMsg, Config.offlineWord);
    }
}
