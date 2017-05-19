package com.baodanyun.websocket.bean.userInterface.order;

import java.util.Date;

public class OrderInfo {
    private Long oid;//订单id

    private Long uid;//用户uid

    private Long userInfoId;//用户id

    private String name;//订单姓名

    private String caseName;//套餐名称

    private Long status;//套餐状态(1有效，-1无效)

    private String statusName;//套餐状态(1有效，-1无效)中文转换

    private Long startTime;//套餐有效开始日期

    private Long endTime;//套餐有效结束日期

    private Long saleModel;//销售模式 1线下销售2线上销售

    private String saleModelName;//1线下销售2线上销售 对应的中文

    private Long orderTime;//预约时间

    private String frontName;//预约门店

    private Long checkTime;//体检时间

    private String tjStoreName;//体检门店
    /********************预约字段******************/
    private Long pid;//套餐id

    private Long isMarried;//适用婚否(0-未婚1-已婚null-通用)

    private Long gender;//适用性别(0-女1-男null-通用)

    private Long frontId;//门店ID

    private String idCard;//订单身份证

    private Long isBook;//是否预约(1:预约0没预约)

    private Long isCheck;//是否到检(1到检0没有到检)

    private String isCheckName;//体检信息

    public String getIsCheckName() {
        return isCheckName;
    }

    public void setIsCheckName(String isCheckName) {
        this.isCheckName = isCheckName;
    }

    public Long getOid() {
        return oid;
    }

    public void setOid(Long oid) {
        this.oid = oid;
    }

    public Long getUid() {
        return uid;
    }

    public void setUid(Long uid) {
        this.uid = uid;
    }

    public Long getUserInfoId() {
        return userInfoId;
    }

    public void setUserInfoId(Long userInfoId) {
        this.userInfoId = userInfoId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCaseName() {
        return caseName;
    }

    public void setCaseName(String caseName) {
        this.caseName = caseName;
    }

    public Long getStatus() {
        return status;
    }

    public void setStatus(Long status) {
        this.status = status;
    }

    public Long getStartTime() {
        return startTime;
    }

    public void setStartTime(Long startTime) {
        this.startTime = startTime;
    }

    public Long getEndTime() {
        return endTime;
    }

    public void setEndTime(Long endTime) {
        this.endTime = endTime;
    }

    public Long getSaleModel() {
        return saleModel;
    }

    public void setSaleModel(Long saleModel) {
        this.saleModel = saleModel;
    }

    public Long getOrderTime() {
        return orderTime;
    }

    public void setOrderTime(Long orderTime) {
        this.orderTime = orderTime;
    }

    public String getFrontName() {
        return frontName;
    }

    public void setFrontName(String frontName) {
        this.frontName = frontName;
    }

    public Long getCheckTime() {
        return checkTime;
    }

    public void setCheckTime(Long checkTime) {
        this.checkTime = checkTime;
    }

    public String getTjStoreName() {
        return tjStoreName;
    }

    public void setTjStoreName(String tjStoreName) {
        this.tjStoreName = tjStoreName;
    }

    public Long getPid() {
        return pid;
    }

    public void setPid(Long pid) {
        this.pid = pid;
    }

    public Long getIsMarried() {
        return isMarried;
    }

    public void setIsMarried(Long isMarried) {
        this.isMarried = isMarried;
    }

    public Long getGender() {
        return gender;
    }

    public void setGender(Long gender) {
        this.gender = gender;
    }

    public Long getFrontId() {
        return frontId;
    }

    public void setFrontId(Long frontId) {
        this.frontId = frontId;
    }

    public String getIdCard() {
        return idCard;
    }

    public void setIdCard(String idCard) {
        this.idCard = idCard;
    }

    public Long getIsBook() {
        return isBook;
    }

    public void setIsBook(Long isBook) {
        this.isBook = isBook;
    }

    public Long getIsCheck() {
        return isCheck;
    }

    public void setIsCheck(Long isCheck) {
        this.isCheck = isCheck;
    }

    public String getStatusName() {
        return statusName;
    }

    public void setStatusName(String statusName) {
        this.statusName = statusName;
    }

    public String getSaleModelName() {
        return saleModelName;
    }

    public void setSaleModelName(String saleModelName) {
        this.saleModelName = saleModelName;
    }

    @Override
    public String toString() {
        return "OrderInfo{" +
                "oid=" + oid +
                ", uid=" + uid +
                ", userInfoId=" + userInfoId +
                ", name='" + name + '\'' +
                ", caseName='" + caseName + '\'' +
                ", status=" + status +
                ", statusName='" + statusName + '\'' +
                ", startTime=" + startTime +
                ", endTime=" + endTime +
                ", saleModel=" + saleModel +
                ", saleModelName='" + saleModelName + '\'' +
                ", orderTime=" + orderTime +
                ", frontName='" + frontName + '\'' +
                ", checkTime=" + checkTime +
                ", tjStoreName='" + tjStoreName + '\'' +
                ", pid=" + pid +
                ", isMarried=" + isMarried +
                ", gender=" + gender +
                ", frontId=" + frontId +
                ", idCard='" + idCard + '\'' +
                ", isBook=" + isBook +
                ", isCheck=" + isCheck +
                ", isCheckName='" + isCheckName + '\'' +
                '}';
    }
}
