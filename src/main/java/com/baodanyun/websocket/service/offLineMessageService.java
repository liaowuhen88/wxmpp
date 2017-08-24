package com.baodanyun.websocket.service;

import com.baodanyun.websocket.node.VisitorChatNode;

/**
 * Created by liaowuhen on 2017/8/24.
 */
public interface OffLineMessageService {
    void dealOfflineMessage(VisitorChatNode visitorChatNode, String leaveMsg, String returnMsg);

    void dealOfflineMessage(VisitorChatNode visitorChatNode, String leaveMsg);
}
