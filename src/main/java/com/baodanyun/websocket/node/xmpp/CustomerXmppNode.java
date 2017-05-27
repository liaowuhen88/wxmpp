package com.baodanyun.websocket.node.xmpp;

import com.baodanyun.websocket.bean.user.AbstractUser;
import com.baodanyun.websocket.exception.BusinessException;
import com.baodanyun.websocket.node.CustomerNode;
import com.baodanyun.websocket.node.Node;
import com.baodanyun.websocket.node.dispatcher.CustomerDispather;
import com.baodanyun.websocket.service.CustomerDispatcherService;
import com.baodanyun.websocket.util.SpringContextUtil;
import org.apache.log4j.Logger;
import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.XMPPException;

import java.io.IOException;

/**
 * Created by liaowuhen on 2017/5/23.
 */
public class CustomerXmppNode extends AbstarctXmppNode implements CustomerDispather {

    private static final Logger logger = Logger.getLogger(CustomerXmppNode.class);
    CustomerDispatcherService customerDispatcherService = SpringContextUtil.getBean("customerDispatcherServiceImpl", CustomerDispatcherService.class);

    public CustomerXmppNode(AbstractUser customer) {
        super(customer);
    }

    @Override
    public boolean logout() throws BusinessException, IOException, XMPPException, SmackException {

        customerDispatcherService.deleteCustomer(this.getAbstractUser().getId());

        return super.logout();
    }

    public boolean joinQueue(AbstractUser abstractUser) {
        if (null != getNodes()) {
            for (Node node : getNodes()) {
                try {
                    ((CustomerNode) node).joinQueue(abstractUser);
                } catch (InterruptedException e) {
                    logger.error(e);
                }
            }
        } else {
            logger.info("joinQueue getNodes() is null");
        }

        return true;
    }

    public boolean uninstall(AbstractUser abstractUser) {
        if (null != getNodes()) {
            for (Node node : getNodes()) {
                try {
                    ((CustomerNode) node).uninstall(abstractUser);
                } catch (InterruptedException e) {
                    logger.error(e);
                }
            }
        } else {
            logger.info("joinQueue getNodes() is null");
        }

        return true;
    }

}
