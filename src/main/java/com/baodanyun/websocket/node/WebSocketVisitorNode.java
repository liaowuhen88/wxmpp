package com.baodanyun.websocket.node;

import com.baodanyun.websocket.bean.user.Visitor;
import com.baodanyun.websocket.service.MsgSendService;
import com.baodanyun.websocket.util.SpringContextUtil;
import org.apache.log4j.Logger;

/**
 * Created by liaowuhen on 2017/5/23.
 */
public class WebSocketVisitorNode extends VisitorNode {
    private static final Logger logger = Logger.getLogger(WeChatNode.class);
    private MsgSendService msgSendService = SpringContextUtil.getBean("webSocketMsgSendService", MsgSendService.class);

    public WebSocketVisitorNode(Visitor visitor) {
        super(visitor);
    }


    @Override
    public MsgSendService getMsgSendService() {
        return msgSendService;
    }
}
