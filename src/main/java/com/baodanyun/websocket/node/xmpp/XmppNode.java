package com.baodanyun.websocket.node.xmpp;

import com.baodanyun.websocket.bean.user.AbstractUser;
import com.baodanyun.websocket.exception.BusinessException;
import com.baodanyun.websocket.node.Node;
import org.jivesoftware.smack.ConnectionListener;
import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.chat.ChatManagerListener;
import org.jivesoftware.smack.chat.ChatMessageListener;

import java.io.IOException;
import java.util.List;

/**
 * Created by liaowuhen on 2017/5/23.
 */
public interface XmppNode extends ChatManagerListener, ChatMessageListener, ConnectionListener {
    boolean login() throws BusinessException, IOException, XMPPException, SmackException;

    boolean logout() throws BusinessException, IOException, XMPPException, SmackException;

    boolean isOnline();

    AbstractUser getAbstractUser();

    List<Node> getNodes();

    void addNode(Node node);

    void initVCard();

    XMPPConnection getXMPPConnection();


}
