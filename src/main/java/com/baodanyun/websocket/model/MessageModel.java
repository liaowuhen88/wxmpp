package com.baodanyun.websocket.model;

import org.apache.commons.lang.StringUtils;

import java.sql.Date;

/**
 * Created by liaowuhen on 2016/11/15.
 */
public class MessageModel extends PageModel {

    private Integer id;
    private String openId; // 微信唯一标识
    private String cid; // 客服id
    private String username; // 留言人
    private String content;  // 留言内容
    private String phone;    // 电话
    private Integer status;  // 状态
    private String jid;  // 留言到客服的id
    private String dealResult; // 处理结果

    public String getOpenId() {
        return openId;
    }

    public void setOpenId(String openId) {
        this.openId = openId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }


    public String getDealResult() {
        return dealResult;
    }

    public void setDealResult(String dealResult) {
        this.dealResult = dealResult;
    }

    public String getJid() {
        return jid;
    }

    public void setJid(String jid) {
        this.jid = jid;
    }

    public String getCid() {
        return cid;
    }

    public void setCid(String cid) {
        this.cid = cid;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String addCheck() {
        if (StringUtils.isEmpty(this.getContent())) {
            return "提交内容[content]不能为空";

        }
        if (StringUtils.isEmpty(this.getCid())) {
            return "客服[cid]不能为空";

        }

        if (StringUtils.isEmpty(this.getPhone())) {
            return "手机[phone]不能为空";

        }

        if (StringUtils.isEmpty(this.getUsername())) {
            return "用户[username]不能为空";
        }

        return null;
    }

}
