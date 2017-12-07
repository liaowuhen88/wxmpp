package com.baodanyun.websocket.service.dubbo.impl;

import com.alibaba.fastjson.JSON;
import com.baodanyun.websocket.service.dubbo.DubboSendMsgService;
import com.baodanyun.websocket.util.DubboPostSendMsgUtils;
import com.baodanyun.websocket.util.DubboServiceConfig;
import com.doubao.open.dto.message.Msg;
import com.doubao.open.dto.message.WxMsg;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by liaowuhen on 2017/11/2.
 */
@Service
public class DubboSendMsgServiceImpl implements DubboSendMsgService {


    final static String code = "SMS_TPL_DOUBAO_ALARM";
    private Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public void sendSmsMsg(Msg smsMsg, Map<String, Object> templateParams) {
        String content = getSmsMsg(smsMsg, templateParams);
        DubboPostSendMsgUtils.send(DubboServiceConfig.SM_SendMessage, content);
    }

    @Override
    public void sendSmsMsg(Msg smsMsg) {
        Map<String, Object> templateParams = new HashMap<String, Object>();
        templateParams.put("message", smsMsg.getContent());
        sendSmsMsg(smsMsg, templateParams);
    }

    @Override
    public void sendWxTempleteMsg(WxMsg smsMsg, Map<String, Object> templateParams) {
        String content = getWxTemplete(smsMsg, templateParams);
        DubboPostSendMsgUtils.send(DubboServiceConfig.SM_SendMessage, content);
    }


    private String getSmsMsg(Msg msg, Map templateParams) {

        Map<String, Object> reqMap = new HashMap<>();
        reqMap.put("templateCode", code);
        reqMap.put("templateParams", templateParams);
        reqMap.put("message", msg);

        String content = JSON.toJSONString(reqMap);
        return content;
    }


    private String getWxTemplete(WxMsg wxMsg, Map templateParams) {

        Map<String, Object> reqMap = new HashMap<>();
        reqMap.put("templateCode", "WECHAT_TPL_PE_BOOK_FAIL");
        reqMap.put("templateParams", templateParams);
        reqMap.put("message", wxMsg);

        return JSON.toJSONString(reqMap);
    }

}
