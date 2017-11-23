package com.baodanyun.websocket.bean;

import java.sql.Date;

/**
 * Created by liaowuhen on 2016/11/11.
 */

public class QuickReply {
    private Integer id;
    private String message;
    private String tag;
    private Date ct;
    private String addid;

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Date getCt() {
        return ct;
    }

    public void setCt(Date ct) {
        this.ct = ct;
    }

    public String getAddid() {
        return addid;
    }

    public void setAddid(String addid) {
        this.addid = addid;
    }
}
