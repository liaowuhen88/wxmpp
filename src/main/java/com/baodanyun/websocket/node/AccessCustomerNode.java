package com.baodanyun.websocket.node;

import com.baodanyun.websocket.bean.msg.Msg;
import com.baodanyun.websocket.bean.user.AbstractUser;
import com.baodanyun.websocket.bean.user.Customer;
import com.baodanyun.websocket.exception.BusinessException;
import com.baodanyun.websocket.service.MsgSendService;
import com.baodanyun.websocket.service.UserServer;
import com.baodanyun.websocket.util.SpringContextUtil;
import com.baodanyun.websocket.util.XMPPUtil;
import org.apache.log4j.Logger;
import org.springframework.util.StringUtils;

/**
 * Created by liaowuhen on 2017/5/23.
 */
public class AccessCustomerNode extends CustomerNode {
    private static final Logger logger = Logger.getLogger(AccessCustomerNode.class);
    private UserServer userServer = SpringContextUtil.getBean("userServer", UserServer.class);
    private MsgSendService msgSendService = SpringContextUtil.getBean("AccessMsgSendServiceImpl", MsgSendService.class);

    public AccessCustomerNode(Customer customer) {
        super(customer);
    }

    @Override
    public MsgSendService getMsgSendService() {
        return msgSendService;
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
                        String to = XMPPUtil.jidToName(msg.getTo());

                        // TODO
                        AbstractUser visitor = null;
                        try {
                            visitor = userServer.InitByUidOrNameOrPhone(to);
                        } catch (BusinessException e) {
                            logger.error("获取用户失败", e);
                        }

                        if (null != visitor) {
                            msg.setTo(visitor.getId());
                        }
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
