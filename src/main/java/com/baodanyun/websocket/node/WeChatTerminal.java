package com.baodanyun.websocket.node;

import com.baodanyun.websocket.bean.msg.Msg;
import com.baodanyun.websocket.bean.user.AbstractUser;
import com.baodanyun.websocket.enums.MsgStatus;
import com.baodanyun.websocket.event.SendMsgToWeChatEvent;
import com.baodanyun.websocket.event.VisitorLoginEvent;
import com.baodanyun.websocket.event.VisitorReciveMsgEvent;
import com.baodanyun.websocket.exception.BusinessException;
import com.baodanyun.websocket.model.WechatMsg;
import com.baodanyun.websocket.node.sendUtils.WeChatResponse;
import com.baodanyun.websocket.node.sendUtils.WeChatSendUtils;
import com.baodanyun.websocket.util.CommonConfig;
import com.baodanyun.websocket.util.EventBusUtils;
import org.apache.commons.lang.StringUtils;
import org.jivesoftware.smack.SmackException;
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

        this.wecahtLoginEvt();
    }

    public void wecahtLoginEvt() {
        //用户从微信进入
        VisitorLoginEvent vle = new VisitorLoginEvent(this.getAbstractUser(),
                this.getChatNodeAdaptation().getAbstractUser(), CommonConfig.LOGIN__FROM_WE_CHAT_ACTIVE);
        EventBusUtils.post(vle);
    }

    @Override
    boolean joinQueue() {
        Msg hello = getMsgHelloToVisitor();
        hello.setFrom(this.getAbstractUser().getOpenId());

        WeChatSendUtils.send(hello);

        return true;
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

    @Override
    public void receiveFromGod(Msg msg) throws InterruptedException, BusinessException, SmackException.NotConnectedException {
        super.receiveFromGod(msg);

        this.wechatMsgEvt(msg);
    }

    public void wechatMsgEvt(Msg msg) {
        //消息来源于微信发事件中心
        VisitorReciveMsgEvent vle = new VisitorReciveMsgEvent(this.getAbstractUser(),
                this.getChatNodeAdaptation().getAbstractUser(), msg.getContent(), CommonConfig.MSG_SOURCE_WE_CHAT_ACTIVE);
        EventBusUtils.post(vle);
    }
}
