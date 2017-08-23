package com.baodanyun.websocket.bean.userInterface;

import com.baodanyun.websocket.bean.userInterface.card.PcontractBaseMessDto;
import com.baodanyun.websocket.bean.userInterface.claims.ClaimsInfo;
import com.baodanyun.websocket.bean.userInterface.mall.YouZanOrderTblAll;
import com.baodanyun.websocket.bean.userInterface.order.OrderInfo;
import com.baodanyun.websocket.bean.userInterface.policy.EnterpriseContractByUidBean;
import com.baodanyun.websocket.bean.userInterface.user.PersonalInfo;
import com.google.gson.JsonObject;
import java.util.List;

public class PersonalDetail {
    private PersonalInfo personalInfo;
    private List<Company> company;
    private JsonObject personalUserAccount;
    private List<PersonalInfo> personalInfos;//个人信息
    private List<ClaimsInfo> claimsInfos;//理赔
    private List<OrderInfo> orderInfos;//体检订单
    private List<EnterpriseContractByUidBean> contractInfos;//合同订单
    private List<YouZanOrderTblAll> youzanOrderAll;//mall订单
    private List<PcontractBaseMessDto> cardList; //卡单

    public List<PersonalInfo> getPersonalInfos() {
        return personalInfos;
    }

    public void setPersonalInfos(List<PersonalInfo> personalInfos) {
        this.personalInfos = personalInfos;
    }

    public List<ClaimsInfo> getClaimsInfos() {
        return claimsInfos;
    }

    public void setClaimsInfos(List<ClaimsInfo> claimsInfos) {
        this.claimsInfos = claimsInfos;
    }

    public List<OrderInfo> getOrderInfos() {
        return orderInfos;
    }

    public void setOrderInfos(List<OrderInfo> orderInfos) {
        this.orderInfos = orderInfos;
    }


    public PersonalInfo getPersonalInfo() {
        return personalInfo;
    }

    public void setPersonalInfo(PersonalInfo personalInfo) {
        this.personalInfo = personalInfo;
    }

    public List<EnterpriseContractByUidBean> getContractInfos() {
        return contractInfos;
    }

    public void setContractInfos(List<EnterpriseContractByUidBean> contractInfos) {
        this.contractInfos = contractInfos;
    }

    public List<YouZanOrderTblAll> getYouzanOrderAll() {
        return youzanOrderAll;
    }

    public void setYouzanOrderAll(List<YouZanOrderTblAll> youzanOrderAll) {
        this.youzanOrderAll = youzanOrderAll;
    }

    public JsonObject getPersonalUserAccount() {
        return personalUserAccount;
    }

    public void setPersonalUserAccount(JsonObject personalUserAccount) {
        this.personalUserAccount = personalUserAccount;
    }

    public List<Company> getCompany() {
        return company;
    }

    public void setCompany(List<Company> company) {
        this.company = company;
    }

    public List<PcontractBaseMessDto> getCardList() {
        return cardList;
    }

    public void setCardList(List<PcontractBaseMessDto> cardList) {
        this.cardList = cardList;
    }
}
