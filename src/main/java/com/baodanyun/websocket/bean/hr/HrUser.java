package com.baodanyun.websocket.bean.hr;

/**
 * Created by liaowuhen on 2017/5/17.
 */
public class HrUser {
    private long uid;
    private String nickname;
    private String icon;

    public long getUid() {
        return uid;
    }

    public void setUid(long uid) {
        this.uid = uid;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }
}
