package com.baodanyun.websocket.service;

import com.baodanyun.websocket.node.AbstractTerminal;
import com.baodanyun.websocket.node.ChatNodeAdaptation;

/**
 * Created by think on 2017-06-16.
 */
public interface TerminalFactory<T> {
    String getId(T t);

    AbstractTerminal getNode(ChatNodeAdaptation chatNodeAdaptation, T t);
}
