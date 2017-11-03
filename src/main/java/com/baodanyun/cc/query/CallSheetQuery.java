package com.baodanyun.cc.query;

/**
 * Created by liaowuhen on 2017/10/31.
 */
public class CallSheetQuery {

    //字段	类型	是否必须	备注
    private String beginTime;  // 是	获取时间段的开始时间，时间格式为 yyyy-­‐MM-­‐dd HH:mm:ss
    private String endTime;  //	是	获取时间段的结束时间，时间格式为 yyyy-­‐MM-­‐dd HH:mm:ss
    private String callNo;  //	否	主叫号码
    private String calledNo;  //	否	被叫号码
    private String connectType;  //	否	呼叫类型（值可以是normal（普通来电）、 dialout（外呼去电）、transfer（来电转接）、dialTransfer（外呼转接）
    private String status;  //	否	处理状态（值可以是dealing（已接听）、 notDeal（振铃未接听）、queueLeak（排队放弃）、voicemail （已留言）、leak（IVR放弃） 、blackList（黑名单））
    private String cdrVar;  //	否	如果webcall接口传了 cdrVar，那么这里可以传变量的名字，来获 取值。（若传了该值，则时间条件可以不传）
    private String resultParams;  //	否	查询参数，多个参数用","号隔开。如果仅仅希望获取某些有用参数可以用这个字段，如传参数FILE_SERVER,RECORD_FILE_NAME就查询所有通话记录的录音地址

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

    public String getCallNo() {
        return callNo;
    }

    public void setCallNo(String callNo) {
        this.callNo = callNo;
    }

    public String getCalledNo() {
        return calledNo;
    }

    public void setCalledNo(String calledNo) {
        this.calledNo = calledNo;
    }

    public String getConnectType() {
        return connectType;
    }

    public void setConnectType(String connectType) {
        this.connectType = connectType;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCdrVar() {
        return cdrVar;
    }

    public void setCdrVar(String cdrVar) {
        this.cdrVar = cdrVar;
    }

    public String getResultParams() {
        return resultParams;
    }

    public void setResultParams(String resultParams) {
        this.resultParams = resultParams;
    }
}
