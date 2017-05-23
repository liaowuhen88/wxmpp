package com.baodanyun.websocket.node.xmpp;

import com.baodanyun.websocket.bean.user.AbstractUser;
import com.baodanyun.websocket.bean.user.Visitor;
import com.baodanyun.websocket.exception.BusinessException;
import com.baodanyun.websocket.node.Node;
import org.apache.log4j.Logger;
import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.XMPPException;

import java.io.IOException;

/**
 * Created by liaowuhen on 2017/5/23.
 */
public class VisitorXmppNode extends AbstarctXmppNode {
    private static final Logger logger = Logger.getLogger(VisitorXmppNode.class);

    public VisitorXmppNode(Visitor visitor) {
        super(visitor);
    }

    @Override
    public boolean login() throws IOException, XMPPException, SmackException, BusinessException {
        if (!isOnline()) {
            boolean flag = super.login();
            initVCard();
            return flag;
        }
        return true;
    }


    @Override
    public void initVCard() {
        AbstractUser vCard = null;
        try {
            vCard = vcardService.getVCardUser(getAbstractUser().getId(), getAbstractUser().getId(), AbstractUser.class);
        } catch (Exception e) {
            logger.error(e);
        }

        if (null == vCard) {
            vcardService.updateBaseVCard(getAbstractUser().getId(), getAbstractUser());
        }
    }


    public boolean joinQueue() throws InterruptedException {
        if (null != getNodes()) {
            for (Node node : getNodes()) {
                node.joinQueue();
            }
        }

        return true;
    }

    public boolean uninstall() throws InterruptedException {
        if (null != getNodes()) {
            for (Node node : getNodes()) {
                node.uninstall();
            }
        }
        return true;
    }

    public boolean onlinePush() throws InterruptedException {
        if (null != getNodes()) {
            for (Node node : getNodes()) {
                try {
                    node.onlinePush();
                } catch (BusinessException e) {
                    logger.error(e);
                }
            }
        }
        return true;
    }


}
