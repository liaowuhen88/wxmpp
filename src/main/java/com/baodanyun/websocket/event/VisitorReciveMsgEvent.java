package com.baodanyun.websocket.event;

import com.baodanyun.websocket.bean.user.AbstractUser;

/**
 * Created by liaowuhen on 2017/5/15.
 */
public class VisitorReciveMsgEvent {
    private AbstractUser user;
    private AbstractUser customer;
    private String content;


    public VisitorReciveMsgEvent(AbstractUser user, AbstractUser customer, String content) {
        this.user = user;
        this.customer = customer;
        this.content = content;
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
