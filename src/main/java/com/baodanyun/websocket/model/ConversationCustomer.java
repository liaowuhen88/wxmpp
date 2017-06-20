package com.baodanyun.websocket.model;

import java.util.Date;

public class ConversationCustomer {
    private Integer id;

    private String cjid;

    private String vjid;

    private Date ut;

    private String visitor;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCjid() {
        return cjid;
    }

    public void setCjid(String cjid) {
        this.cjid = cjid == null ? null : cjid.trim();
    }

    public String getVjid() {
        return vjid;
    }

    public void setVjid(String vjid) {
        this.vjid = vjid == null ? null : vjid.trim();
    }

    public Date getUt() {
        return ut;
    }

    public void setUt(Date ut) {
        this.ut = ut;
    }

    public String getVisitor() {
        return visitor;
    }

    public void setVisitor(String visitor) {
        this.visitor = visitor == null ? null : visitor.trim();
    }
}