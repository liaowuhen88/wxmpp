package com.baodanyun.websocket.bean.userInterface.user;

import com.baodanyun.websocket.bean.user.AbstractUser;
import com.baodanyun.websocket.bean.user.VCardUser;

/**
 * Created by liaowuhen on 2017/3/31.
 */
public class VcardUserRes {
    private AbstractUser user;
    private VCardUser vcard;

    public AbstractUser getUser() {
        return user;
    }

    public void setUser(AbstractUser user) {
        this.user = user;
    }

    public VCardUser getVcard() {
        return vcard;
    }

    public void setVcard(VCardUser vcard) {
        this.vcard = vcard;
    }
}
