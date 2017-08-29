package com.baodanyun.robot.dto;

import java.io.Serializable;

/**
 * 统计5分钟15分钟30分钟的客服告警次数
 *
 * @author hubo
 * @since 2017-8-28 12:22:22
 */
public class AlarmStatisticsDto implements Serializable {

    private String customerName;

    private String beginDate;

    private String endDate;

    /*5分钟次数*/
    private Integer msgFive;

    /*15分钟次数*/
    private Integer msgFifth;

    /*30分钟次数*/
    private Integer msgThirty;

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getBeginDate() {
        return beginDate;
    }

    public void setBeginDate(String beginDate) {
        this.beginDate = beginDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public Integer getMsgFive() {
        return msgFive;
    }

    public void setMsgFive(Integer msgFive) {
        this.msgFive = msgFive;
    }

    public Integer getMsgFifth() {
        return msgFifth;
    }

    public void setMsgFifth(Integer msgFifth) {
        this.msgFifth = msgFifth;
    }

    public Integer getMsgThirty() {
        return msgThirty;
    }

    public void setMsgThirty(Integer msgThirty) {
        this.msgThirty = msgThirty;
    }
}
