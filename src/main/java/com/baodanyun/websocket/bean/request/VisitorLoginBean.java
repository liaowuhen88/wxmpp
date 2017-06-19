package com.baodanyun.websocket.bean.request;

/**
 * Created by liaowuhen on 2017/6/19.
 */
public class VisitorLoginBean {
    //LOGIN_USER  登录用户名
    private String u;
    // 指定客服
    private String to;

    public String getU() {
        return u;
    }

    public void setU(String u) {
        this.u = u;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }
}
