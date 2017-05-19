package com.baodanyun.websocket.bean.user;

import com.baodanyun.websocket.bean.user.AbstractUser;
import org.apache.commons.lang.StringUtils;

import java.io.Serializable;

/**
 * Created by yutao on 2016/7/12.
 * 客服应该是有别于其他用户的
 */
public class Customer extends AbstractUser implements Serializable {


    public Customer() {
        setUserType(UserType.customer);
    }
}
