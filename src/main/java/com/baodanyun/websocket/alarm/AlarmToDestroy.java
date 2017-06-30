package com.baodanyun.websocket.alarm;

import com.baodanyun.websocket.event.AlarmEvent;


/**
 * 销毁任务对象链
 * 超过30分钟时间客服不回复访客清除任务
 *
 * @author hubo
 * @since 2017-06-29 18:57
 **/
public class AlarmToDestroy extends AlarmHandler {

    /*30分钟*/;
    private final static int MINUTE = 30;

    private AlarmToDestroy(Builder builder) {
        this.nextAlarmHandler = builder.nextHandler;
    }

    /**
     * 告警业务操作
     *
     * @param ruleTime  规则时间
     * @param alarmInfo
     */
    public void alarm(final long ruleTime, final AlarmEvent alarmInfo) {
        if (ruleTime >= MINUTE) {
            super.recordLog(String.format("%s分钟后客服无回复", MINUTE), alarmInfo);

            AlarmBoxer.getInstance().remove(alarmInfo); //清除此条任务
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

        public AlarmToDestroy build() {
            return new AlarmToDestroy(this);
        }
    }
}
