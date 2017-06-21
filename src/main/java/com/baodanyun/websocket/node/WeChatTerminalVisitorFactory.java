package com.baodanyun.websocket.node;

import com.baodanyun.websocket.bean.user.AbstractUser;
import com.baodanyun.websocket.service.TerminalFactory;
import com.baodanyun.websocket.util.JSONUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * Created by think on 2017-06-16.
 */
@Service
public class WeChatTerminalVisitorFactory implements TerminalFactory<AbstractUser> {
    public static final String key = "weChat_";
    private static final Logger logger = LoggerFactory.getLogger(WeChatTerminalVisitorFactory.class);

    @Override
    public String getId(AbstractUser visitor) {
        return key + visitor.getId();
    }

    @Override
    public AbstractTerminal getNode(ChatNodeAdaptation chatNodeAdaptation, AbstractUser visitor) {
        logger.info("create WeChatTerminal [" + JSONUtil.toJson(visitor) + "]");
        return new WeChatTerminal(chatNodeAdaptation, getId(visitor));
    }
}
