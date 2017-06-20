package com.baodanyun.websocket.bean.user;

import java.io.Serializable;

/**
 * Created by yutao on 2016/7/12.
 * 客服应该是有别于其他用户的
 */
public class Customer extends AbstractUser implements Serializable {


    private String to;
    private String accessType;

    public Customer() {
        setUserType(UserType.customer);
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getAccessType() {
        return accessType;
    }

    public void setAccessType(String accessType) {
        this.accessType = accessType;
    }
}
