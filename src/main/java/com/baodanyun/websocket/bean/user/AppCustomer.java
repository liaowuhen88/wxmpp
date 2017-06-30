package com.baodanyun.websocket.bean.user;

import java.io.Serializable;

/**
 * Created by yutao on 2016/7/12.
 * 客服应该是有别于其他用户的
 */
public class AppCustomer implements Serializable {

    private AbstractUser visitor;
    private AbstractUser customer;
    private boolean customerIsOnline;
    private String socketUrl;
    private String ossUrl;

    public String getOssUrl() {
        return ossUrl;
    }

    public void setOssUrl(String ossUrl) {
        this.ossUrl = ossUrl;
    }

    public String getSocketUrl() {
        return socketUrl;
    }

    public void setSocketUrl(String socketUrl) {
        this.socketUrl = socketUrl;
    }

    public AbstractUser getVisitor() {
        return visitor;
    }

    public void setVisitor(AbstractUser visitor) {
        this.visitor = visitor;
    }

    public AbstractUser getCustomer() {
        return customer;
    }

    public void setCustomer(AbstractUser customer) {
        this.customer = customer;
    }

    public boolean isCustomerIsOnline() {
        return customerIsOnline;
    }

    public void setCustomerIsOnline(boolean customerIsOnline) {
        this.customerIsOnline = customerIsOnline;
    }
}
