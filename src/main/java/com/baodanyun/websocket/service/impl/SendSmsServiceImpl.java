package com.baodanyun.websocket.service.impl;

import com.alibaba.fastjson.JSON;
import com.baodanyun.websocket.service.SendSmsService;
import com.baodanyun.websocket.util.Config;
import com.doubao.open.api.Constants;
import com.doubao.open.api.DefaultRequest;
import com.doubao.open.api.DefaultResponse;
import com.doubao.open.client.DoubaoClient;
import com.doubao.open.client.env.DouBaoEnvEnum;
import com.doubao.open.client.exception.DouBaoOpenException;
import com.doubao.open.dto.message.SmsMsg;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by liaowuhen on 2017/11/2.
 */
@Service
public class SendSmsServiceImpl implements SendSmsService {
    final static String serviceName = "doubao.message.MessageService.sendMessage";
    final static String code = "SMS_TPL_DOUBAO_ALARM";
    private Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public void sendMsg(SmsMsg smsMsg) {
        DefaultRequest defaultRequest = new DefaultRequest() {
            @Override
            public String serviceName() {
                return serviceName;
            }

            @Override
            public void checkBizParams() throws DouBaoOpenException {

            }
        };
        defaultRequest.setSignType(Constants.MD5);
        defaultRequest.setEncrypt(true);
        // 86603
        defaultRequest.setContent(getMsg(smsMsg));
        DoubaoClient client = new DoubaoClient(DouBaoEnvEnum.PRD, Config.dubbo_appKey, Config.dubbo_appSecret, Config.dubbo_privateKey);
        try {
            DefaultResponse response = client.call(defaultRequest);
            logger.info(JSON.toJSONString(response));
        } catch (DouBaoOpenException e) {
            logger.error("error", e);
        }
    }


    private String getMsg(SmsMsg smsMsg) {

        Map<String, Object> tplMap = new HashMap<String, Object>();
        tplMap.put("message", smsMsg.getContent());

        Map<String, Object> reqMap = new HashMap<>();
        reqMap.put("templateCode", code);
        reqMap.put("templateParams", tplMap);
        reqMap.put("message", smsMsg);

        String content = JSON.toJSONString(reqMap);

        logger.info(content);
        return content;
    }


}
