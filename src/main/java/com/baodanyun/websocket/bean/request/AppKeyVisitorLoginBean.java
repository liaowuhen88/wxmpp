package com.baodanyun.websocket.bean.request;

/**
 * Created by liaowuhen on 2017/6/19.
 */
public class AppKeyVisitorLoginBean {
    //LOGIN_USER  登录用户名
    private String appKey;
    private String id;
    private String nickname;
    private String avatar;
    private String source;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getAppKey() {
        return appKey;
    }

    public void setAppKey(String appKey) {
        this.appKey = appKey;
    }
}
