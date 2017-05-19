package com.baodanyun.websocket.bean;

import java.io.Serializable;

/**
 * Created by liaowuhen on 2017/2/22.
 */
public class UserEvent implements Serializable {

    /**
     * 事件类型id, 例如“020401”
     */
    private String evt;
    /*
    时间戳，精确到ms
     */
    private Long t;

    /**
     * 事件的对象类型，类型定义见：
     */
    private Integer otype;

    /**
     * 事件的对象id，如商品id或客服系统会话sessionid
     */
    private String oid;

    /**
     * 备注，如用户输入的信息等
     */
    private String m;


    public String getEvt() {
        return evt;
    }

    public void setEvt(String evt) {
        this.evt = evt;
    }

    public Long getT() {
        return t;
    }

    public void setT(Long t) {
        this.t = t;
    }

    public Integer getOtype() {
        return otype;
    }

    public void setOtype(Integer otype) {
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
