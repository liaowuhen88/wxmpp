package com.baodanyun.websocket.node.terminal;

import com.baodanyun.websocket.bean.user.AbstractUser;
import com.baodanyun.websocket.bean.user.Visitor;
import org.springframework.web.socket.WebSocketSession;

/**
 * Created by think on 2017-06-16.
 */
public class WebSocketTerminal {
    public WebSocketTerminal( AbstractUser abstractUser,WebSocketSession session){
        this.abstractUser = abstractUser;
        this.session = session;
    }
    private AbstractUser abstractUser;
    private WebSocketSession session;

    public AbstractUser getAbstractUser() {
        return abstractUser;
    }

    public void setAbstractUser(AbstractUser abstractUser) {
        this.abstractUser = abstractUser;
    }

    public WebSocketSession getSession() {
        return session;
    }

    public void setSession(WebSocketSession session) {
        this.session = session;
    }
}
