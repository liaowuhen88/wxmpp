package com.baodanyun.websocket.bean.user;

import com.baodanyun.websocket.bean.Tags;

import java.io.Serializable;
import java.util.List;

/**
 * Created by yutao on 2016/7/12.
 */
public class VCardUser implements Serializable {
    public List<Tags> tags;
    /**
     * 因为xmpp发送消息的时候是带后缀，id代表一个xmpp唯一发送地址
     */
    private String openId;
    private Long uid;//对应的豆包系统内的用户id
    private String dbName;//对应豆包系统内的用户姓名
    private String icon;
    private String nickName;
    private String userName;// 真实姓名
    private String loginUsername;// 登录用户名
    private boolean vip;//是否时vip vip将不进行排队
    private String desc;  // 详细信息
    private String remark;  // 备注信息
    private String workNumber;

    public List<Tags> getTags() {
        return tags;
    }

    public void setTags(List<Tags> tags) {
        this.tags = tags;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getWorkNumber() {
        return workNumber;
    }

    public void setWorkNumber(String workNumber) {
        this.workNumber = workNumber;
    }

    public boolean isVip() {
        return vip;
    }

    public void setVip(boolean vip) {
        this.vip = vip;
    }

    public Long getUid() {
        return uid;
    }

    public void setUid(Long uid) {
        this.uid = uid;
    }

    public String getDbName() {
        return dbName;
    }

    public void setDbName(String dbName) {
        this.dbName = dbName;
    }

    public String getOpenId() {
        return openId;
    }

    public void setOpenId(String openId) {
        this.openId = openId;
    }

    public String getLoginUsername() {
        return loginUsername;
    }

    public void setLoginUsername(String loginUsername) {
        this.loginUsername = loginUsername;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

}
