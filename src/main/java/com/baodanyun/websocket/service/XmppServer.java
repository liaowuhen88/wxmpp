package com.baodanyun.websocket.service;

import com.baodanyun.websocket.bean.msg.Msg;
import com.baodanyun.websocket.bean.user.AbstractUser;
import com.baodanyun.websocket.exception.BusinessException;
import com.baodanyun.websocket.node.ChatNode;
import org.jivesoftware.smack.AbstractXMPPConnection;
import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Presence;

import java.io.IOException;
import java.util.Set;

/**
 * Created by liaowuhen on 2016/11/11.
 */
public interface XmppServer {

    boolean isAuthenticated(String jid);

    Set<String> getJids();

    /**
     * 是否在线 包括手机端在线和容器tomcat在线
     *
     * @param jid
     * @return
     */

    boolean isConnected(String jid);

    /**
     * 获取登录成功的XMPPConnection
     *
     * @param jid
     * @return
     */

    AbstractXMPPConnection getXMPPConnectionAuthenticated(String jid) throws BusinessException;

    /**
     * 获取登录成功的XMPPConnection
     *
     * @param jid
     * @return
     */
    AbstractXMPPConnection getXMPPConnection(String jid);

    /**
     * 保存登陆成功的XMppConnection
     *
     * @param jid
     * @param xMPPConnection
     */
    void saveXMPPConnection(String jid, AbstractXMPPConnection xMPPConnection);

    boolean closed(String jid) throws IOException;

    void sendPresence(String jid, Presence.Type type) throws SmackException.NotConnectedException;

    void sendMessage(Msg msg) throws SmackException.NotConnectedException, BusinessException;

    void sendMessage(Message xmppMsg) throws BusinessException, SmackException.NotConnectedException;

    boolean login(AbstractUser user) throws SmackException, XMPPException, IOException;

    boolean login(ChatNode chatNode) throws SmackException, XMPPException, IOException;

}
