package com.baodanyun.websocket.alarm;

import com.baodanyun.websocket.enums.AlarmTypeEnum;
import com.baodanyun.websocket.event.AlarmEvent;

/**
 * 客服链对象
 * 分钟时间客服不回复访客消息则提示客服
 *
 * @author hubo
 * @since 2017-06-29 18:57
 **/
public class AlarmToCustomer extends AlarmHandler {

     /*5分钟*/;
    private final static int MINUTE = 5;

    private AlarmToCustomer(Builder builder) {
        this.nextAlarmHandler = builder.nextHandler;
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

    public static class Builder {
        private AlarmHandler nextHandler;

        public Builder() {

        }

        public Builder(AlarmHandler nextHandler) {
            this.nextHandler = nextHandler;
        }

        public Builder nextHandler(AlarmHandler nextHandler) {
            this.nextHandler = nextHandler;
            return this;
        }

        public AlarmToCustomer build() {
            return new AlarmToCustomer(this);
        }
    }

}
