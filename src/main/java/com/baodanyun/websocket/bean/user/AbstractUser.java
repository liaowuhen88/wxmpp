package com.baodanyun.websocket.bean.user;

import java.io.Serializable;

/**
 * Created by yutao on 2016/7/12.
 */
public class AbstractUser implements Serializable {

    public UserType userType;
    /**
     * 因为xmpp发送消息的时候是带后缀，id代表一个xmpp唯一发送地址
     */

    private String id;
    private String openId;
    private Long uid;//对应的豆包系统内的用户id
    private String dbName;//对应豆包系统内的用户姓名
    private String icon;
    private String nickName;
    private String userName;// 真实姓名
    private String loginUsername;// 登录用户名
    private transient String passWord;
    private Long loginTime;//登录时间
    private Long logoutTime;//离线时间
    private boolean vip;//是否时vip vip将不进行排队
    private String desc;  // 详细信息
    private String remark;  // 备注信息
    private String workNumber;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public UserType getUserType() {
        return userType;
    }

    public void setUserType(UserType userType) {
        this.userType = userType;
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

    public String getPassWord() {
        return passWord;
    }

    public void setPassWord(String passWord) {
        this.passWord = passWord;
    }

    public String getWorkNumber() {
        return workNumber;
    }

    public void setWorkNumber(String workNumber) {
        this.workNumber = workNumber;
    }

    @Override
    public boolean equals(Object o) {
        //TODO 重写了equals  但是没有重写hashCode
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        AbstractUser that = (AbstractUser) o;

        return id != null ? id.equals(that.id) : that.id == null;

    }

    @Override
    public int hashCode() {
        return this.getId().hashCode();
    }

    public Long getLoginTime() {
        return loginTime;
    }

    public void setLoginTime(Long loginTime) {
        this.loginTime = loginTime;
    }

    public Long getLogoutTime() {
        return logoutTime;
    }

    public void setLogoutTime(Long logoutTime) {
        this.logoutTime = logoutTime;
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

    public enum UserType {
        visitor, customer
    }
}
