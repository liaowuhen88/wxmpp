package com.baodanyun.websocket.model;

import com.mysql.jdbc.StringUtils;

/**
 * Created by liaowuhen on 2016/11/2.
 */
public class UserModel {
    private String cjid; // cjid
    private String icon;//  头像
    private String nickName; // 昵称
    private String userName;
    private String desc;//描述--%>
    private String tags;  // 多个标签,分割
    private String remark; // 备注信息

    public String getCjid() {
        return cjid;
    }

    public void setCjid(String cjid) {
        this.cjid = cjid;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }


    public String getDesc() {
        return desc;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String check() {
        if (StringUtils.isNullOrEmpty(this.getCjid())) {
            return "cjid 参数不能为空";
        }
        return null;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
