package com.baodanyun.websocket.event;/**
 * @author hubo
 * @since 2017-06-29 17:22
 **/

import com.baodanyun.websocket.enums.AlarmEnum;
import org.jivesoftware.smack.packet.Message;

/**
 * @author hubo
 * @since 2017-06-29 17:22
 **/
public class AlarmEvent {

    /**
     * 告警次数
     */
    private int count;

    /**
     * 类型
     */
    private byte type;

    private AlarmEnum alarmEnum;

    private Message message;
    /**
     * 访客发消息的时间
     */
    private long visitorSendMsgTime = System.currentTimeMillis();

    public AlarmEvent(AlarmEnum alarmEnum, Message message) {
        this.alarmEnum = alarmEnum;
        this.message = message;
    }

    public byte getType() {
        return type;
    }

    public void setType(byte type) {
        this.type = type;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public long getVisitorSendMsgTime() {
        return visitorSendMsgTime;
    }

    public void setVisitorSendMsgTime(long visitorSendMsgTime) {
        this.visitorSendMsgTime = visitorSendMsgTime;
    }

    public AlarmEnum getAlarmEnum() {
        return alarmEnum;
    }

    public void setAlarmEnum(AlarmEnum alarmEnum) {
        this.alarmEnum = alarmEnum;
    }

    public Message getMessage() {
        return message;
    }

    public void setMessage(Message message) {
        this.message = message;
    }
}
