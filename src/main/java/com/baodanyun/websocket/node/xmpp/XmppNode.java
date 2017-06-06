package com.baodanyun.websocket.node.xmpp;

import com.baodanyun.websocket.bean.user.AbstractUser;
import com.baodanyun.websocket.node.Node;
import com.baodanyun.websocket.node.dispatcher.Dispatcher;
import org.jivesoftware.smack.ConnectionListener;
import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.chat.ChatManagerListener;
import org.jivesoftware.smack.chat.ChatMessageListener;
import org.jivesoftware.smack.packet.Message;

import java.util.List;

/**
 * Created by liaowuhen on 2017/5/23.
 */

public interface XmppNode extends ChatManagerListener, ChatMessageListener, ConnectionListener, Dispatcher {

    /**
     * 判断xxmpp是否在线
     *
     * @return
     */

    boolean isOnline();

    /**
     * 获取用户
     * @return
     */
    AbstractUser getAbstractUser();


    /**
     * 获取绑定的客户端
     * @return
     */
    List<Node> getNodes();


    /**
     * 增加客户端
     * @param node
     */
    void addNode(Node node);

    /**
     * 更新initVcard
     */
    void initVCard();


    /**
     * 获取连接器
     * @return
     */
    XMPPConnection getXMPPConnection();

    void setXmppConnection(XMPPConnection xmppConnection);

    /**
     * 发送信息到xmpp
     *
     * @param
     * @throws SmackException.NotConnectedException
     */

    void sendMessage(Message xmppMsg) throws SmackException.NotConnectedException;

}
