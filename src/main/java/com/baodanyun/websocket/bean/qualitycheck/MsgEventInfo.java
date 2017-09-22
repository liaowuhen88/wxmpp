package com.baodanyun.websocket.bean.qualitycheck;

import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;


@Document
public class MsgEventInfo implements Serializable {
    private String t;

    private String evt;

    private String otype;

    private String oid;

    private String m;

    public String getEvt() {
        return evt;
    }

    public void setEvt(String evt) {
        this.evt = evt;
    }

    public String getT() {
        return t;
    }

    public void setT(String t) {
        this.t = t;
    }

    public String getOtype() {
        return otype;
    }

    public void setOtype(String otype) {
        this.otype = otype;
    }

    public String getOid() {
        return oid;
    }

    public void setOid(String oid) {
        this.oid = oid;
    }

    public String getM() {
        return m;
    }

    public void setM(String m) {
        this.m = m;
    }
}
