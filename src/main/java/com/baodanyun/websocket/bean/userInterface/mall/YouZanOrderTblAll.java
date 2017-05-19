package com.baodanyun.websocket.bean.userInterface.mall;

import java.util.Date;

public class YouZanOrderTblAll {
    private Long id;

    private Long uid;

    private String oderCode;

    private String commodityTitle;

    private Byte oderType;

    private Integer commodityNumber;

    private Double orderPrice;

    private Long createTime;

    private Long payTime;

    private String tid;

    private String status;

    private Double discountfee;

    private String openid;

    private String numiid;

    private String tjName;

    private String mobile;

    private String idcard;

    private String sex;

    private String wxNc;

    private String lishuNo;

    private String loginMobile;

    private Long ct;

    private Long ut;

    private String sku;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUid() {
        return uid;
    }

    public void setUid(Long uid) {
        this.uid = uid;
    }

    public String getOderCode() {
        return oderCode;
    }

    public void setOderCode(String oderCode) {
        this.oderCode = oderCode == null ? null : oderCode.trim();
    }

    public String getCommodityTitle() {
        return commodityTitle;
    }

    public void setCommodityTitle(String commodityTitle) {
        this.commodityTitle = commodityTitle == null ? null : commodityTitle.trim();
    }

    public Byte getOderType() {
        return oderType;
    }

    public void setOderType(Byte oderType) {
        this.oderType = oderType;
    }

    public Integer getCommodityNumber() {
        return commodityNumber;
    }

    public void setCommodityNumber(Integer commodityNumber) {
        this.commodityNumber = commodityNumber;
    }

    public Double getOrderPrice() {
        return orderPrice;
    }

    public void setOrderPrice(Double orderPrice) {
        this.orderPrice = orderPrice;
    }

    public Long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Long createTime) {
        this.createTime = createTime;
    }

    public Long getPayTime() {
        return payTime;
    }

    public void setPayTime(Long payTime) {
        this.payTime = payTime;
    }

    public String getTid() {
        return tid;
    }

    public void setTid(String tid) {
        this.tid = tid == null ? null : tid.trim();
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status == null ? null : status.trim();
    }

    public Double getDiscountfee() {
        return discountfee;
    }

    public void setDiscountfee(Double discountfee) {
        this.discountfee = discountfee;
    }

    public String getOpenid() {
        return openid;
    }

    public void setOpenid(String openid) {
        this.openid = openid == null ? null : openid.trim();
    }

    public String getNumiid() {
        return numiid;
    }

    public void setNumiid(String numiid) {
        this.numiid = numiid == null ? null : numiid.trim();
    }

    public String getTjName() {
        return tjName;
    }

    public void setTjName(String tjName) {
        this.tjName = tjName == null ? null : tjName.trim();
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile == null ? null : mobile.trim();
    }

    public String getIdcard() {
        return idcard;
    }

    public void setIdcard(String idcard) {
        this.idcard = idcard == null ? null : idcard.trim();
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex == null ? null : sex.trim();
    }

    public String getWxNc() {
        return wxNc;
    }

    public void setWxNc(String wxNc) {
        this.wxNc = wxNc == null ? null : wxNc.trim();
    }

    public String getLishuNo() {
        return lishuNo;
    }

    public void setLishuNo(String lishuNo) {
        this.lishuNo = lishuNo == null ? null : lishuNo.trim();
    }

    public String getLoginMobile() {
        return loginMobile;
    }

    public void setLoginMobile(String loginMobile) {
        this.loginMobile = loginMobile == null ? null : loginMobile.trim();
    }

    public Long getCt() {
        return ct;
    }

    public void setCt(Long ct) {
        this.ct = ct;
    }

    public Long getUt() {
        return ut;
    }

    public void setUt(Long ut) {
        this.ut = ut;
    }

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }
}