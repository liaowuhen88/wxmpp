/*
package com.baodanyun.websocket.service.impl.lifecycle;

import com.baodanyun.websocket.bean.msg.Msg;
import com.baodanyun.websocket.bean.user.AbstractUser;
import com.baodanyun.websocket.exception.BusinessException;
import com.baodanyun.websocket.service.MsgSendService;
import com.baodanyun.websocket.util.XMPPUtil;
import org.jivesoftware.smack.SmackException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

*/
/**
 * Created by liaowuhen on 2017/3/6.
 *//*

@Service("accessCustomerUserLifeCycleService")
public class AccessCustomerUserLifeCycleServiceImpl extends CustomerUserLifeCycleServiceImpl {
    @Autowired
    @Qualifier("webSocketMsgSendService")
    protected MsgSendService msgSendService;

    @Override
    public MsgSendService getMsgSendService() {
        return msgSendService;
    }

    @Override
    public void logout(AbstractUser customer) throws InterruptedException {

    }

    @Override
    public Msg receiveMessage(AbstractUser user, String content) throws InterruptedException, SmackException.NotConnectedException, BusinessException {
        Msg msg = super.receiveMessage(user, content);
        lastVisitorSendMessageService.remove(msg.getTo(), msg.getFrom());
        return msg;
    }

    @Override
    public Msg getMsg(AbstractUser user, String content) {
        if (!StringUtils.isEmpty(content)) {
            Msg msg = Msg.handelMsg(content);
            if (msg != null) {
                if (StringUtils.isEmpty(msg.getFrom())) {
                    logger.error("error","handleSendMsg from is blank");
                } else {
                    if (StringUtils.isEmpty(msg.getTo())) {
                        logger.error("error","handleSendMsg to is blank");
                    } else {
                        String to = XMPPUtil.jidToName(msg.getTo());
                        AbstractUser visitor = userCacheServer.getVisitorByUidOrOpenID(to);
                        if (null != visitor) {
                            msg.setTo(visitor.getId());
                        }
                        return msg;
                    }
                }
            }

        } else {
            logger.error("error","msg is blank");
        }
        return null;
    }
}
*/
