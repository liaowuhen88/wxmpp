package com.baodanyun.websocket.node;

import com.baodanyun.websocket.bean.user.AbstractUser;
import com.baodanyun.websocket.exception.BusinessException;
import com.baodanyun.websocket.node.dispatcher.ChatLifecycle;
import com.baodanyun.websocket.node.dispatcher.XmppNodeMsgDeal;
import com.baodanyun.websocket.node.terminal.TerminalMsgDeal;
import org.jivesoftware.smack.ConnectionListener;
import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.chat.ChatManagerListener;
import org.jivesoftware.smack.chat.ChatMessageListener;

import java.io.IOException;

/**
 * Created by liaowuhen on 2017/5/23.
 */

public interface ChatNode extends ChatManagerListener, ChatMessageListener, ConnectionListener, ChatLifecycle, TerminalMsgDeal, XmppNodeMsgDeal {

    void setXmppConnection(XMPPConnection xmppConnection);
    /**
     * 添加终端节点
     * @param node
     */
    void addNode(AbstractTerminal node);

    /**
     * 获取终端节点
     * @param id
     * @return
     */
    AbstractTerminal getNode(String id);

    /**
     * 删除终端节点
     * @param id
     * @return
     */

    AbstractTerminal removeNode(String id) throws IOException, XMPPException, SmackException, BusinessException;

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
    /**
     * 是否在线
     */

    boolean isXmppOnline();

    /**
     * 是否在线
     */

    boolean isOnline();
}
