package com.baodanyun.websocket.service.dubbo;

import com.doubao.open.dto.message.Msg;
import com.doubao.open.dto.message.WxMsg;

import java.util.Map;

/**
 * Created by liaowuhen on 2017/11/2.
 */
public interface DubboSendMsgService {
    void sendSmsMsg(Msg smsMsg, Map<String, Object> templateParams);

    void sendSmsMsg(Msg smsMsg);

    void sendWxTempleteMsg(WxMsg smsMsg, Map<String, Object> templateParams);
}
