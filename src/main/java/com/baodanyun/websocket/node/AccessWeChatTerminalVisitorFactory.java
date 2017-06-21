package com.baodanyun.websocket.node;

import com.baodanyun.websocket.bean.user.Visitor;
import com.baodanyun.websocket.service.TerminalFactory;
import com.baodanyun.websocket.util.JSONUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * Created by think on 2017-06-16.
 */
@Service
public class AccessWeChatTerminalVisitorFactory implements TerminalFactory<Visitor> {
    public static final String key = "weChat_";
    private static final Logger logger = LoggerFactory.getLogger(AccessWeChatTerminalVisitorFactory.class);

    @Override
    public String getId(Visitor visitor) {
        return key + visitor.getId();
    }

    @Override
    public AbstractTerminal getNode(ChatNodeAdaptation chatNodeAdaptation, Visitor visitor) {
        logger.info("create WeChatTerminal [" + JSONUtil.toJson(visitor) + "]");
        return new AccessVisitorTerminal(chatNodeAdaptation, getId(visitor));
    }
}
