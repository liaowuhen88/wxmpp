package com.baodanyun.websocket.alarm.listener;

import com.alibaba.fastjson.JSON;
import com.baodanyun.websocket.bean.msg.Msg;
import com.baodanyun.websocket.event.AlarmEvent;
import com.baodanyun.websocket.node.sendUtils.WeChatResponse;
import com.baodanyun.websocket.node.sendUtils.WeChatSendUtils;
import com.baodanyun.websocket.util.XMPPUtil;
import org.jivesoftware.smack.packet.Message;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 告警发微信公众号提醒
 *
 * @author hubo
 * @since 2017-06-30 15:40
 **/
public class AlarmWechatListenerImpl implements AlarmListener {
    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    @Override
    public void alarm(final AlarmEvent alarmInfo) {
        Message message = alarmInfo.getMessage();
        DateTimeFormatter fmt = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss");
        String sendTime = new DateTime(alarmInfo.getVisitorSendMsgTime()).toString(fmt);
        String content = "用户%s【%s】发送消息【%s】到【%s】,已超时【%s】分钟未回复";

        content = String.format(content, XMPPUtil.jidToName(message.getFrom()), sendTime, message.getBody(),
                XMPPUtil.jidToName(message.getTo()), alarmInfo.getAlarmTypeEnum().getMinute());

        Msg msg = new Msg();
        msg.setFrom("oAH_qsm7Z48eQHZzhs6t_8xL5m9E");
        msg.setOpenId("oAH_qsm7Z48eQHZzhs6t_8xL5m9E");
        msg.setType("text");
        msg.setContentType("text");
        msg.setContent(content);

        LOGGER.info("发送微信: " + JSON.toJSONString(msg));
        WeChatResponse response = WeChatSendUtils.send(msg);

        if (null == response || !response.getAccept()) {
            LOGGER.info("发送微信失败");
        } else {
            LOGGER.info("发送微信成功: " + JSON.toJSONString(response));
        }
    }
}
