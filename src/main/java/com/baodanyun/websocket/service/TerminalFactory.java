package com.baodanyun.websocket.service;

import com.baodanyun.websocket.node.AbstractNode;
import com.baodanyun.websocket.node.xmpp.ChatNodeAdaptation;

/**
 * Created by think on 2017-06-16.
 */
public interface TerminalFactory<T> {
    String getId(T t);

    AbstractNode getNode(ChatNodeAdaptation chatNodeAdaptation, T t);
}
