package com.baodanyun.admin.dto;

import java.io.Serializable;

/**
 * 客户配置搜索条件dto
 */
public class CustomerSearchDto implements Serializable {

    /**
     * 状态
     */
    private int state;

    /**
     * 批次号
     */
    private String serialNo;

    /**
     * 创建开始时间
     */
    private String beginTime;

    /**
     * 创建结束时间
     */
    private String endTime;

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public String getSerialNo() {
        return serialNo;
    }

    public void setSerialNo(String serialNo) {
        this.serialNo = serialNo;
    }

    public String getBeginTime() {
        return beginTime;
    }

    public void setBeginTime(String beginTime) {
        this.beginTime = beginTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }
}
