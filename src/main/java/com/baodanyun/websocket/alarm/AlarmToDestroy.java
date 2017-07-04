package com.baodanyun.websocket.alarm;

import com.baodanyun.websocket.enums.AlarmTypeEnum;
import com.baodanyun.websocket.event.AlarmEvent;


/**
 * 销毁任务对象链
 * 超过30分钟时间客服不回复访客清除任务
 *
 * @author hubo
 * @since 2017-06-29 18:57
 **/
public class AlarmToDestroy extends AlarmHandler {

    @Override
    protected boolean isAlarm() {
        return false;
    }

    /**
     * 告警业务操作
     *
     * @param ruleTime  规则时间
     * @param alarmInfo 告警详情
     */
    public void alarm(final long ruleTime, final AlarmEvent alarmInfo) {

        if (ruleTime >= AlarmTypeEnum.TYPE3.getMinute()) {
            AlarmBoxer.getInstance().remove(alarmInfo); //清除此条任务
            alarmInfo.setAlarmTypeEnum(AlarmTypeEnum.TYPE3);

            super.processAlarm(alarmInfo);
        } else {
            if (getNextAlarmHandler() != null)
                getNextAlarmHandler().alarm(ruleTime, alarmInfo);
        }
    }
}
