package com.baodanyun.websocket.bean.user;


/**
 * Created by yutao on 2016/7/12.
 * 客服应该是有别于其他用户的
 */
public class NewCustomer extends Customer {
    private String accessType;

    public String getAccessType() {
        return accessType;
    }

    public void setAccessType(String accessType) {
        this.accessType = accessType;
    }


}
