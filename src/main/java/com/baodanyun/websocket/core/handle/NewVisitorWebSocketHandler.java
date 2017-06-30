package com.baodanyun.websocket.core.handle;


import com.baodanyun.websocket.node.WebSocketTerminalVisitorFactory;
import com.baodanyun.websocket.util.SpringContextUtil;

/**
 * 进入当前处理器后
 * 节点已经验证完成了
 */
public class NewVisitorWebSocketHandler extends VisitorWebSocketHandler {

    WebSocketTerminalVisitorFactory webSocketTerminalVisitorFactory = SpringContextUtil.getBean("webSocketTerminalVisitorFactory", WebSocketTerminalVisitorFactory.class);


   /* @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        logger.info("sessionId--->" + session.getId() + "webSocket receive message:" + JSONUtil.toJson(message));
        String content = message.getPayload();
        session.sendMessage(new TextMessage(content));
    }*/

}
