package com.baodanyun.websocket.bean;

import java.sql.Time;
import java.sql.Timestamp;

/**
 * Created by liaowuhen on 2016/11/15.
 */
public class Message {

    public static final Integer SUB = 1;
    public static final Integer DEAL = 2;

    private Integer id;
    private String openId; // 微信唯一标识
    private String cid; // 客服id
    private String username; // 留言人
    private String content;  // 留言内容
    private String phone;    // 电话
    private Integer status;  // 状态
    private String jid;  // 留言到客服的id
    private String dealResult; // 处理结果
    private Timestamp ut;
    private Timestamp ct;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getOpenId() {
        return openId;
    }

    public void setOpenId(String openId) {
        this.openId = openId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }


    public String getDealResult() {
        return dealResult;
    }

    public void setDealResult(String dealResult) {
        this.dealResult = dealResult;
    }

    public String getJid() {
        return jid;
    }

    public void setJid(String jid) {
        this.jid = jid;
    }

    public String getCid() {
        return cid;
    }

    public void setCid(String cid) {
        this.cid = cid;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }


    public Timestamp getUt() {
        return ut;
    }

    public void setUt(Timestamp ut) {
        this.ut = ut;
    }

    public Timestamp getCt() {
        return ct;
    }

    public void setCt(Timestamp ct) {
        this.ct = ct;
    }
}
