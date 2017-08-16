package com.baodanyun.websocket.bean.userInterface.claims;

import java.util.Date;

public class ClaimsInfo {
    private String applycode;//理赔单号
    private String insuranceCompany;//承保公司
    private String applyPerson;//申请人姓名
    private String dangerPerson;//受益人姓名
    private String dangerIdCard;//受益人证件号
    private Long applyDate;//申请时间
    private Long doneTime;//完结时间
    private Double chargeMoney;//申请金额
    private Byte claimsStatus;//理赔状态
    private String handleStatus;//处理状态
    private Long uid;

    private Integer processType;

    private Long applyId;

    public Long getUid() {
        return uid;
    }

    public void setUid(Long uid) {
        this.uid = uid;
    }

    public String getApplycode() {
        return applycode;
    }

    public void setApplycode(String applycode) {
        this.applycode = applycode;
    }

    public String getInsuranceCompany() {
        return insuranceCompany;
    }

    public void setInsuranceCompany(String insuranceCompany) {
        this.insuranceCompany = insuranceCompany;
    }

    public String getApplyPerson() {
        return applyPerson;
    }

    public void setApplyPerson(String applyPerson) {
        this.applyPerson = applyPerson;
    }

    public String getDangerPerson() {
        return dangerPerson;
    }

    public void setDangerPerson(String dangerPerson) {
        this.dangerPerson = dangerPerson;
    }

    public String getDangerIdCard() {
        return dangerIdCard;
    }

    public void setDangerIdCard(String dangerIdCard) {
        this.dangerIdCard = dangerIdCard;
    }

    public Long getApplyDate() {
        return applyDate;
    }

    public void setApplyDate(Long applyDate) {
        this.applyDate = applyDate;
    }

    public Long getDoneTime() {
        return doneTime;
    }

    public void setDoneTime(Long doneTime) {
        this.doneTime = doneTime;
    }

    public Double getChargeMoney() {
        return chargeMoney;
    }

    public void setChargeMoney(Double chargeMoney) {
        this.chargeMoney = chargeMoney;
    }

    public Byte getClaimsStatus() {
        return claimsStatus;
    }

    public void setClaimsStatus(Byte claimsStatus) {
        this.claimsStatus = claimsStatus;
    }

    public String getHandleStatus() {
        return handleStatus;
    }

    public void setHandleStatus(String handleStatus) {
        this.handleStatus = handleStatus;
    }

    public Integer getProcessType() {
        return processType;
    }

    public void setProcessType(Integer processType) {
        this.processType = processType;
    }

    public Long getApplyId() {
        return applyId;
    }

    public void setApplyId(Long applyId) {
        this.applyId = applyId;
    }

    @Override
    public String toString() {
        return "ClaimsInfo{" +
                "applycode='" + applycode + '\'' +
                ", insuranceCompany='" + insuranceCompany + '\'' +
                ", applyPerson='" + applyPerson + '\'' +
                ", dangerPerson='" + dangerPerson + '\'' +
                ", dangerIdCard='" + dangerIdCard + '\'' +
                ", applyDate=" + applyDate +
                ", doneTime=" + doneTime +
                ", chargeMoney=" + chargeMoney +
                ", claimsStatus=" + claimsStatus +
                ", handleStatus='" + handleStatus + '\'' +
                ", uid=" + uid +
                '}';
    }
}
