package com.baodanyun.websocket.alarm;

import com.baodanyun.websocket.enums.AlarmTypeEnum;
import com.baodanyun.websocket.event.AlarmEvent;
import org.springframework.stereotype.Service;

/**
 * Boss告警链
 * 15分钟时间客服不回复访客消息通知Boss
 *
 * @author hubo
 * @since 2017-06-29 18:57
 **/
@Service
public class AlarmToBoss extends AlarmHandler {

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
        if (ruleTime >= AlarmTypeEnum.TYPE2.getMinute() && count == 1) {
            alarmInfo.setCount(++count);
            alarmInfo.setAlarmTypeEnum(AlarmTypeEnum.TYPE2);

            super.processAlarm(alarmInfo);
        } else {
            if (getNextAlarmHandler() != null)
                getNextAlarmHandler().alarm(ruleTime, alarmInfo);
        }
    }
}
