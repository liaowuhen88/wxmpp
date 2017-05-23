/*
package com.baodanyun.websocket.service.impl.lifecycle;

import com.baodanyun.websocket.bean.user.AbstractUser;
import com.baodanyun.websocket.enums.MsgStatus;
import com.baodanyun.websocket.exception.BusinessException;
import com.baodanyun.websocket.util.CommonConfig;
import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.XMPPException;
import org.springframework.stereotype.Service;

import java.io.IOException;

*/
/**
 * Created by liaowuhen on 2017/3/6.
 *//*

@Service
public abstract  class CustomerUserLifeCycleServiceImpl extends UserLifeCycleServiceImpl {


    @Override
    public boolean login(AbstractUser user) throws IOException, XMPPException, SmackException, BusinessException, InterruptedException {
        boolean flag = super.login(user);

        return flag;
    }

    @Override
    public boolean online(AbstractUser user) throws InterruptedException, BusinessException, SmackException.NotLoggedInException, XMPPException.XMPPErrorException, SmackException.NotConnectedException, SmackException.NoResponseException {
        super.online(user);

        getMsgSendService().sendSMMsgToCustomer(user, MsgStatus.loginSuccess);

        getMsgSendService().sendSMMsgToCustomer(user, MsgStatus.initSuccess);

        logger.info("保存到缓存[USER_CUSTOMER][" + user.getId() + "]--->" + userCacheServer.add(CommonConfig.USER_CUSTOMER, user));
        return true;
    }

}
*/
