package com.baodanyun.websocket.event;

import com.baodanyun.websocket.bean.user.AbstractUser;

/**
 * Created by liaowuhen on 2017/5/15.
 */
public class VisitorReciveMsgEvent {
    private AbstractUser user;
    private AbstractUser customer;
    private String content;
    private String eventNum;


    public VisitorReciveMsgEvent(AbstractUser user, AbstractUser customer, String content, String eventNum) {
        this.user = user;
        this.customer = customer;
        this.content = content;
        this.eventNum = eventNum;
    }

    public String getEventNum() {
        return eventNum;
    }

    public void setEventNum(String eventNum) {
        this.eventNum = eventNum;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public AbstractUser getUser() {
        return user;
    }

    public void setUser(AbstractUser user) {
        this.user = user;
    }

    public AbstractUser getCustomer() {
        return customer;
    }

    public void setCustomer(AbstractUser customer) {
        this.customer = customer;
    }
}
