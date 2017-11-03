package com.baodanyun.wxmpp.test;

import com.alibaba.fastjson.JSON;
import com.baodanyun.websocket.service.SendSmsService;
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
    public SendSmsService sendSmsService;


    private Logger logger = LoggerFactory.getLogger(getClass());

    /**
     * 发送微信模板消息
     */
    @Test
    public void pcontractBaseMessTest() {

        SmsMsg smsMsg = new SmsMsg();
        smsMsg.setMobile("18649041578");
        smsMsg.setBizId(100);
        smsMsg.setTaskId("001task");
        smsMsg.setContent("告警信息");

        sendSmsService.sendMsg(smsMsg);
    }

    private String get() {
        WxMsg wxMsg = new WxMsg();
        wxMsg.setToUser("oAH_qsm7Z48eQHZzhs6t_8xL5m9E");

        //模板中的占位符
        //{"touser":"$!openId","msgtype":"image","image":{"media_id":"$!mediaId"}}
        Map<String, Object> tplMap = new HashMap<String, Object>();
        tplMap.put("openId", "oAH_qsm7Z48eQHZzhs6t_8xL5m9E");
        tplMap.put("frontName", "我是胡波");

        Map<String, Object> reqMap = new HashMap<>();
        reqMap.put("templateCode", "WECHAT_TPL_PE_BOOK_FAIL");
        reqMap.put("templateParams", tplMap);
        reqMap.put("message", wxMsg);

        return JSON.toJSONString(reqMap);
    }


}
