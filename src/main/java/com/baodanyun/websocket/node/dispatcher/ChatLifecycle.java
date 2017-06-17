package com.baodanyun.websocket.node.dispatcher;

import com.baodanyun.websocket.exception.BusinessException;
import com.baodanyun.websocket.node.AbstractTerminal;
import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.XMPPException;

import java.io.IOException;

/**
 * Created by liaowuhen on 2017/5/24.
 * <p/>
 * 负责用户的接入
 * <p/>
 * 用户的退出
 */
public interface ChatLifecycle {


    String getId();

    /**
     * 登出
     *
     * @return
     * @throws BusinessException
     * @throws IOException
     * @throws XMPPException
     * @throws SmackException
     */
    boolean logout() throws BusinessException, IOException, XMPPException, SmackException;


    /**
     * xmpp 登录
     *
     * @return
     * @throws BusinessException
     * @throws IOException
     * @throws XMPPException
     * @throws SmackException
     */
    boolean login() throws BusinessException, IOException, XMPPException, SmackException;



    /**
     * 上线
     * @throws InterruptedException
     * @throws BusinessException
     */
    void online(AbstractTerminal node) throws InterruptedException, BusinessException;


}
