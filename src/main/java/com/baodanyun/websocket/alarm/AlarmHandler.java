package com.baodanyun.websocket.alarm;/**
 * @author hubo
 * @since 2017-06-29 18:53
 **/

import com.baodanyun.websocket.alarm.listener.AlarmModels;
import com.baodanyun.websocket.alarm.listener.WriteDBListenerImpl;
import com.baodanyun.websocket.event.AlarmEvent;
import com.baodanyun.websocket.util.XMPPUtil;
import org.jivesoftware.smack.packet.Message;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 告警责任链
 *
 * @author hubo
 * @since 2017-06-29 18:53
 **/
public abstract class AlarmHandler {
    protected final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    protected AlarmHandler nextAlarmHandler;

    protected AlarmHandler getNextAlarmHandler() {
        return nextAlarmHandler;
    }


    /**
     * 告警
     *
     * @param ruleTime 规则时间
     */
    protected abstract void alarm(final long ruleTime, final AlarmEvent alarmInfo);

    /**
     * 打印记录日志且记入到库
     *
     * @param alarmInfo
     */
    protected final void recordLog(final String tip, final AlarmEvent alarmInfo) {
        DateTimeFormatter fmt = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss");
        Message message = alarmInfo.getMessage();

        String content = String.format("%s;客服:%s;用户:%s;发送消息时间:%s;现在时间:%s;消息内容:[%s]",
                tip,
                XMPPUtil.jidToName(message.getTo()),
                XMPPUtil.jidToName(message.getFrom()),
                new DateTime(alarmInfo.getVisitorSendMsgTime()).toString(fmt),
                DateTime.now().toString(fmt),
                message.getBody());

        LOGGER.info(content);

        //打印日志后写库
        AlarmModels models = new AlarmModels();
        models.addListener(new WriteDBListenerImpl()); //记录到库的listener
        models.executeAlarm(alarmInfo);
    }
}

