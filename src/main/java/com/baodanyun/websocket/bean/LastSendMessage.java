package com.baodanyun.websocket.bean;

/**
 * Created by liaowuhen on 2016/11/16.
 */
public class LastSendMessage {
    private String from;
    private String to;
    private Long time;

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public Long getTime() {
        return time;
    }

    public void setTime(Long time) {
        this.time = time;
    }
}
