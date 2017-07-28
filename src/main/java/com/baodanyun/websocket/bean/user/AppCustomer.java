package com.baodanyun.websocket.bean.user;

/**
 * Created by yutao on 2016/7/12.
 * 客服应该是有别于其他用户的
 */
public class AppCustomer extends AbstractUser {

    private boolean customerIsOnline;
    private String socketUrl;
    private String ossUrl;
    private String token;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

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

    public boolean isCustomerIsOnline() {
        return customerIsOnline;
    }

    public void setCustomerIsOnline(boolean customerIsOnline) {
        this.customerIsOnline = customerIsOnline;
    }
}
