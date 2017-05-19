package com.baodanyun.websocket.service;

import com.baodanyun.websocket.bean.msg.Msg;
import com.baodanyun.websocket.bean.user.AbstractUser;
import com.baodanyun.websocket.exception.BusinessException;
import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.XMPPException;
import org.springframework.stereotype.Service;

import java.io.IOException;

/**
 * Created by liaowuhen on 2017/3/6.
 */

@Service
public interface UserLifeCycleService {

    boolean login(AbstractUser user) throws IOException, XMPPException, SmackException, BusinessException, InterruptedException;

    //boolean init(AbstractUser user) throws InterruptedException;

    /**
     * @param user
     * @return
     * @throws InterruptedException
     * @throws BusinessException
     * @throws SmackException.NotLoggedInException
     * @throws XMPPException.XMPPErrorException
     * @throws SmackException.NotConnectedException
     * @throws SmackException.NoResponseException
     */
    boolean online(AbstractUser user) throws InterruptedException, BusinessException, SmackException.NotLoggedInException, XMPPException.XMPPErrorException, SmackException.NotConnectedException, SmackException.NoResponseException;

    /**
     * 处理离线信息
     * @param user
     * @return
     * @throws InterruptedException
     */
    //boolean pushOfflineMsg(AbstractUser user) throws InterruptedException, BusinessException;

    void logout(AbstractUser user) throws InterruptedException;

    /**
     * 接收消息发送到xmpp
     * @param user
     * @param content
     * @return
     * @throws InterruptedException
     * @throws SmackException.NotConnectedException
     * @throws BusinessException
     */
    Msg receiveMessage(AbstractUser user, String content) throws InterruptedException, SmackException.NotConnectedException, BusinessException;

    Msg receiveMessage(AbstractUser user, Msg msg) throws InterruptedException, SmackException.NotConnectedException, BusinessException;

    boolean joinQueue(AbstractUser user) throws InterruptedException;

    boolean uninstallVisitor(AbstractUser user) throws InterruptedException;
    /**
     * 获取消息通知器
      */

    MsgSendService getMsgSendService() ;
}
