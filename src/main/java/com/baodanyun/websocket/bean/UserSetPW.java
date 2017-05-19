package com.baodanyun.websocket.bean;

/**
 * Created by liaowuhen on 2016/11/8.
 */
public class UserSetPW {
    private String cjid; //  string  //客服id
    private String oldPWD; //: string  //旧密码
    private String newPWD; //: string  //新密码
    private String confirmPWD; //: string  //确认密码

    public String getCjid() {
        return cjid;
    }

    public void setCjid(String cjid) {
        this.cjid = cjid;
    }

    public String getOldPWD() {
        return oldPWD;
    }

    public void setOldPWD(String oldPWD) {
        this.oldPWD = oldPWD;
    }

    public String getNewPWD() {
        return newPWD;
    }

    public void setNewPWD(String newPWD) {
        this.newPWD = newPWD;
    }

    public String getConfirmPWD() {
        return confirmPWD;
    }

    public void setConfirmPWD(String confirmPWD) {
        this.confirmPWD = confirmPWD;
    }
}
