package com.baodanyun.websocket.node.xmpp;

import com.baodanyun.websocket.bean.user.AbstractUser;
import com.baodanyun.websocket.bean.user.Visitor;
import com.baodanyun.websocket.exception.BusinessException;
import com.baodanyun.websocket.node.dispatcher.VisitorDispather;
import org.apache.log4j.Logger;
import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.XMPPException;

import java.io.IOException;

/**
 * Created by liaowuhen on 2017/5/23.
 */
public class VisitorXmppNode extends AbstarctXmppNode implements VisitorDispather {
    private static final Logger logger = Logger.getLogger(VisitorXmppNode.class);

    public VisitorXmppNode(AbstractUser visitor) {
        super(visitor);
    }

    @Override
    public boolean login() throws BusinessException, IOException, XMPPException, SmackException {
        boolean flag = super.login();

        initVCard();

        return flag;
    }

    @Override
    public boolean joinQueue() throws InterruptedException {
        AbstractUser customer = ((Visitor) this.getAbstractUser()).getCustomer();
        return XmppNodeManager.getCustomerXmppNode(customer).joinQueue(this.getAbstractUser());
    }

    @Override
    public boolean uninstall() throws InterruptedException {
        AbstractUser customer = ((Visitor) this.getAbstractUser()).getCustomer();
        return null == customer || XmppNodeManager.getCustomerXmppNode(customer).uninstall(this.getAbstractUser());

    }


}
