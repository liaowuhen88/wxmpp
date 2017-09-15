package com.baodanyun.websocket.bean.dubbo;

/**
 * Created by liaowuhen on 2017/9/14.
 */
public class FollowWXResponse {
    private Byte status;
    private boolean follow;

    public Byte getStatus() {
        return status;
    }

    public void setStatus(Byte status) {
        this.status = status;
    }

    public boolean isFollow() {
        if (1 == status) {
            return true;
        }
        return false;
    }
}
