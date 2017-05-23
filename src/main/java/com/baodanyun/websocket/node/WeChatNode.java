package com.baodanyun.websocket.node;

import com.baodanyun.websocket.bean.msg.Msg;
import com.baodanyun.websocket.bean.user.Visitor;
import com.baodanyun.websocket.event.VisitorReciveMsgEvent;
import com.baodanyun.websocket.exception.BusinessException;
import com.baodanyun.websocket.service.MsgSendService;
import com.baodanyun.websocket.util.CommonConfig;
import com.baodanyun.websocket.util.EventBusUtils;
import com.baodanyun.websocket.util.SpringContextUtil;
import org.apache.log4j.Logger;
import org.jivesoftware.smack.SmackException;

/**
 * Created by liaowuhen on 2017/5/23.
 */
public class WeChatNode extends VisitorNode {
    private static final Logger logger = Logger.getLogger(WeChatNode.class);
    private MsgSendService msgSendService = SpringContextUtil.getBean("winXinMsgSendService", MsgSendService.class);


    public WeChatNode(Visitor visitor) {
        super(visitor);
    }

    @Override
    public MsgSendService getMsgSendService() {
        return msgSendService;
    }

    @Override
    public Msg receiveMessage(String content) throws InterruptedException, SmackException.NotConnectedException, BusinessException {
        Msg msg = super.receiveMessage(content);

        VisitorReciveMsgEvent vme = new VisitorReciveMsgEvent(getAbstractUser(), ((Visitor) getAbstractUser()).getCustomer(), msg.getContent(), CommonConfig.MSG_BIZ_KF_WX_CHAT);

        EventBusUtils.post(vme);

        return msg;
    }


}
