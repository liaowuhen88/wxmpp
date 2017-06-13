package com.baodanyun.websocket.bean.response;

import java.util.Date;

/**
 * Created by liaowuhen on 2017/6/12.
 */
public class WeChatMsgStatisticsAdapter {
    private Long id;
    private String msgTo;
    private String msgFrom;
    private Byte successFromCount = 0;
    private Byte failFromCount = 0;
    private Byte successToCount = 0;
    private Byte failToCount = 0;
    private Date sendTime;

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

    public Byte getSuccessFromCount() {
        return successFromCount;
    }

    public void setSuccessFromCount(Byte successFromCount) {
        this.successFromCount = successFromCount;
    }

    public Byte getFailFromCount() {
        return failFromCount;
    }

    public void setFailFromCount(Byte failFromCount) {
        this.failFromCount = failFromCount;
    }

    public Byte getSuccessToCount() {
        return successToCount;
    }

    public void setSuccessToCount(Byte successToCount) {
        this.successToCount = successToCount;
    }

    public Byte getFailToCount() {
        return failToCount;
    }

    public void setFailToCount(Byte failToCount) {
        this.failToCount = failToCount;
    }

    public Date getSendTime() {
        return sendTime;
    }

    public void setSendTime(Date sendTime) {
        this.sendTime = sendTime;
    }
}
