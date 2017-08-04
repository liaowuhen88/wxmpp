package com.baodanyun.websocket.alarm.listener;

import com.baodanyun.websocket.event.AlarmEvent;

/**
 * 告警Listener
 *
 * @author hubo
 * @since 2017-06-30 15:37
 **/
public interface AlarmListener {

    void alarm(AlarmEvent alarmInfo);
}
