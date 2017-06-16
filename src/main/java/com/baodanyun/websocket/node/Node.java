package com.baodanyun.websocket.node;

import com.baodanyun.websocket.bean.user.AbstractUser;
import com.baodanyun.websocket.node.dispatcher.ChatLifecycle;
import com.baodanyun.websocket.node.terminal.TerminalMsgDeal;

/**
 * Created by liaowuhen on 2017/5/23.
 */
public interface Node extends ChatLifecycle, TerminalMsgDeal {

    AbstractUser getAbstractUser();

}
