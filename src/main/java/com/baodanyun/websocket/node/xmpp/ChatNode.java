package com.baodanyun.websocket.node.xmpp;

import com.baodanyun.websocket.bean.user.AbstractUser;
import com.baodanyun.websocket.node.Node;
import com.baodanyun.websocket.node.dispatcher.ChatLifecycle;
import com.baodanyun.websocket.node.dispatcher.XmppNodeMsgDeal;
import com.baodanyun.websocket.node.terminal.TerminalMsgDeal;
import org.jivesoftware.smack.ConnectionListener;
import org.jivesoftware.smack.chat.ChatManagerListener;
import org.jivesoftware.smack.chat.ChatMessageListener;

/**
 * Created by liaowuhen on 2017/5/23.
 */

public interface ChatNode extends ChatManagerListener, ChatMessageListener, ConnectionListener, ChatLifecycle, TerminalMsgDeal, XmppNodeMsgDeal {

    void addNode(Node node);

    Node getNode(String id);
    /**
     * 获取用户
     *
     * @return
     */
    AbstractUser getAbstractUser();

    /**
     * 更新initVcard
     */
    void initVCard();


}
