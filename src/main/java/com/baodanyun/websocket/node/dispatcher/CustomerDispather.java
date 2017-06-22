package com.baodanyun.websocket.node.dispatcher;

import com.baodanyun.websocket.node.VisitorChatNode;

/**
 * Created by liaowuhen on 2017/5/24.
 */
public interface CustomerDispather {
    /**
     * 接入访客
     *
     * @param visitorChatNode
     * @return
     * @throws InterruptedException
     */
    boolean joinQueue(VisitorChatNode visitorChatNode) throws InterruptedException;

    /**
     * 移除访客
     * @param visitorChatNode
     * @return
     * @throws InterruptedException
     */
    boolean uninstall(VisitorChatNode visitorChatNode) throws InterruptedException;

}
