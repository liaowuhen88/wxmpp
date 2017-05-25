package com.baodanyun.websocket.node;

import com.baodanyun.websocket.bean.user.Visitor;
import org.apache.log4j.Logger;
import org.springframework.web.socket.WebSocketSession;

/**
 * Created by liaowuhen on 2017/5/23.
 */
public class AccessVisitorNode extends WeChatNode {
    private static final Logger logger = Logger.getLogger(WeChatNode.class);

    private WebSocketSession session;

    public AccessVisitorNode(Visitor visitor) {
        super(visitor);
    }

    public WebSocketSession getSession() {
        return session;
    }

    public void setSession(WebSocketSession session) {
        this.session = session;
    }

    @Override
    public void online() throws InterruptedException {
        super.online();
        joinQueue();
    }

}
