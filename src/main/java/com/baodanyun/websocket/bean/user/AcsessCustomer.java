package com.baodanyun.websocket.bean.user;


/**
 * Created by yutao on 2016/7/12.
 * 客服应该是有别于其他用户的
 */
public class AcsessCustomer extends Customer {


    public AcsessCustomer() {
        super();
    }

    private String to;

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }
}
