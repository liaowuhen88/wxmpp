package com.baodanyun.websocket.bean.response;

import java.util.Date;

/**
 * Created by liaowuhen on 2017/6/12.
 */
public class WeChatMsgStatistics {
    private Long id;
    private String msgTo;
    private String msgFrom;
    private Byte fromCount = 0;
    private Byte toCount = 0;
    private Date sendTime;
    private Byte msgStatus;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMsgTo() {
        return msgTo;
    }

    public void setMsgTo(String msgTo) {
        this.msgTo = msgTo;
    }

    public String getMsgFrom() {
        return msgFrom;
    }

    public void setMsgFrom(String msgFrom) {
        this.msgFrom = msgFrom;
    }

    public Byte getFromCount() {
        return fromCount;
    }

    public void setFromCount(Byte fromCount) {
        this.fromCount = fromCount;
    }

    public Byte getToCount() {
        return toCount;
    }

    public void setToCount(Byte toCount) {
        this.toCount = toCount;
    }

    public Date getSendTime() {
        return sendTime;
    }

    public void setSendTime(Date sendTime) {
        this.sendTime = sendTime;
    }

    public Byte getMsgStatus() {
        return msgStatus;
    }

    public void setMsgStatus(Byte msgStatus) {
        this.msgStatus = msgStatus;
    }
}
