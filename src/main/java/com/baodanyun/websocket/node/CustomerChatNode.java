package com.baodanyun.websocket.node;

import com.baodanyun.websocket.bean.user.AbstractUser;
import com.baodanyun.websocket.bean.user.Customer;
import com.baodanyun.websocket.enums.MsgStatus;
import com.baodanyun.websocket.exception.BusinessException;
import com.baodanyun.websocket.model.ConversationCustomer;
import com.baodanyun.websocket.node.dispatcher.CustomerDispather;
import com.baodanyun.websocket.service.ConversationCustomerService;
import com.baodanyun.websocket.service.CustomerDispatcherService;
import com.baodanyun.websocket.service.UserCacheServer;
import com.baodanyun.websocket.util.JSONUtil;
import com.baodanyun.websocket.util.SpringContextUtil;
import org.apache.commons.lang.StringUtils;
import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.XMPPException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * Created by liaowuhen on 2017/5/23.
 */
public class CustomerChatNode extends AbstarctChatNode implements CustomerDispather {

    private static final Logger logger = LoggerFactory.getLogger(CustomerChatNode.class);

    UserCacheServer userCacheServer = SpringContextUtil.getBean("userCacheServerImpl", UserCacheServer.class);
    ConversationCustomerService conversationCustomerService = SpringContextUtil.getBean("conversationCustomerServiceImpl", ConversationCustomerService.class);
    CustomerDispatcherService customerDispatcherService = SpringContextUtil.getBean("customerDispatcherServiceImpl", CustomerDispatcherService.class);

    public CustomerChatNode(AbstractUser customer, Long lastActiveTime) {
        super(customer, lastActiveTime);
    }

    @Override
    public boolean logout() {

        customerDispatcherService.deleteCustomer(this.getAbstractUser().getId());

        return super.logout();
    }

    public boolean joinQueue(AbstractUser abstractUser) {
        ConversationCustomer cc = new ConversationCustomer();
        cc.setCjid(this.getAbstractUser().getId());
        cc.setVjid(abstractUser.getId());
        cc.setVisitor(JSONUtil.toJson(abstractUser));
        conversationCustomerService.insert(cc);

        if (null != getNodes()) {
            for (AbstractTerminal node : getNodes().values()) {
                try {
                    ((CustomerTerminal) node).joinQueue(abstractUser);
                } catch (InterruptedException e) {
                    logger.error("error", e);
                }
            }
        } else {
            logger.info("joinQueue getNodes() is null");
        }

        return true;
    }

    /**
     * 当客服所有节点移除，默认两秒之后并且没有新的节点加入时，关闭客服节点，确保消息不丢失
     *
     * @param id
     * @return
     * @throws IOException
     * @throws XMPPException
     * @throws SmackException
     * @throws BusinessException
     */
    @Override
    public AbstractTerminal removeNode(String id) throws IOException, XMPPException, SmackException, BusinessException {
        AbstractTerminal at = super.removeNode(id);
        try {
            Thread.sleep(1000 * 2);
        } catch (InterruptedException e) {
            logger.error("error", e);
        }

        if (this.getNodes().size() < 1) {
            logger.info("customer 没有中断在线，关闭");
            this.logout();
        }
        return at;
    }

    public boolean uninstall(AbstractUser abstractUser) {
        ConversationCustomer cc = new ConversationCustomer();
        cc.setCjid(this.getAbstractUser().getId());
        cc.setVjid(abstractUser.getId());
        conversationCustomerService.delete(cc);

        if (null != getNodes()) {
            for (AbstractTerminal node : getNodes().values()) {
                try {
                    ((CustomerTerminal) node).uninstall(abstractUser);
                } catch (InterruptedException e) {
                    logger.error("error", e);
                }
            }
        } else {
            logger.info("joinQueue getNodes() is null");
        }

        return true;
    }

    @Override
    public boolean messageCallBack(AbstractUser abstractUser, MsgStatus msgStatus) throws InterruptedException {
        if (null != getNodes()) {
            for (AbstractTerminal node : getNodes().values()) {
                try {
                    node.messageCallBack(abstractUser, msgStatus);
                } catch (InterruptedException e) {
                    logger.error("error", e);
                }
            }
        } else {
            logger.info("joinQueue getNodes() is null");
        }

        return true;
    }

    /**
     * 登录，并且保存所有客服，以及接入用户客服
     * @return
     * @throws BusinessException
     * @throws IOException
     * @throws XMPPException
     * @throws SmackException
     */
    @Override
    public boolean login() throws BusinessException, IOException, XMPPException, SmackException {
        // 是否为接入用户客服
        boolean flag = super.login();
        if (flag) {
            if (!StringUtils.isEmpty(((Customer) this.getAbstractUser()).getAccessType()) && ((Customer) this.getAbstractUser()).getAccessType().equals("2")) {
                customerDispatcherService.saveCustomer(this.getAbstractUser());
            } else {
                logger.info("不接入用户");
            }
        }
        userCacheServer.addCustomer(this.getAbstractUser());
        return flag;
    }
}
