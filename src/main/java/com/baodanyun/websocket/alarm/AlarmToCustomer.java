package com.baodanyun.websocket.alarm;

import com.baodanyun.websocket.enums.AlarmTypeEnum;
import com.baodanyun.websocket.event.AlarmEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

/**
 * 客服链对象
 * 分钟时间客服不回复访客消息则提示客服
 *
 * @author hubo
 * @since 2017-06-29 18:57
 **/
@Service
public class AlarmToCustomer extends AlarmHandler {
    @Autowired
    private AlarmToDestroy alarmToDestroy;

    @Autowired
    private AlarmToBoss alarmToBoss;

    @PostConstruct
    public void init() {
        LOGGER.info("init customer");
        //组合责任链
        alarmToBoss.setNextAlarmHandler(alarmToDestroy);
        setNextAlarmHandler(alarmToBoss);
    }

    @Override
    protected boolean isAlarm() {
        return true;
    }

    /**
     * 告警业务操作
     *
     * @param ruleTime  规则时间
     * @param alarmInfo
     */
    public void alarm(final long ruleTime, final AlarmEvent alarmInfo) {
        int count = alarmInfo.getCount();
        if (ruleTime >= AlarmTypeEnum.TYPE1.getMinute() && count < 1) {//告警一次
            alarmInfo.setCount(++count);
            alarmInfo.setAlarmTypeEnum(AlarmTypeEnum.TYPE1);

            super.processAlarm(alarmInfo);
        } else {
            if (getNextAlarmHandler() != null)
                getNextAlarmHandler().alarm(ruleTime, alarmInfo);
        }
    }
}
