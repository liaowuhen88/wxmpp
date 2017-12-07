package com.baodanyun.websocket.util;

/**
 * Created by liaowuhen on 2017/12/1.
 */
public class DubboServiceConfig {
    public final static String SM_SendMessage = "doubao.message.MessageService.sendMessage";
    // 获取事件历史事件消息
    public final static String SM_GetByMessage = "doubao.doubao.MessageService.getByMessage";

    public final static String SM_GetFollow = "doubao.wechat.WechatService.getFollow";

    public final static String SM_GetQRCodeTempUrl = "doubao.wechat.WxQRCodeService.getQRCodeTempUrl";
}
