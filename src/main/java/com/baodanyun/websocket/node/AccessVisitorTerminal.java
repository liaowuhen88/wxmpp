package com.baodanyun.websocket.node;

import com.baodanyun.websocket.bean.user.AbstractUser;
import com.baodanyun.websocket.exception.BusinessException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.socket.WebSocketSession;

/**
 * Created by liaowuhen on 2017/5/23.
 */
public class AccessVisitorTerminal extends WeChatTerminal {
    private static final Logger logger = LoggerFactory.getLogger(WeChatTerminal.class);

    private WebSocketSession session;

    AccessVisitorTerminal(ChatNodeAdaptation chatNodeAdaptation, AbstractUser visitor, String id) {
        super(chatNodeAdaptation,visitor,id);
    }

    public WebSocketSession getSession() {
        return session;
    }

    public void setSession(WebSocketSession session) {
        this.session = session;
    }

    @Override
    public void online() throws InterruptedException, BusinessException {
        super.online();
        joinQueue();
    }

}
