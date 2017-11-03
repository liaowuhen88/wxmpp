package com.baodanyun.cc.bean;

/**
 * Created by liaowuhen on 2017/10/31.
 */
public class CallSheetEvent {
    private String CallNo;//   主叫号码
    private String CalledNo;//   	被叫号码
    private String CallSheetID;//   	通话记录ID,CallSheetID 是这条通话记录再DB中的唯一id
    private String CallType;//   	通话类型：dialout外呼通话,normal普通来电,transfer转接电话,dialTransfer外呼转接
    private String Ring;//   	通话振铃时间（话务进入呼叫中心系统的时间）
    private String Begin;//   	通话接通时间（呼入是按座席接起的时间,外呼按客户接起的时间,如果没接听的话为空）
    private String End;//   	通话结束时间
    private String QueueTime;//   	来电进入技能组时间
    private String Agent;//   	处理坐席的工号
    private String Exten;//   	处理坐席的工号,历史原因该字段与Agent相同
    private String AgentName;//   	处理坐席的姓名
    private String Queue;//   	通话进入的技能组名称
    private String State;//   	接听状态：dealing（已接）,notDeal（振铃未接听）,leak（ivr放弃）,queueLeak（排队放弃）,blackList（黑名单）,voicemail（留言）
    private String CallState;//   	事件状态：Ring, Ringing, Link, Hangup(Unlink也当成Hangup处理)
    private String ActionID;//   	通过接口调用时,该字段会保存请求的actionID,其它情况下该字段为空
    private String WebcallActionID;//   	通过调用webcall接口,该字段会保存请求的actionID,其它情况下该字段为空
    private String RecordFile;//   	通话录音文件名：用户要访问录音时,在该文件名前面加上服务路径即可,如：FileServer/RecordFile
    private String FileServer;//   	通过FileServer中指定的地址加上RecordFile的值可以获取录音
    private String Province;//   	目标号码的省,例如北京市。呼入为来电号码,呼出为去电号码
    private String District;//   	目标号码的市,例如北京市。呼入为来电号码,呼出为去电号码
    private String CallID;//   	通话ID,通话连接的在系统中的唯一标识。CallID 是在通话进行中channel的id,可以用这个id来挂断通话之类的操作。一个call有一个CallID,但一个call可能会出现在多个通话中,比如转接。
    private String IVRKEY;//   	通话在系统中选择的按键菜单,10004@0。格式为：按键菜单的节点编号@选择的菜单按键。如果为多级菜单则为：10004@0-10005@1。
    private String AccountId;//   	账户编号字段,默认不推送有需求的客户对接时联系七陌技术支持人员进行开通
    private String AccountName;//   	账户名称字段,默认不推送有需求的客户对接时联系七陌技术支持人员进行开通


    public String getCallNo() {
        return CallNo;
    }

    public void setCallNo(String callNo) {
        CallNo = callNo;
    }

    public String getCalledNo() {
        return CalledNo;
    }

    public void setCalledNo(String calledNo) {
        CalledNo = calledNo;
    }

    public String getCallSheetID() {
        return CallSheetID;
    }

    public void setCallSheetID(String callSheetID) {
        CallSheetID = callSheetID;
    }

    public String getCallType() {
        return CallType;
    }

    public void setCallType(String callType) {
        CallType = callType;
    }

    public String getRing() {
        return Ring;
    }

    public void setRing(String ring) {
        Ring = ring;
    }

    public String getBegin() {
        return Begin;
    }

    public void setBegin(String begin) {
        Begin = begin;
    }

    public String getEnd() {
        return End;
    }

    public void setEnd(String end) {
        End = end;
    }

    public String getQueueTime() {
        return QueueTime;
    }

    public void setQueueTime(String queueTime) {
        QueueTime = queueTime;
    }

    public String getAgent() {
        return Agent;
    }

    public void setAgent(String agent) {
        Agent = agent;
    }

    public String getExten() {
        return Exten;
    }

    public void setExten(String exten) {
        Exten = exten;
    }

    public String getAgentName() {
        return AgentName;
    }

    public void setAgentName(String agentName) {
        AgentName = agentName;
    }

    public String getQueue() {
        return Queue;
    }

    public void setQueue(String queue) {
        Queue = queue;
    }

    public String getState() {
        return State;
    }

    public void setState(String state) {
        State = state;
    }

    public String getCallState() {
        return CallState;
    }

    public void setCallState(String callState) {
        CallState = callState;
    }

    public String getActionID() {
        return ActionID;
    }

    public void setActionID(String actionID) {
        ActionID = actionID;
    }

    public String getWebcallActionID() {
        return WebcallActionID;
    }

    public void setWebcallActionID(String webcallActionID) {
        WebcallActionID = webcallActionID;
    }

    public String getRecordFile() {
        return RecordFile;
    }

    public void setRecordFile(String recordFile) {
        RecordFile = recordFile;
    }

    public String getFileServer() {
        return FileServer;
    }

    public void setFileServer(String fileServer) {
        FileServer = fileServer;
    }

    public String getProvince() {
        return Province;
    }

    public void setProvince(String province) {
        Province = province;
    }

    public String getDistrict() {
        return District;
    }

    public void setDistrict(String district) {
        District = district;
    }

    public String getCallID() {
        return CallID;
    }

    public void setCallID(String callID) {
        CallID = callID;
    }

    public String getIVRKEY() {
        return IVRKEY;
    }

    public void setIVRKEY(String IVRKEY) {
        this.IVRKEY = IVRKEY;
    }

    public String getAccountId() {
        return AccountId;
    }

    public void setAccountId(String accountId) {
        AccountId = accountId;
    }

    public String getAccountName() {
        return AccountName;
    }

    public void setAccountName(String accountName) {
        AccountName = accountName;
    }

    public String getDownUrl() {
        return getFileServer() + "/" + getRecordFile();
    }
}
