package com.baodanyun.websocket.service;

import com.doubao.open.dto.message.SmsMsg;

/**
 * Created by liaowuhen on 2017/11/2.
 */
public interface SendSmsService {
    public void sendMsg(SmsMsg smsMsg);
}
