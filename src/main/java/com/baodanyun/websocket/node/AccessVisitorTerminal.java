package com.baodanyun.websocket.node;

import com.baodanyun.websocket.bean.msg.Msg;
import com.baodanyun.websocket.event.VisitorReciveMsgEvent;
import com.baodanyun.websocket.event.WechatLoginEvent;
import com.baodanyun.websocket.exception.BusinessException;
import com.baodanyun.websocket.util.CommonConfig;
import com.baodanyun.websocket.util.EventBusUtils;
import org.jivesoftware.smack.SmackException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.socket.WebSocketSession;

/**
 * Created by liaowuhen on 2017/5/23.
 */
public class AccessVisitorTerminal extends WeChatTerminal {
    private static final Logger logger = LoggerFactory.getLogger(WeChatTerminal.class);

    private WebSocketSession session;

    AccessVisitorTerminal(ChatNodeAdaptation chatNodeAdaptation, String id) {
        super(chatNodeAdaptation, id);
    }

    public WebSocketSession getSession() {
        return session;
    }

    public void setSession(WebSocketSession session) {
        this.session = session;
    }

    @Override
    public void online() throws InterruptedException, BusinessException {
        super.online();
    }

    public void wecahtLoginEvt() {
        //用户被动接入微信
        WechatLoginEvent vle = new WechatLoginEvent(this.getAbstractUser(),
                this.getChatNodeAdaptation().getAbstractUser(), CommonConfig.LOGIN__FROM_WE_CHAT_PASSIVE);
        EventBusUtils.post(vle);
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
                this.getChatNodeAdaptation().getAbstractUser(), msg.getContent(),
                CommonConfig.MSG_SOURCE_WE_CHAT_PASSIVE);
        EventBusUtils.post(vle);
    }
}
