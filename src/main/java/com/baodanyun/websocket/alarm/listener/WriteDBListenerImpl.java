package com.baodanyun.websocket.alarm.listener;

import com.baodanyun.websocket.dao.AlarmLogMapper;
import com.baodanyun.websocket.event.AlarmEvent;
import com.baodanyun.websocket.model.AlarmLog;
import com.baodanyun.websocket.util.XMPPUtil;
import org.jivesoftware.smack.packet.Message;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * 告警记录到数据库
 *
 * @author hubo
 * @since 2017-06-30 15:40
 **/
@Service("writeDBListener")
public class WriteDBListenerImpl implements AlarmListener {

    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private AlarmLogMapper alarmLogMapper;

    @Override
    public void alarm(AlarmEvent alarmInfo) {
        try {
            Message message = alarmInfo.getMessage();

            AlarmLog log = new AlarmLog();
            log.setCreateTime(new Date());
            log.setCustomerName(XMPPUtil.jidToName(message.getTo()));
            log.setVisitorName(XMPPUtil.jidToName(message.getFrom()));
            log.setMessage(message.getBody());
            log.setSendTime(new DateTime(alarmInfo.getVisitorSendMsgTime()).toDate());
            log.setType((byte) alarmInfo.getAlarmTypeEnum().getType());

            alarmLogMapper.insertSelective(log);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
        }

    }
}
