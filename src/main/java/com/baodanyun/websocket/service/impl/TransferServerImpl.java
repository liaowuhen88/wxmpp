package com.baodanyun.websocket.service.impl;

import com.baodanyun.websocket.bean.user.AbstractUser;
import com.baodanyun.websocket.bean.user.Visitor;
import com.baodanyun.websocket.exception.BusinessException;
import com.baodanyun.websocket.model.Transferlog;
import com.baodanyun.websocket.node.*;
import com.baodanyun.websocket.service.*;
import com.baodanyun.websocket.util.JSONUtil;
import org.apache.commons.lang.StringUtils;
import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.XMPPException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;

/**
 * Created by liaowuhen on 2016/11/11.
 */
@Service
public class TransferServerImpl implements TransferServer {
    protected final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private TransferlogServer transferlogServer;

    @Autowired
    private UserServer userServer;

    @Autowired
    private XmppServer xmppServer;

    @Autowired
    private CustomerDispatcherTactics customerDispatcherTactics;

    @Autowired
    private AccessWeChatTerminalVisitorFactory accessWeChatTerminalVisitorFactory;

    public boolean changeVisitorTo(Transferlog tm, Visitor visitor) throws BusinessException, XMPPException, IOException, SmackException {
        AbstractUser customerTo = customerDispatcherTactics.getCustomer(tm.getTransferto());

        return changeVisitorTo(tm, visitor, customerTo);

    }

    @Override
    public boolean bindVisitor(AbstractUser customerFrom, AbstractUser customer, Visitor visitor) {
        boolean flag = false;
        try {
            if (null == visitor) {
                throw new BusinessException("访客未在线");
            }
            VisitorChatNode visitorChatNode = ChatNodeManager.getVisitorXmppNode(visitor);
            ChatNodeAdaptation chatNodeAdaptation = new ChatNodeAdaptation(visitorChatNode);
            AbstractTerminal at = accessWeChatTerminalVisitorFactory.getNode(chatNodeAdaptation,visitor);

            if (null != customerFrom) {
                CustomerChatNode chatNodeFrom = ChatNodeManager.getCustomerXmppNode(customerFrom);
                visitorChatNode.setCurrentChatNode(chatNodeFrom);
            }

            CustomerChatNode chatNodeTo = ChatNodeManager.getCustomerXmppNode(customer);
            visitorChatNode.changeCurrentChatNode(chatNodeTo);

            visitorChatNode.login();
            visitorChatNode.online(at);
        } catch (Exception e) {
            logger.error("error", "", e);
        } finally {
        }
        return flag;
    }

    @Override
    public boolean changeVisitorTo(Transferlog tm, Visitor visitor, AbstractUser customerTo) throws BusinessException, XMPPException, IOException, SmackException {
        boolean flag = false;
        try {

            if (null == visitor) {
                throw new BusinessException("访客未在线");
            }
            if (!tm.getTransferto().equals(tm.getTransferfrom()) || tm.isMustDo()) {
                logger.info("Transferlog----->" + JSONUtil.toJson(tm));

                if (StringUtils.isEmpty(tm.getVisitorjid())) {
                    throw new BusinessException("手机端用户id为空");
                }
                if (StringUtils.isEmpty(tm.getTransferto())) {
                    throw new BusinessException("被转接客服账号id为空");
                }
                CustomerChatNode chatNodeTo = ChatNodeManager.getCustomerXmppNode(customerTo);

                if (chatNodeTo.isXmppOnline()) {
                    VisitorChatNode chatNode = ChatNodeManager.getVisitorXmppNode(visitor);
                    chatNode.changeCurrentChatNode(chatNodeTo);

                } else {
                    throw new BusinessException("转出客服已经下线");
                }
                tm.setStatus(flag);
            } else {
                tm.setStatus(flag);
                tm.setDetail("总客服超时，转接忽略");
            }
        } catch (BusinessException e) {
            tm.setStatus(false);
            tm.setDetail(e.getMessage());
            throw e;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            transferlogServer.insert(tm);
        }
        return flag;
    }
}
