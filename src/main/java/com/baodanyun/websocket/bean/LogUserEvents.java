package com.baodanyun.websocket.bean;

/**
 * Created by liaowuhen on 2017/2/27.
 */
public class LogUserEvents {
    private String myUid;
    private String openid;
    private Integer otype;
    private String evt;
    private String oid;
    // 备注信息
    private String mark;

    public String getOpenid() {
        return openid;
    }

    public void setOpenid(String openid) {
        this.openid = openid;
    }

    public Integer getOtype() {
        return otype;
    }

    public void setOtype(Integer otype) {
        this.otype = otype;
    }

    public String getEvt() {
        return evt;
    }

    public void setEvt(String evt) {
        this.evt = evt;
    }

    public String getOid() {
        return oid;
    }

    public void setOid(String oid) {
        this.oid = oid;
    }

    public String getMyUid() {
        return myUid;
    }

    public void setMyUid(String myUid) {
        this.myUid = myUid;
    }

    public String getMark() {
        return mark;
    }

    public void setMark(String mark) {
        this.mark = mark;
    }
}
