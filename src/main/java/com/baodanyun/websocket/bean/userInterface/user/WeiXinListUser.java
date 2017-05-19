package com.baodanyun.websocket.bean.userInterface.user;

import com.baodanyun.websocket.bean.user.AbstractUser;

/**
 * Created by liaowuhen on 2017/3/31.
 */
public class WeiXinListUser {
    private AbstractUser user;
    private AbstractUser customerVcard;
    private WeiXinUser info;
    private AbstractUser customer;

    public AbstractUser getCustomer() {
        return customer;
    }

    public void setCustomer(AbstractUser customer) {
        this.customer = customer;
    }

    public AbstractUser getUser() {
        return user;
    }

    public void setUser(AbstractUser user) {
        this.user = user;
    }

    public WeiXinUser getInfo() {
        return info;
    }

    public void setInfo(WeiXinUser info) {
        this.info = info;
    }

    public AbstractUser getCustomerVcard() {
        return customerVcard;
    }

    public void setCustomerVcard(AbstractUser customerVcard) {
        this.customerVcard = customerVcard;
    }
}
