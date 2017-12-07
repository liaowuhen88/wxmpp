package com.baodanyun.websocket.service.dubbo.bean;

import java.io.Serializable;

/**
 * <p>
 * 消息明细表-zrm
 * </p>
 *
 * @author zrm
 * @since 2017-12-01
 */
public class Message implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 消息主键
     */
    private Long id;

    /**
     * 父ID
     */
    private Long pid;

    /**
     * 任务ID
     */
    private String taskid;

    /**
     * 接收者ID
     */
    private Long receiverid;

    /**
     * 接收者
     */
    private String receiver;

    /**
     * 手机号
     */
    private String mobile;

    /**
     * 邮箱
     */
    private String email;

    /**
     * 发送者ID
     */
    private Long senderid;

    /**
     * 发送者
     */
    private String sender;

    /**
     * 内容
     */
    private String content;

    /**
     * 0未知|1sms|2email|3站内|4微信
     */
    private Integer type;

    /**
     * 类型
     */
    private Integer kind;

    /**
     * 是否重发,为空则没有重发
     */
    private Integer isretry;

    /**
     * 发送次数
     */
    private Integer count;

    /**
     * 提交时间
     */
    private Long submittime;

    /**
     * 最后更新时间
     */
    private Long updatetime;

    /**
     * -1失败0待处理1处理中2成功
     */
    private Integer status;

    /**
     * 邮件主题
     */
    private String title;

    /**
     * 状态码
     */
    private String statuscode;

    /**
     * 备注
     */
    private String remark;

    /**
     * 模板ID
     */
    private Long tplid;

    /**
     * 模板编码
     */
    private String tplcode;

    /**
     * 业务ID
     */
    private Integer bizid;

    /**
     * 合同ID
     */
    private Long contractid;

    /**
     * 活动ID
     */
    private Integer activityid;

    /**
     * 发送渠道
     */
    private String channel;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getPid() {
        return pid;
    }

    public void setPid(Long pid) {
        this.pid = pid;
    }

    public String getTaskid() {
        return taskid;
    }

    public void setTaskid(String taskid) {
        this.taskid = taskid;
    }

    public Long getReceiverid() {
        return receiverid;
    }

    public void setReceiverid(Long receiverid) {
        this.receiverid = receiverid;
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Long getSenderid() {
        return senderid;
    }

    public void setSenderid(Long senderid) {
        this.senderid = senderid;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Integer getKind() {
        return kind;
    }

    public void setKind(Integer kind) {
        this.kind = kind;
    }

    public Integer getIsretry() {
        return isretry;
    }

    public void setIsretry(Integer isretry) {
        this.isretry = isretry;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public Long getSubmittime() {
        return submittime;
    }

    public void setSubmittime(Long submittime) {
        this.submittime = submittime;
    }

    public Long getUpdatetime() {
        return updatetime;
    }

    public void setUpdatetime(Long updatetime) {
        this.updatetime = updatetime;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getStatuscode() {
        return statuscode;
    }

    public void setStatuscode(String statuscode) {
        this.statuscode = statuscode;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Long getTplid() {
        return tplid;
    }

    public void setTplid(Long tplid) {
        this.tplid = tplid;
    }

    public String getTplcode() {
        return tplcode;
    }

    public void setTplcode(String tplcode) {
        this.tplcode = tplcode;
    }

    public Integer getBizid() {
        return bizid;
    }

    public void setBizid(Integer bizid) {
        this.bizid = bizid;
    }

    public Long getContractid() {
        return contractid;
    }

    public void setContractid(Long contractid) {
        this.contractid = contractid;
    }

    public Integer getActivityid() {
        return activityid;
    }

    public void setActivityid(Integer activityid) {
        this.activityid = activityid;
    }

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

}
