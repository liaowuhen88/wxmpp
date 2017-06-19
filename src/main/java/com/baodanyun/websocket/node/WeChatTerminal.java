package com.baodanyun.websocket.node;

import com.baodanyun.websocket.bean.msg.Msg;
import com.baodanyun.websocket.bean.user.AbstractUser;
import com.baodanyun.websocket.bean.user.Visitor;
import com.baodanyun.websocket.enums.MsgStatus;
import com.baodanyun.websocket.event.SendMsgToWeChatEvent;
import com.baodanyun.websocket.exception.BusinessException;
import com.baodanyun.websocket.model.WechatMsg;
import com.baodanyun.websocket.node.sendUtils.WeChatSendUtils;
import com.baodanyun.websocket.util.EventBusUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by liaowuhen on 2017/5/23.
 */
public class WeChatTerminal extends VisitorTerminal {

    private static final Logger logger = LoggerFactory.getLogger(WeChatTerminal.class);

    WeChatTerminal(ChatNodeAdaptation chatNodeAdaptation, AbstractUser visitor, String id) {
        super(chatNodeAdaptation,visitor);
        super.id = id;
    }

    @Override
    public boolean sendMsgToGod(Msg msg) {
        boolean flag = false;
        try {
            String from = msg.getFrom();
            String content = msg.getContent();
            msg.setType("text");
            msg.setFrom(this.getAbstractUser().getOpenId());
            flag = WeChatSendUtils.send(msg);


            WechatMsg we = new WechatMsg();
            we.setMsgFrom(from);
            we.setMsgTo(this.getAbstractUser().getOpenId());
            we.setMsgStatus((byte) -1);

            if (!flag) {
                ChatNodeManager.getCustomerXmppNode(((Visitor) getAbstractUser()).getCustomer()).messageCallBack(this.getAbstractUser(), MsgStatus.msgFail);

                // 发送失败记录
                msg.setFrom(msg.getTo());
                msg.setTo(from);
                msg.setContent("系统消息,微信接口不通，消息发送失败");
                receiveFromGod(msg);

                /**
                 * 入库
                 */
            } else {
                we.setMsgStatus((byte) 1);
            }
            we.setContent(content);
            we.setType("send");
            SendMsgToWeChatEvent sme = new SendMsgToWeChatEvent(we);
            EventBusUtils.post(sme);
        } catch (Exception e) {
            logger.error("error", "", e);
        }
        return flag;
    }

    @Override
    public void online() throws InterruptedException, BusinessException {
        super.online();
        Msg hello = getMsgHelloToVisitor(((Visitor) getAbstractUser()));
        hello.setFrom(this.getAbstractUser().getOpenId());

        WeChatSendUtils.send(hello);

        joinQueue();
    }


}