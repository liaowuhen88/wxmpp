/*
package com.baodanyun.websocket.service.impl.lifecycle;

import com.baodanyun.websocket.bean.user.AbstractUser;
import com.baodanyun.websocket.bean.user.Visitor;
import com.baodanyun.websocket.event.VisitorLogoutEvent;
import com.baodanyun.websocket.service.MsgSendService;
import com.baodanyun.websocket.service.WebSocketService;
import com.baodanyun.websocket.util.EventBusUtils;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

*/
/**
 * Created by liaowuhen on 2017/3/6.
 *//*

@Service("wvUserLifeCycleService")
public class WebSocketVisitorUserLifeCycleServiceImpl extends VisitorUserLifeCycleServiceImpl {
    private static final Logger logger = LoggerFactory.getLogger(WebSocketVisitorUserLifeCycleServiceImpl.class);

    @Autowired
    protected WebSocketService webSocketService;

    @Autowired
    @Qualifier("webSocketMsgSendService")
    protected MsgSendService msgSendService;

    @Override
    public void logout(AbstractUser user) throws InterruptedException {
        VisitorLogoutEvent ve = new VisitorLogoutEvent(user, ((Visitor) user).getCustomer(), msgSendService);
        EventBusUtils.post(ve);
    }

    @Override
    public MsgSendService getMsgSendService() {
        return msgSendService;
    }
}
*/
