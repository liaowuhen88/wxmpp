package com.baodanyun.websocket.service.impl.terminal;

import com.baodanyun.websocket.node.AbstractTerminal;
import com.baodanyun.websocket.node.AccessCustomerTerminal;
import com.baodanyun.websocket.node.terminal.WebSocketTerminal;
import com.baodanyun.websocket.node.ChatNodeAdaptation;
import com.baodanyun.websocket.service.TerminalFactory;
import com.baodanyun.websocket.util.JSONUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * Created by think on 2017-06-16.
 */
@Service
public class AccessWebSocketTerminalCustomerFactory implements TerminalFactory<WebSocketTerminal> {
    public static final String key = "weChat_";
    private static final Logger logger = LoggerFactory.getLogger(AccessWebSocketTerminalCustomerFactory.class);


    @Override
    public String getId(WebSocketTerminal webSocketTerminal) {
        return webSocketTerminal.getSession().getId();
    }

    @Override
    public AbstractTerminal getNode(ChatNodeAdaptation chatNodeAdaptation, WebSocketTerminal webSocketTerminal) {
        logger.info("create AccessWebSocketTerminalCustomerFactory [" + JSONUtil.toJson(webSocketTerminal.getAbstractUser()) + "]");
        return new AccessCustomerTerminal(chatNodeAdaptation,webSocketTerminal.getAbstractUser(),webSocketTerminal.getSession(),getId(webSocketTerminal));

    }
}
