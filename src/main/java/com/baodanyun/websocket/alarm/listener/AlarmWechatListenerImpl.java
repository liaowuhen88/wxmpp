package com.baodanyun.websocket.alarm.listener;

import com.alibaba.fastjson.JSON;
import com.baodanyun.websocket.bean.msg.Msg;
import com.baodanyun.websocket.core.common.Common;
import com.baodanyun.websocket.enums.AlarmTypeEnum;
import com.baodanyun.websocket.event.AlarmEvent;
import com.baodanyun.websocket.node.sendUtils.WeChatResponse;
import com.baodanyun.websocket.node.sendUtils.WeChatSendUtils;
import com.baodanyun.websocket.util.XMPPUtil;
import org.apache.commons.lang.StringUtils;
import org.jivesoftware.smack.packet.Message;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * 告警发微信公众号提醒
 *
 * @author hubo
 * @since 2017-06-30 15:40
 **/
public class AlarmWechatListenerImpl implements AlarmListener {
    /**
     * boss的openId对应的key
     */
    private static final String BOSS_KEY = "wangjing";
    private final Logger LOGGER = LoggerFactory.getLogger(AlarmWechatListenerImpl.class);
    /**
     * 人名与openid对应的关系
     */
    private static Map<String, String> openIdMap = new HashMap<>();

    static {
        openIdMap.put("hubo", "oAH_qsm7Z48eQHZzhs6t_8xL5m9E"); //胡波
        openIdMap.put("maqiumeng", "oAH_qsq4sqTB9RRRCsAqm6MNvk94"); //马秋萌
        openIdMap.put("wangjing", "oAH_qslUwS4cM6XDSv_AFPpyQ8fo"); //汪婧
        openIdMap.put("hushuangyue", "oAH_qsgrUvRgEcTz3ILwmKz90QKw"); //胡双月
        openIdMap.put("liuya", "oAH_qsumwmy510EzpSHHmMTTwchQ"); //刘雅
        openIdMap.put("zhangchi", "oAH_qsq4sqTB9RRRCsAqm6MNvk94"); //张弛用马秋萌的openid
        openIdMap.put("boss", "oAH_qsk7UoktC1IRUIACJGUZ-Tkg"); //张启科
    }

    @Override
    public void alarm(final AlarmEvent alarmInfo) {
        Message message = alarmInfo.getMessage();
        String msgBody = message.getBody();
        if (StringUtils.isBlank(msgBody)) {
            return;
        }

        boolean flag = false;
        for (String str : Common.WORDS) {//过滤异常关键词
            if (str.equals(msgBody)) {
                flag = true;
                break;
            }
        }

        if (flag) {
            return;
        }
        DateTimeFormatter fmt = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss");
        String sendTime = new DateTime(alarmInfo.getVisitorSendMsgTime()).toString(fmt);
        String content = "用户【%s】【%s】发送消息【%s】到【%s】,已超时【%s】分钟未回复";

        String customerName = XMPPUtil.jidToName(message.getTo());
        content = String.format(content, XMPPUtil.jidToName(message.getFrom()), sendTime, message.getBody(),
                customerName, alarmInfo.getAlarmTypeEnum().getMinute());

        Msg msg = new Msg();
        msg.setOpenId(openIdMap.get(customerName));
        msg.setFrom(openIdMap.get(customerName));
        msg.setType("text");
        msg.setContentType("text");
        msg.setContent(content);
        this.sendWechat(msg); //告警到客服

        if (alarmInfo.getAlarmTypeEnum() == AlarmTypeEnum.TYPE2) {//15分钟无回复发送到leader汪婧
            Msg toBossMsg = new Msg();
            BeanUtils.copyProperties(msg, toBossMsg);
            toBossMsg.setFrom(openIdMap.get(BOSS_KEY));
            msg.setOpenId(openIdMap.get(BOSS_KEY));

            this.sendWechat(toBossMsg);
        }
    }

    /**
     * 发送微信公众号
     *
     * @param msg 消息实体
     */
    private void sendWechat(Msg msg) {
        LOGGER.info("发送微信: " + JSON.toJSONString(msg));
        WeChatResponse response = WeChatSendUtils.send(msg);

        if (null == response || !response.getAccept()) {
            LOGGER.info("发送微信失败:" + JSON.toJSONString(msg));
        } else {
            LOGGER.info("发送微信成功: " + JSON.toJSONString(response));
        }

    }
}
