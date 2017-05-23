/*
package com.baodanyun.websocket.service.impl.lifecycle;

import com.baodanyun.websocket.bean.msg.Msg;
import com.baodanyun.websocket.bean.user.AbstractUser;
import com.baodanyun.websocket.bean.user.Visitor;
import com.baodanyun.websocket.exception.BusinessException;
import com.baodanyun.websocket.service.MsgSendService;
import com.baodanyun.websocket.service.WebSocketService;
import com.baodanyun.websocket.util.CommonConfig;
import com.baodanyun.websocket.util.JSONUtil;
import org.apache.log4j.Logger;
import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.XMPPException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.io.IOException;

*/
/**
 * Created by liaowuhen on 2017/3/6.
 *//*

@Service("wxUserLifeCycleService")
public class WeiXInVisitorUserLifeCycleServiceImpl extends VisitorUserLifeCycleServiceImpl {
    private static final Logger logger = Logger.getLogger(WeiXInVisitorUserLifeCycleServiceImpl.class);

    @Autowired
    protected WebSocketService webSocketService;

    @Autowired
    @Qualifier("winXinMsgSendService")
    protected MsgSendService msgSendService;

    @Override
    public boolean login(AbstractUser user) throws IOException, XMPPException, SmackException, BusinessException, InterruptedException {
        Visitor visitor = (Visitor) user;
        userCacheServer.addOpenId(CommonConfig.USER_VISITOR,visitor);
        return super.login(user);
    }


    @Override
    public Msg getMsg(AbstractUser user, String content) {
        try {
            Msg msg = JSONUtil.toObject(Msg.class,content);
            AbstractUser customer = userCacheServer.getCustomerByVisitorOpenId(user.getOpenId());
            msg.setTo(customer.getId());
            msg.setFrom(user.getId());
            return msg;
        } catch (Exception e) {
            logger.error(e);
        }
        return null;
    }

    @Override
    public MsgSendService getMsgSendService() {
        return msgSendService;
    }
}
*/
