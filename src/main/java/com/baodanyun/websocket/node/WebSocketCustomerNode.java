package com.baodanyun.websocket.node;

import com.baodanyun.websocket.bean.msg.Msg;
import com.baodanyun.websocket.bean.msg.status.StatusMsg;
import com.baodanyun.websocket.bean.user.AbstractUser;
import com.baodanyun.websocket.bean.user.Customer;
import com.baodanyun.websocket.enums.MsgStatus;
import com.baodanyun.websocket.exception.BusinessException;
import com.baodanyun.websocket.node.sendUtils.SessionSendUtils;
import com.baodanyun.websocket.util.CommonConfig;
import org.apache.log4j.Logger;
import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.XMPPException;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;

/**
 * Created by liaowuhen on 2017/5/23.
 */
public class WebSocketCustomerNode extends CustomerNode {
    private static final Logger logger = Logger.getLogger(WebSocketCustomerNode.class);
    private WebSocketSession session;

    public WebSocketCustomerNode(Customer customer) {
        super(customer);
    }

    public WebSocketSession getSession() {
        return session;
    }

    public void setSession(WebSocketSession session) {
        this.session = session;
    }


    @Override
    public boolean sendMsgToGod(Msg msg) {
        SessionSendUtils.send(getSession(), msg);
        return true;
    }

    @Override
    public boolean logout() throws BusinessException, IOException, XMPPException, SmackException {
        return super.logout();
    }

    @Override
    public boolean joinQueue(AbstractUser abstractUser) throws InterruptedException {

        super.joinQueue(abstractUser);

        Msg hello = getMsgHelloToCustomer(abstractUser);
        Msg online = getSMMsgOnlineSendTOCustomer(abstractUser, MsgStatus.onlineQueueSuccess);

        SessionSendUtils.send(getSession(), online);
        SessionSendUtils.send(getSession(), hello);

        return true;
    }

    @Override
    public boolean messageCallBack(AbstractUser abstractUser, MsgStatus msgStatus) throws InterruptedException {
        StatusMsg msg = getSMMsgSendTOCustomer(msgStatus);
        SessionSendUtils.send(getSession(), msg);
        return false;
    }

    @Override
    public void online() throws InterruptedException {
        StatusMsg msg = getSMMsgSendTOCustomer(MsgStatus.loginSuccess);
        StatusMsg initSuccess = getSMMsgSendTOCustomer(MsgStatus.initSuccess);
        StatusMsg onlineQueueSuccess = getSMMsgSendTOCustomer(MsgStatus.onlineQueueSuccess);
        SessionSendUtils.send(getSession(), msg);
        SessionSendUtils.send(getSession(), initSuccess);

        logger.info("保存到缓存[USER_CUSTOMER][" + this.getAbstractUser().getId() + "]--->" + userCacheServer.add(CommonConfig.USER_CUSTOMER, this.getAbstractUser()));
    }

}
