package com.baodanyun.websocket.node;

import com.baodanyun.websocket.bean.msg.Msg;
import com.baodanyun.websocket.bean.user.Visitor;
import com.baodanyun.websocket.enums.MsgStatus;
import com.baodanyun.websocket.event.SendMsgToWeChatEvent;
import com.baodanyun.websocket.exception.BusinessException;
import com.baodanyun.websocket.model.WechatMsg;
import com.baodanyun.websocket.node.sendUtils.WeChatResponse;
import com.baodanyun.websocket.node.sendUtils.WeChatSendUtils;
import com.baodanyun.websocket.util.EventBusUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by liaowuhen on 2017/5/23.
 */
public class WeChatTerminal extends VisitorTerminal {

    private static final Logger logger = LoggerFactory.getLogger(WeChatTerminal.class);

    WeChatTerminal(ChatNodeAdaptation chatNodeAdaptation, String id) {
        super(chatNodeAdaptation);
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
            WeChatResponse response = WeChatSendUtils.send(msg);


            WechatMsg we = new WechatMsg();
            we.setMsgFrom(from);
            we.setMsgTo(this.getAbstractUser().getOpenId());
            we.setMsgStatus((byte) -1);

            if (null == response || !response.getAccept()) {
                this.getChatNodeAdaptation().messageCallBack(this.getAbstractUser(), MsgStatus.msgFail);

                // 发送失败记录
                msg.setFrom(this.getAbstractUser().getId());
                msg.setTo(from);

                if (null == response) {
                    msg.setContent("系统消息,微信发送超时，消息发送失败");
                } else {
                    if (StringUtils.isEmpty(response.getReason())) {
                        msg.setContent("系统消息,微信接口不通，返回信息为空，消息发送失败");
                    } else {
                        msg.setContent(response.getReason());
                    }

                }

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

    @Override
    boolean joinQueue() {
        return false;
    }

    @Override
    boolean uninstall() throws InterruptedException {
        return false;
    }

    @Override
    boolean customerOnline() {
        return false;
    }

    @Override
    boolean customerOffline() {
        return false;
    }


}
