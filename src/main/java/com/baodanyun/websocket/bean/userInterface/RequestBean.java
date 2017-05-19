package com.baodanyun.websocket.bean.userInterface;

import java.io.Serializable;

/**
 * Created by yutao on 2016/10/10.
 */
public class RequestBean implements Serializable {

    public RequestBean() {
    }

    public RequestBean(Long uid) {
        this.uid = uid;
    }

    private Long uid;

    private String openId;

    public Long getUid() {
        return uid;
    }

    public void setUid(Long uid) {
        this.uid = uid;
    }

    public String getOpenId() {
        return openId;
    }

    public void setOpenId(String openId) {
        this.openId = openId;
    }
}
