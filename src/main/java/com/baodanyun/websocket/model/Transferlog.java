package com.baodanyun.websocket.model;

import java.util.Date;

public class Transferlog {
    private Integer id;

    private String transferfrom;

    private String transferto;

    private Date ct;

    private String cause;

    private String visitorjid;

    private Boolean status = false;

    private String detail;

    private boolean mustDo = false;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTransferfrom() {
        return transferfrom;
    }

    public void setTransferfrom(String transferfrom) {
        this.transferfrom = transferfrom == null ? null : transferfrom.trim();
    }

    public String getTransferto() {
        return transferto;
    }

    public void setTransferto(String transferto) {
        this.transferto = transferto == null ? null : transferto.trim();
    }

    public Date getCt() {
        return ct;
    }

    public void setCt(Date ct) {
        this.ct = ct;
    }

    public String getCause() {
        return cause;
    }

    public void setCause(String cause) {
        this.cause = cause == null ? null : cause.trim();
    }

    public String getVisitorjid() {
        return visitorjid;
    }

    public void setVisitorjid(String visitorjid) {
        this.visitorjid = visitorjid == null ? null : visitorjid.trim();
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail == null ? null : detail.trim();
    }

    public boolean isMustDo() {
        return mustDo;
    }

    public void setMustDo(boolean mustDo) {
        this.mustDo = mustDo;
    }
}