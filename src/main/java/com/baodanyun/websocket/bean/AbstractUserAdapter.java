package com.baodanyun.websocket.bean;

import com.baodanyun.websocket.bean.user.AbstractUser;

/**
 * Created by liaowuhen on 2017/5/16.
 */
public class AbstractUserAdapter {

    private AbstractUser login;
    private AbstractUser customer;

    public AbstractUserAdapter(AbstractUser login, AbstractUser customer) {
        this.login = login;
        this.customer = customer;
    }

    public AbstractUser getLogin() {
        return login;
    }

    public void setLogin(AbstractUser login) {
        this.login = login;
    }

    public AbstractUser getCustomer() {
        return customer;
    }

    public void setCustomer(AbstractUser customer) {
        this.customer = customer;
    }
}
