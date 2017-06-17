/*
package com.baodanyun.websocket.core.handle;

import com.baodanyun.websocket.bean.user.Visitor;
import com.baodanyun.websocket.exception.BusinessException;
import com.baodanyun.websocket.service.MsgSendService;
import com.baodanyun.websocket.service.XmppServer;
import com.baodanyun.websocket.util.SpringContextUtil;
import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.XMPPException;

import java.io.IOException;

*/
/**
 * Created by yutao on 2016/9/7.
 * 如果访客节点上线，客服节点会收到订阅消息
 *//*

public class VisitorNodeWeiXin extends VisitorTerminal {
    public MsgSendService msgSendService = SpringContextUtil.getBean("WeiXInMsgSendServiceImpl", MsgSendService.class);

    //访客回调事件
    protected IVisitorNodeEvent visitorEvent = null;

    public VisitorNodeWeiXin(Visitor visitor) {
        super(visitor);
    }

    public boolean xmppLogin() throws IOException, XMPPException, SmackException {
        return xmppServer.login(this.getBindUser(), XmppServer.VISITOR);
    }


    public boolean login() throws SmackException, XMPPException, IOException, BusinessException {
        Visitor visitor = (Visitor) this.getBindUser();
        boolean customerIsOnline = customerIsOnline();
        boolean isLoginDone;
        if (customerIsOnline) {
            isLoginDone = xmppLogin();
            if (isLoginDone) {
                NodeManager.getVisitorNodeMap().put(this.getBindUser().getId(), this);
                xmppServer.roster(visitor.getId(), visitor.getCustomerJid());
            } else {
                throw new BusinessException("登录失败");
            }
        } else {
            throw new BusinessException("customer is not online");
        }

        return isLoginDone;
    }

    public MsgSendService getMsgSendService() {
        return msgSendService;
    }
}
*/
