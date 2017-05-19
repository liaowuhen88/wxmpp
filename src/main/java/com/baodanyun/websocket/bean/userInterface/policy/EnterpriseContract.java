package com.baodanyun.websocket.bean.userInterface.policy;

import java.util.Date;

//只显示备注字段
public class EnterpriseContract {
    private Long id;

    private Long enterpriseid;

    private String name;//合同名称

    private String code;//合同编号

    private Long effectivedate;//生效时间

    private Long expirydate;//过期时间

    private Long signaturedate;

    private Long personnum;

    private Double money;

    private String paytype;

    private Byte status;

    private Long ct;

    private String payment;

    private Integer mianpnum;

    private Integer joinpnum;

    private String followcode;

    private String dispute;

    private String channelname;

    private Long channelid;

    private Long companyid;

    private String bilityAll;

    private Byte isOfficial;

    private Long toCompanyDate;

    private Long brokerageagencyid;

    private Byte customerType;

    private Boolean contactType;

    private Boolean sceneType;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getEnterpriseid() {
        return enterpriseid;
    }

    public void setEnterpriseid(Long enterpriseid) {
        this.enterpriseid = enterpriseid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code == null ? null : code.trim();
    }

    public Long getEffectivedate() {
        return effectivedate;
    }

    public void setEffectivedate(Long effectivedate) {
        this.effectivedate = effectivedate;
    }

    public Long getExpirydate() {
        return expirydate;
    }

    public void setExpirydate(Long expirydate) {
        this.expirydate = expirydate;
    }

    public Long getSignaturedate() {
        return signaturedate;
    }

    public void setSignaturedate(Long signaturedate) {
        this.signaturedate = signaturedate;
    }

    public Long getPersonnum() {
        return personnum;
    }

    public void setPersonnum(Long personnum) {
        this.personnum = personnum;
    }

    public Double getMoney() {
        return money;
    }

    public void setMoney(Double money) {
        this.money = money;
    }

    public String getPaytype() {
        return paytype;
    }

    public void setPaytype(String paytype) {
        this.paytype = paytype == null ? null : paytype.trim();
    }

    public Byte getStatus() {
        return status;
    }

    public void setStatus(Byte status) {
        this.status = status;
    }

    public Long getCt() {
        return ct;
    }

    public void setCt(Long ct) {
        this.ct = ct;
    }

    public String getPayment() {
        return payment;
    }

    public void setPayment(String payment) {
        this.payment = payment == null ? null : payment.trim();
    }

    public Integer getMianpnum() {
        return mianpnum;
    }

    public void setMianpnum(Integer mianpnum) {
        this.mianpnum = mianpnum;
    }

    public Integer getJoinpnum() {
        return joinpnum;
    }

    public void setJoinpnum(Integer joinpnum) {
        this.joinpnum = joinpnum;
    }

    public String getFollowcode() {
        return followcode;
    }

    public void setFollowcode(String followcode) {
        this.followcode = followcode == null ? null : followcode.trim();
    }

    public String getDispute() {
        return dispute;
    }

    public void setDispute(String dispute) {
        this.dispute = dispute == null ? null : dispute.trim();
    }

    public String getChannelname() {
        return channelname;
    }

    public void setChannelname(String channelname) {
        this.channelname = channelname == null ? null : channelname.trim();
    }

    public Long getChannelid() {
        return channelid;
    }

    public void setChannelid(Long channelid) {
        this.channelid = channelid;
    }

    public Long getCompanyid() {
        return companyid;
    }

    public void setCompanyid(Long companyid) {
        this.companyid = companyid;
    }

    public String getBilityAll() {
        return bilityAll;
    }

    public void setBilityAll(String bilityAll) {
        this.bilityAll = bilityAll == null ? null : bilityAll.trim();
    }

    public Byte getIsOfficial() {
        return isOfficial;
    }

    public void setIsOfficial(Byte isOfficial) {
        this.isOfficial = isOfficial;
    }

    public Long getToCompanyDate() {
        return toCompanyDate;
    }

    public void setToCompanyDate(Long toCompanyDate) {
        this.toCompanyDate = toCompanyDate;
    }

    public Long getBrokerageagencyid() {
        return brokerageagencyid;
    }

    public void setBrokerageagencyid(Long brokerageagencyid) {
        this.brokerageagencyid = brokerageagencyid;
    }

    public Byte getCustomerType() {
        return customerType;
    }

    public void setCustomerType(Byte customerType) {
        this.customerType = customerType;
    }

    public Boolean getContactType() {
        return contactType;
    }

    public void setContactType(Boolean contactType) {
        this.contactType = contactType;
    }

    public Boolean getSceneType() {
        return sceneType;
    }

    public void setSceneType(Boolean sceneType) {
        this.sceneType = sceneType;
    }
}