package com.baodanyun.websocket.bean.userInterface.policy;


/**
 * Created by yangzai-lenovo on 2016/11/14.
 */
public class EnterpriseContractByUidBean {
    private EnterpriseContract enterpriseContract;
    private String insureDay;
    private Long contractId;

    public String getInsureDay() {
        return insureDay;
    }

    public void setInsureDay(String insureDay) {
        this.insureDay = insureDay;
    }

    public EnterpriseContract getEnterpriseContract() {
        return enterpriseContract;
    }

    public void setEnterpriseContract(EnterpriseContract enterpriseContract) {
        this.enterpriseContract = enterpriseContract;
    }

    public Long getContractId() {
        return contractId;
    }

    public void setContractId(Long contractId) {
        this.contractId = contractId;
    }
}
