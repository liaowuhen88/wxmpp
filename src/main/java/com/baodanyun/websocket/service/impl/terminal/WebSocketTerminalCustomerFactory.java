package com.baodanyun.websocket.service.impl.terminal;

import com.baodanyun.websocket.node.AbstractNode;
import com.baodanyun.websocket.node.WebSocketCustomerNode;
import com.baodanyun.websocket.node.terminal.WebSocketTerminal;
import com.baodanyun.websocket.node.xmpp.ChatNodeAdaptation;
import com.baodanyun.websocket.service.TerminalFactory;
import com.baodanyun.websocket.util.JSONUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * Created by think on 2017-06-16.
 */
@Service
public class WebSocketTerminalCustomerFactory implements TerminalFactory<WebSocketTerminal> {

    private static final Logger logger = LoggerFactory.getLogger(WebSocketTerminalCustomerFactory.class);

    @Override
    public String getId(WebSocketTerminal webSocketTerminal) {
        return webSocketTerminal.getSession().getId();
    }

    @Override
    public AbstractNode getNode(ChatNodeAdaptation chatNodeAdaptation, WebSocketTerminal webSocketTerminal) {
        logger.info("create WebSocketVisitorNode [" + JSONUtil.toJson(webSocketTerminal.getAbstractUser()) + "]");
        return new WebSocketCustomerNode(chatNodeAdaptation,webSocketTerminal.getAbstractUser(),webSocketTerminal.getSession(),getId(webSocketTerminal));

    }
}
