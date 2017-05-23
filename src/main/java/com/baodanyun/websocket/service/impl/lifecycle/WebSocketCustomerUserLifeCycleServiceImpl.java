/*
package com.baodanyun.websocket.service.impl.lifecycle;

import com.baodanyun.websocket.bean.msg.Msg;
import com.baodanyun.websocket.bean.user.AbstractUser;
import com.baodanyun.websocket.exception.BusinessException;
import com.baodanyun.websocket.service.CustomerDispatcherService;
import com.baodanyun.websocket.service.MsgSendService;
import com.baodanyun.websocket.service.UserLifeCycleService;
import org.jivesoftware.smack.SmackException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.io.IOException;

*/
/**
 * Created by liaowuhen on 2017/3/6.
 *//*

@Service("wcUserLifeCycleService")
public class WebSocketCustomerUserLifeCycleServiceImpl extends CustomerUserLifeCycleServiceImpl {
    @Autowired
    @Qualifier("webSocketMsgSendService")
    protected MsgSendService msgSendService;

    @Autowired
    @Qualifier("wvUserLifeCycleService")
    protected UserLifeCycleService userLifeCycleService;

    @Autowired
    protected CustomerDispatcherService customerDispatcherService;

    @Override
    public MsgSendService getMsgSendService() {
        return msgSendService;
    }

    @Override
    public void logout(AbstractUser customer) throws InterruptedException {
        try {
            xmppServer.closed(customer.getId());
        } catch (IOException e) {
            logger.info("closed xmpp error", e);
        }

        customerDispatcherService.deleteCustomer(customer);

       */
/* Set<AbstractUser> onlineQueue = userCacheServer.get(CommonConfig.USER_ONLINE, customer.getId());

        if(null != onlineQueue){
            for(AbstractUser visitor:onlineQueue){
                // 通知用户
                userLifeCycleService.logout(visitor);
            }
        }*//*


    }

    @Override
    public Msg receiveMessage(AbstractUser user, String content) throws InterruptedException, SmackException.NotConnectedException, BusinessException {
        Msg msg = super.receiveMessage(user,content);

        lastVisitorSendMessageService.remove(msg.getTo(), msg.getFrom());
        return msg;
    }

    @Override
    public Msg getMsg(AbstractUser user, String content) {
        if (!StringUtils.isEmpty(content)) {
            Msg msg = Msg.handelMsg(content);
            if (msg != null) {
                if (StringUtils.isEmpty(msg.getFrom())) {
                    logger.error("handleSendMsg from is blank");
                } else {
                    if (StringUtils.isEmpty(msg.getTo())) {
                        logger.error("handleSendMsg to is blank");
                    } else {
                        return msg;
                    }
                }
            }

        } else {
            logger.error("msg is blank");
        }
       return null;
    }
}
*/
