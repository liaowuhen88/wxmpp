package com.baodanyun.websocket.core.handle;

import com.baodanyun.websocket.service.XmppServer;
import com.baodanyun.websocket.util.SpringContextUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.socket.handler.TextWebSocketHandler;

/**
 * Created by yutao on 2016/9/27.
 */
public abstract class AbstractWebSocketHandler extends TextWebSocketHandler {
    protected static Logger logger = LoggerFactory.getLogger(AbstractWebSocketHandler.class);
    public XmppServer xmppServer = SpringContextUtil.getBean("xmppServer", XmppServer.class);

}
