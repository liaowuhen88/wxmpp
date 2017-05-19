package com.baodanyun.websocket.service.impl;

import com.baodanyun.websocket.bean.user.AbstractUser;
import com.baodanyun.websocket.service.UserLifeCycleFactoryService;
import com.baodanyun.websocket.service.UserLifeCycleService;
import com.baodanyun.websocket.service.WebSocketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

/**
 * Created by liaowuhen on 2017/4/17.
 */

@Service
public class UserLifeCycleFactoryServiceImpl implements UserLifeCycleFactoryService {

    @Autowired
    public WebSocketService webSocketService;
    @Autowired
    @Qualifier("wvUserLifeCycleService")
    private UserLifeCycleService wvUserLifeCycleService;
    @Autowired
    @Qualifier("wxUserLifeCycleService")
    private UserLifeCycleService wxUserLifeCycleService;

    @Override
    public UserLifeCycleService getUserLifeCycleService(AbstractUser user) {

        if (webSocketService.hasH5Connected(user.getId())) {
            return wvUserLifeCycleService;
        }
        return wxUserLifeCycleService;
    }
}
