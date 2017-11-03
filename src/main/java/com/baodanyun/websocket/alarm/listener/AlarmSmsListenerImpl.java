package com.baodanyun.websocket.alarm.listener;

import com.baodanyun.websocket.enums.AlarmTypeEnum;
import com.baodanyun.websocket.event.AlarmEvent;
import com.baodanyun.websocket.service.SendSmsService;
import com.baodanyun.websocket.util.XMPPUtil;
import com.doubao.open.dto.message.SmsMsg;
import org.jivesoftware.smack.packet.Message;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * 告警发微信公众号提醒
 *
 * @author hubo
 * @since 2017-06-30 15:40
 **/
@Service("alarmSmsListener")
public class AlarmSmsListenerImpl implements AlarmListener {

    /**
     * boss的openId对应的key
     */
    private static final String BOSS_KEY = "wangjing";
    /**
     * 人名与openid对应的关系
     */
    private static Map<String, String> MobileMap = new HashMap<>();

    static {
        MobileMap.put("yutao", "18649041578"); //胡波
        MobileMap.put("hubo", "18511072313"); //胡波
        MobileMap.put("maqiumeng", "18500300506"); //马秋萌
        MobileMap.put("wangjing", "13651399787"); //汪婧
        MobileMap.put("hushuangyue", "13811830904"); //胡双月
        MobileMap.put("liuya", "180038771877"); //刘雅
        MobileMap.put("zhangchi", "13810208809"); //张弛用马秋萌的openid
        MobileMap.put("zhangfuliang", "18811055923"); //张弛用马秋萌的openid
        MobileMap.put("boss", "18649041578"); //张启科
    }

    private final Logger LOGGER = LoggerFactory.getLogger(AlarmSmsListenerImpl.class);
    @Autowired
    public SendSmsService sendSmsService;

    @Override
    public void alarm(final AlarmEvent alarmInfo) {
        Message message = alarmInfo.getMessage();

        DateTimeFormatter fmt = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss");
        String sendTime = new DateTime(alarmInfo.getVisitorSendMsgTime()).toString(fmt);
        String content = "用户【%s】【%s】发送消息【%s】到【%s】,已超时【%s】分钟未回复";

        String customerName = XMPPUtil.jidToName(message.getTo());
        content = String.format(content, XMPPUtil.jidToName(message.getFrom()), sendTime, message.getBody(),
                customerName, alarmInfo.getAlarmTypeEnum().getMinute());

        SmsMsg smsMsg = new SmsMsg();
        smsMsg.setMobile(MobileMap.get(customerName));
        smsMsg.setBizId(100);
        smsMsg.setTaskId("001task");
        smsMsg.setContent(content);

        sendSmsService.sendMsg(smsMsg);

        if (alarmInfo.getAlarmTypeEnum() == AlarmTypeEnum.TYPE2) {//15分钟无回复发送到leader汪婧
            SmsMsg toBossMsg = new SmsMsg();
            BeanUtils.copyProperties(smsMsg, toBossMsg);
            toBossMsg.setMobile(MobileMap.get(BOSS_KEY));

            sendSmsService.sendMsg(smsMsg);
        }
    }

}
