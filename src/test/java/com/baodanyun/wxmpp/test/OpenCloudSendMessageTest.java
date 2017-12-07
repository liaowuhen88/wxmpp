package com.baodanyun.wxmpp.test;

import com.alibaba.fastjson.JSON;
import com.baodanyun.websocket.service.dubbo.DubboSendMsgService;
import com.baodanyun.websocket.util.DubboPostSendMsgUtils;
import com.baodanyun.websocket.util.DubboServiceConfig;
import com.doubao.open.dto.message.SmsMsg;
import com.doubao.open.dto.message.WxMsg;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by liaowuhen on 2017/8/18.
 */
public class OpenCloudSendMessageTest extends BaseTest {
    @Autowired
    public DubboSendMsgService dubboSendMsgService;


    private Logger logger = LoggerFactory.getLogger(getClass());

    /**
     * 发送微信模板消息
     */
    @Test
    public void sendSms() {

        SmsMsg smsMsg = new SmsMsg();
        smsMsg.setMobile("18649041578");
        smsMsg.setBizId(100);
        smsMsg.setTaskId("001task");
        smsMsg.setContent("告警信息");

        Map<String, Object> templateParams = new HashMap<String, Object>();
        templateParams.put("message", smsMsg.getContent());

        dubboSendMsgService.sendSmsMsg(smsMsg, templateParams);
    }


    /**
     * 发送微信模板消息
     */
    @Test
    public void sendKFSms() {

        WxMsg wxMsg = new WxMsg();
        wxMsg.setToUser("oAH_qslG4CziSU43s2NtM5f-b61U");

        //模板中的占位符
        //{"touser":"$!openId","msgtype":"image","image":{"media_id":"$!mediaId"}}
        Map<String, Object> templateParams = new HashMap<String, Object>();
        templateParams.put("openId", "oAH_qslG4CziSU43s2NtM5f-b61U");
        templateParams.put("receiver", "oAH_qslG4CziSU43s2NtM5f-b61U");
        templateParams.put("text", "我是张文超");


        Map<String, Object> reqMap = new HashMap<>();
        reqMap.put("templateCode", "WECHAT_TPL_CUSTOM_TEXT");
        reqMap.put("templateParams", templateParams);
        reqMap.put("message", wxMsg);

        String content = JSON.toJSONString(reqMap);

        DubboPostSendMsgUtils.send(DubboServiceConfig.SM_SendMessage, content);
        logger.info(content);
    }

    /**
     * 发送微信模板消息
     */
    @Test
    public void pcontractBaseMessTest() {
        WxMsg wxMsg = new WxMsg();
        wxMsg.setToUser("oAH_qslG4CziSU43s2NtM5f-b61U");

        Map<String, Object> tplMap = new HashMap<String, Object>();
        tplMap.put("openId", "oAH_qslG4CziSU43s2NtM5f-b61U");
        tplMap.put("frontName", "我是张文超");


        dubboSendMsgService.sendWxTempleteMsg(wxMsg, tplMap);
    }
}
