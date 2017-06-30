package com.baodanyun.websocket.listener.impl;

import com.alibaba.fastjson.JSON;
import com.baodanyun.websocket.alarm.AlarmBoxer;
import com.baodanyun.websocket.event.AlarmEvent;
import com.baodanyun.websocket.listener.EventBusListener;
import com.baodanyun.websocket.util.JSONUtil;
import com.google.common.eventbus.Subscribe;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;


/**
 * 告警
 */
@Service
public class AlarmListener extends AbstarctEventBusListener<AlarmEvent> implements EventBusListener<AlarmEvent> {
    private static Logger logger = LoggerFactory.getLogger(AlarmListener.class);

    @Override
    @Subscribe
    public boolean processExpiringEvent(final AlarmEvent alarmEvent) {
        logger.info(JSONUtil.toJson(alarmEvent));
        System.out.println("事件: " + JSON.toJSONString(alarmEvent));

        switch (alarmEvent.getAlarmEnum()) {
            case CUSTOMER:
                AlarmBoxer.getInstance().remove(alarmEvent);
                break;
            case VISITOR:
                AlarmBoxer.getInstance().put(alarmEvent);
                break;
        }

        return false;
    }
}
