package com.baodanyun.websocket.alarm;

import com.baodanyun.websocket.alarm.listener.AlarmListener;
import com.baodanyun.websocket.alarm.listener.AlarmModels;
import com.baodanyun.websocket.event.AlarmEvent;
import org.jivesoftware.smack.packet.Message;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

/**
 * 告警责任链
 *
 * @author hubo
 * @since 2017-06-29 18:53
 **/
@Service
public class AlarmHandler {
    protected final Logger LOGGER = LoggerFactory.getLogger(this.getClass());
    protected AlarmHandler nextAlarmHandler;
    @Autowired
    @Qualifier("writeDBListener")
    private AlarmListener writeDBListener;
    @Autowired
    @Qualifier("alarmWechatListener")
    private AlarmListener alarmWechatListener;
    @Autowired
    @Qualifier("alarmSmsListener")
    private AlarmListener alarmSmsListener;

    protected AlarmHandler getNextAlarmHandler() {
        return nextAlarmHandler;
    }

    public void setNextAlarmHandler(AlarmHandler nextAlarmHandler) {
        this.nextAlarmHandler = nextAlarmHandler;
    }

    /**
     * 告警
     *
     * @param ruleTime 规则时间
     */
    protected void alarm(final long ruleTime, final AlarmEvent alarmInfo) {
    }

    ;

    /**
     * 是否告警
     */
    protected boolean isAlarm() {
        return false;
    }

    /**
     * 告警业务
     * 执行告警业务写库存及发微信
     *
     * @param alarmInfo 告警信息
     */
    protected final void processAlarm(final AlarmEvent alarmInfo) {
        //打印日志
        this.printLong(alarmInfo);

        AlarmModels models = new AlarmModels();
        models.addListener(writeDBListener); //记录到库

        if (isAlarm()) {
            models.addListener(alarmWechatListener);//微信告警
            models.addListener(alarmSmsListener);//短信告警
        }

        models.executeAlarm(alarmInfo);
    }

    /**
     * 打印记录日志且记入到库
     *
     * @param alarmInfo
     */
    private void printLong(final AlarmEvent alarmInfo) {
        DateTimeFormatter fmt = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss");
        Message message = alarmInfo.getMessage();

        String text = "%s分钟后客服无回复;客服:%s;用户:%s;发送消息时间:%s;现在时间:%s;消息内容:[%s]";
        String content = String.format(text,
                alarmInfo.getAlarmTypeEnum().getMinute(),
                message.getTo(),
                message.getFrom(),
                new DateTime(alarmInfo.getVisitorSendMsgTime()).toString(fmt),
                DateTime.now().toString(fmt),
                message.getBody());

        LOGGER.info(content);
    }
}

