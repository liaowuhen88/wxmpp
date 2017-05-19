package com.baodanyun.websocket.service;

import com.baodanyun.websocket.bean.user.AbstractUser;

/**
 * Created by liaowuhen on 2017/4/17.
 */
public interface UserLifeCycleFactoryService {

    UserLifeCycleService getUserLifeCycleService(AbstractUser user);
}
