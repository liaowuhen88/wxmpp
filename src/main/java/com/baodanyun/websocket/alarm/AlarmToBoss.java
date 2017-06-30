package com.baodanyun.websocket.alarm;

import com.baodanyun.websocket.event.AlarmEvent;

/**
 * Boss告警链
 * 15分钟时间客服不回复访客消息通知Boss
 *
 * @author hubo
 * @since 2017-06-29 18:57
 **/
public class AlarmToBoss extends AlarmHandler {

    /*15分钟*/;
    private final static int MINUTE = 4;

    private AlarmToBoss(Builder builder) {
        this.nextAlarmHandler = builder.nextHandler;
    }

    /**
     * 告警业务操作
     *
     * @param ruleTime  规则时间
     * @param alarmInfo
     */
    public void alarm(final long ruleTime, final AlarmEvent alarmInfo) {
        int count = alarmInfo.getCount();
        if (ruleTime >= MINUTE && count == 1) {
            super.recordLog(String.format("%s分钟后客服无回复告警给张总", MINUTE), alarmInfo);

            alarmInfo.setCount(++count);
        } else {
            if (getNextAlarmHandler() != null)
                getNextAlarmHandler().alarm(ruleTime, alarmInfo);
        }
    }


    public static class Builder {
        private AlarmHandler nextHandler;

        public Builder() {

        }

        public Builder nextHandler(AlarmHandler nextHandler) {
            this.nextHandler = nextHandler;
            return this;
        }

        public AlarmToBoss build() {
            return new AlarmToBoss(this);
        }
    }
}
