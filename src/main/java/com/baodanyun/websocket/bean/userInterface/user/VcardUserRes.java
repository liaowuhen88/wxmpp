package com.baodanyun.websocket.bean.userInterface.user;

import com.baodanyun.websocket.bean.user.AbstractUser;

/**
 * Created by liaowuhen on 2017/3/31.
 */
public class VcardUserRes {
    private AbstractUser user;
    private AbstractUser vcard;

    private int isAdmin;

    public AbstractUser getUser() {
        return user;
    }

    public void setUser(AbstractUser user) {
        this.user = user;
    }

    public AbstractUser getVcard() {
        return vcard;
    }

    public void setVcard(AbstractUser vcard) {
        this.vcard = vcard;
    }

    public int getIsAdmin() {
        return isAdmin;
    }

    public void setIsAdmin(int isAdmin) {
        this.isAdmin = isAdmin;
    }
}
