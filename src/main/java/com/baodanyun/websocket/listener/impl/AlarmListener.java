package com.baodanyun.websocket.listener.impl;

import com.baodanyun.websocket.alarm.AlarmBoxer;
import com.baodanyun.websocket.core.common.Common;
import com.baodanyun.websocket.event.AlarmEvent;
import com.baodanyun.websocket.listener.EventBusListener;
import com.baodanyun.websocket.util.JSONUtil;
import com.google.common.eventbus.Subscribe;
import org.apache.commons.lang.StringUtils;
import org.jivesoftware.smack.packet.Message;
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
        logger.info("告警: " + JSONUtil.toJson(alarmEvent));
        boolean flag = false;
        Message message = alarmEvent.getMessage();
        String msgBody = message.getBody();

        if (StringUtils.isBlank(msgBody)) {
            return false;
        }

        for (String str : Common.WORDS) {//过滤异常关键词
            if (str.equals(msgBody)) {
                flag = true;
                break;
            }
        }

        if (flag) {
            return false;
        }

        switch (alarmEvent.getAlarmEnum()) {
            case CUSTOMER:
                AlarmBoxer.getInstance().remove(alarmEvent);
                break;
            case VISITOR:
                AlarmBoxer.getInstance().put(alarmEvent);
                break;
        }

        return flag;
    }
}
