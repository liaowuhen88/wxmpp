package com.baodanyun.websocket.node;

import com.baodanyun.websocket.bean.msg.Msg;
import com.baodanyun.websocket.bean.user.Customer;
import com.baodanyun.websocket.service.CustomerDispatcherService;
import com.baodanyun.websocket.service.MsgSendService;
import com.baodanyun.websocket.util.SpringContextUtil;
import org.apache.log4j.Logger;
import org.springframework.util.StringUtils;

import java.io.IOException;

/**
 * Created by liaowuhen on 2017/5/23.
 */
public class WebSocketCustomerNode extends CustomerNode {
    private static final Logger logger = Logger.getLogger(WebSocketCustomerNode.class);
    CustomerDispatcherService customerDispatcherService = SpringContextUtil.getBean("customerDispatcherServiceImpl", CustomerDispatcherService.class);
    private MsgSendService msgSendService = SpringContextUtil.getBean("webSocketMsgSendService", MsgSendService.class);

    public WebSocketCustomerNode(Customer customer) {
        super(customer);
    }

    @Override
    public MsgSendService getMsgSendService() {
        return msgSendService;
    }

    @Override
    public void logout() throws InterruptedException {
        try {
            xmppServer.closed(getAbstractUser().getId());
        } catch (IOException e) {
            logger.info("closed xmpp error", e);
        }

        this.getXmppNode().getNodes().remove(this);
        customerDispatcherService.deleteCustomer(getAbstractUser());

    }

    @Override
    public Msg getMsg(String content) {
        if (!StringUtils.isEmpty(content)) {
            Msg msg = Msg.handelMsg(content);
            if (msg != null) {
                if (StringUtils.isEmpty(msg.getFrom())) {
                    logger.error("handleSendMsg from is blank");
                } else {
                    if (StringUtils.isEmpty(msg.getTo())) {
                        logger.error("handleSendMsg to is blank");
                    } else {
                        return msg;
                    }
                }
            }

        } else {
            logger.error("msg is blank");
        }
        return null;
    }

}
