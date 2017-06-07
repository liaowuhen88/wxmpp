package com.baodanyun.websocket.model;

import java.util.Date;

public class WechatMsg {
    private Integer id;

    private String msgTo;

    private Byte msgStatus;

    private String msgFrom;

    private Date ct;

    private String type;

    private String content;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getMsgTo() {
        return msgTo;
    }

    public void setMsgTo(String msgTo) {
        this.msgTo = msgTo == null ? null : msgTo.trim();
    }

    public Byte getMsgStatus() {
        return msgStatus;
    }

    public void setMsgStatus(Byte msgStatus) {
        this.msgStatus = msgStatus;
    }

    public String getMsgFrom() {
        return msgFrom;
    }

    public void setMsgFrom(String msgFrom) {
        this.msgFrom = msgFrom == null ? null : msgFrom.trim();
    }

    public Date getCt() {
        return ct;
    }

    public void setCt(Date ct) {
        this.ct = ct;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type == null ? null : type.trim();
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content == null ? null : content.trim();
    }
}