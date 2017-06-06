package com.baodanyun.websocket.service.impl;

import com.baodanyun.websocket.bean.user.AbstractUser;
import com.baodanyun.websocket.bean.user.Customer;
import com.baodanyun.websocket.bean.user.Visitor;
import com.baodanyun.websocket.exception.BusinessException;
import com.baodanyun.websocket.model.Transferlog;
import com.baodanyun.websocket.node.NodeManager;
import com.baodanyun.websocket.node.VisitorNode;
import com.baodanyun.websocket.node.xmpp.VisitorXmppNode;
import com.baodanyun.websocket.node.xmpp.XmppNodeManager;
import com.baodanyun.websocket.service.*;
import com.baodanyun.websocket.util.JSONUtil;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
    private UserCacheServer userCacheServer;

    @Autowired
    private MsgSendControl msgSendControl;

    public boolean changeVisitorTo(Transferlog tm, Visitor visitor) throws BusinessException {
        Customer customer = userCacheServer.getUserCustomer(tm.getTransferto());

        return changeVisitorTo(tm, visitor, customer);

    }

    @Override
    public boolean bindVisitor(AbstractUser customerFrom, AbstractUser customer, Visitor visitor) {
        boolean flag = false;
        try {
            if (null == visitor) {
                throw new BusinessException("访客未在线");
            }
            VisitorNode wn = NodeManager.getWeChatNode(visitor);

            if (wn.getXmppNode().isOnline()) {
                if (!wn.uninstall()) {
                    throw new BusinessException("从当前客服卸载失败");
                }
                visitor.setCustomer(customer);
                userCacheServer.addVisitorCustomerOpenId(visitor.getOpenId(), customer.getId());

                if (!wn.joinQueue()) {
                    throw new BusinessException("加入到目标客服失败");
                }
            } else {
                visitor.setCustomer(customer);
                userCacheServer.addVisitorCustomerOpenId(visitor.getOpenId(), customer.getId());
                try {
                    logger.info(JSONUtil.toJson(visitor));
                    String pwd = "00818863ff056f1d66c8427836f94a87";
                    visitor.setPassWord(pwd);
                    wn.login();
                    wn.joinQueue();
                } catch (Exception e) {
                    logger.error("", e);
                }
            }

        } catch (Exception e) {
            logger.error("", e);
        } finally {
        }
        return flag;
    }

    @Override
    public boolean changeVisitorTo(Transferlog tm, Visitor visitor, Customer customer) throws BusinessException {
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

                boolean toflag = xmppServer.isAuthenticated(tm.getTransferto());

                if (toflag) {

                    AbstractUser customerTo = userCacheServer.getUserCustomer(tm.getTransferto());

                    boolean vflag = xmppServer.isAuthenticated(tm.getVisitorjid());


                    VisitorXmppNode node = XmppNodeManager.getVisitorXmppNode(visitor);

                    if (vflag) {
                        if (!node.uninstall()) {
                            throw new BusinessException("从当前客服卸载失败");
                        }
                        visitor.setCustomer(customerTo);
                        userCacheServer.addVisitorCustomerOpenId(visitor.getOpenId(), customerTo.getId());

                        if (!node.joinQueue()) {
                            throw new BusinessException("加入到目标客服失败");
                        }
                    } else {
                        visitor.setCustomer(customerTo);

                        userCacheServer.addVisitorCustomerOpenId(visitor.getOpenId(), customerTo.getId());
                        try {
                            logger.info(JSONUtil.toJson(visitor));
                            String pwd = "00818863ff056f1d66c8427836f94a87";
                            visitor.setPassWord(pwd);
                            node.login();
                            node.joinQueue();
                        } catch (Exception e) {
                            logger.error("", e);
                        }
                    }
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
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            transferlogServer.insert(tm);
        }
        return flag;
    }
}
