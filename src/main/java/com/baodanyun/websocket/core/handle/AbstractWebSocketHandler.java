package com.baodanyun.websocket.core.handle;

import com.baodanyun.websocket.service.XmppServer;
import com.baodanyun.websocket.util.SpringContextUtil;
import org.apache.log4j.Logger;
import org.springframework.web.socket.handler.TextWebSocketHandler;

/**
 * Created by yutao on 2016/9/27.
 */
public abstract class AbstractWebSocketHandler extends TextWebSocketHandler {
    public XmppServer xmppServer = SpringContextUtil.getBean("xmppServer", XmppServer.class);

    protected static Logger logger = Logger.getLogger(AbstractWebSocketHandler.class);

}
