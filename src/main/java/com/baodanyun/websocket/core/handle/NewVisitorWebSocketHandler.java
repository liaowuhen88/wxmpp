package com.baodanyun.websocket.core.handle;


import com.baodanyun.websocket.node.WebSocketTerminalVisitorFactory;
import com.baodanyun.websocket.util.JSONUtil;
import com.baodanyun.websocket.util.SpringContextUtil;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

/**
 * 进入当前处理器后
 * 节点已经验证完成了
 */
public class NewVisitorWebSocketHandler extends VisitorWebSocketHandler {

    WebSocketTerminalVisitorFactory webSocketTerminalVisitorFactory = SpringContextUtil.getBean("webSocketTerminalVisitorFactory", WebSocketTerminalVisitorFactory.class);

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {

    }


    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        logger.info("sessionId--->" + session.getId() + "webSocket receive message:" + JSONUtil.toJson(message));
        String content = message.getPayload();
        session.sendMessage(new TextMessage(content));
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {

    }
}
