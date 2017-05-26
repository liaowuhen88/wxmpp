package com.baodanyun.websocket.bean.user;

import com.baodanyun.websocket.enums.MsgStatus;

import java.io.Serializable;

/**
 * Created by liaowuhen on 2017/3/8.
 */
public class Friend implements Serializable {

    String id;
    String name;
    String icon;
    String loginUsername;

    MsgStatus onlineStatus;
    String nickname;
    String waitTime;
    String openId;
    Long loginTime;//登录时间

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public MsgStatus getOnlineStatus() {
        return onlineStatus;
    }

    public void setOnlineStatus(MsgStatus onlineStatus) {
        this.onlineStatus = onlineStatus;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getWaitTime() {
        return waitTime;
    }

    public void setWaitTime(String waitTime) {
        this.waitTime = waitTime;
    }

    public String getLoginUsername() {
        return loginUsername;
    }

    public void setLoginUsername(String loginUsername) {
        this.loginUsername = loginUsername;
    }

    public String getOpenId() {
        return openId;
    }

    public void setOpenId(String openId) {
        this.openId = openId;
    }

    public Long getLoginTime() {
        return loginTime;
    }

    public void setLoginTime(Long loginTime) {
        this.loginTime = loginTime;
    }

    @Override
    public boolean equals(Object obj) {
        if (null == obj) {
            return false;
        } else {
            if (obj instanceof Friend) {
                return this.getId().equals(((Friend) obj).getId());
            } else {
                return false;
            }
        }
    }

    @Override
    public int hashCode() {
        return this.getId().hashCode();
    }
}
