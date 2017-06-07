package com.baodanyun.websocket.event;

import com.baodanyun.websocket.model.WechatMsg;

/**
 * Created by liaowuhen on 2017/6/7.
 */
public class SendMsgToWeChatEvent {
    private WechatMsg wechatMsg;

    public SendMsgToWeChatEvent(WechatMsg wechatMsg) {
        this.wechatMsg = wechatMsg;
    }

    public WechatMsg getWechatMsg() {
        return wechatMsg;
    }

    public void setWechatMsg(WechatMsg wechatMsg) {
        this.wechatMsg = wechatMsg;
    }
}
