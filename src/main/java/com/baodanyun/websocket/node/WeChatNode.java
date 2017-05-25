package com.baodanyun.websocket.node;

import com.baodanyun.websocket.bean.msg.Msg;
import com.baodanyun.websocket.bean.user.Visitor;
import com.baodanyun.websocket.node.sendUtils.WeChatSendUtils;
import org.apache.log4j.Logger;

/**
 * Created by liaowuhen on 2017/5/23.
 */
public class WeChatNode extends VisitorNode {
    private static final Logger logger = Logger.getLogger(WeChatNode.class);

    public WeChatNode(Visitor visitor) {
        super(visitor);
    }

    @Override
    public boolean sendMsgToGod(Msg msg) {
        boolean flag = false;
        try {
            msg.setType("text");
            msg.setFrom(this.getAbstractUser().getOpenId());
            flag = WeChatSendUtils.send(msg);

        } catch (Exception e) {
            logger.error("", e);
        }
        return flag;
    }

    @Override
    public void online() throws InterruptedException {
        super.online();
        Msg hello = getMsgHelloToVisitor(((Visitor) getAbstractUser()));
        hello.setFrom(this.getAbstractUser().getOpenId());

        WeChatSendUtils.send(hello);

        joinQueue();
    }


}
