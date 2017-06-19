package com.baodanyun.websocket.node;

import com.baodanyun.websocket.bean.user.AbstractUser;
import com.baodanyun.websocket.bean.user.NewCustomer;
import com.baodanyun.websocket.enums.MsgStatus;
import com.baodanyun.websocket.exception.BusinessException;
import com.baodanyun.websocket.node.dispatcher.CustomerDispather;
import com.baodanyun.websocket.service.CustomerDispatcherService;
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
    CustomerDispatcherService customerDispatcherService = SpringContextUtil.getBean("customerDispatcherServiceImpl", CustomerDispatcherService.class);
    public CustomerChatNode(AbstractUser customer) {
        super(customer);
    }

    @Override
    public boolean logout() {

        customerDispatcherService.deleteCustomer(this.getAbstractUser().getId());

        return super.logout();
    }

    public boolean joinQueue(AbstractUser abstractUser) {
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

    public boolean uninstall(AbstractUser abstractUser) {
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

    public boolean messageCallBack(AbstractUser abstractUser, MsgStatus msgStatus) throws InterruptedException {
        if (null != getNodes()) {
            for (AbstractTerminal node : getNodes().values()) {
                try {
                    ((CustomerTerminal) node).messageCallBack(abstractUser, msgStatus);
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
    public boolean login() throws BusinessException, IOException, XMPPException, SmackException {
        // 是否为接入用户客服
        boolean flag = super.login();
        if (flag) {
            if (!StringUtils.isEmpty(((NewCustomer) this.getAbstractUser()).getAccessType()) && ((NewCustomer) this.getAbstractUser()).getAccessType().equals("2")) {
                customerDispatcherService.saveCustomer(this.getAbstractUser());
            } else {
                logger.info("不接入用户");
            }
        }
        return flag;
    }
}
