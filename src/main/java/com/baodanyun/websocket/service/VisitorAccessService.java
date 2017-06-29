package com.baodanyun.websocket.service;

import com.baodanyun.websocket.node.VisitorChatNode;

/**
 * Created by liaowuhen on 2017/6/29.
 */
public interface VisitorAccessService {
    VisitorChatNode accessVIsitor(String openID);
}
