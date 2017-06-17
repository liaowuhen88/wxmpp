package com.baodanyun.websocket.node;

import com.baodanyun.websocket.bean.user.AbstractUser;
import com.baodanyun.websocket.exception.BusinessException;
import com.baodanyun.websocket.service.UserCacheServer;
import com.baodanyun.websocket.util.JSONUtil;
import com.baodanyun.websocket.util.SpringContextUtil;
import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.XMPPException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * Created by liaowuhen on 2017/5/23.
 */
public class VisitorChatNode extends AbstarctChatNode {
    private static final Logger logger = LoggerFactory.getLogger(VisitorChatNode.class);
    UserCacheServer userCacheServer = SpringContextUtil.getBean("userCacheServer", UserCacheServer.class);

    private CustomerChatNode currentChatNode;

    public VisitorChatNode(AbstractUser visitor) {
        super(visitor);
    }

    public CustomerChatNode getCurrentChatNode() {
        return currentChatNode;
    }

    public void setCurrentChatNode(CustomerChatNode currentChatNode) throws BusinessException {
        this.currentChatNode = currentChatNode;
        userCacheServer.addVisitorCustomerOpenId(this.getAbstractUser().getOpenId(), currentChatNode.getAbstractUser().getId());
    }


    @Override
    public void online(AbstractTerminal node) throws InterruptedException, BusinessException {
        super.online(node);
        this.getCurrentChatNode().joinQueue(this.getAbstractUser());
    }


    public ChatNode changeCurrentChatNode(CustomerChatNode currentChatNode) throws BusinessException {
        CustomerChatNode old =  this.currentChatNode;

        if(old.getId().equals(currentChatNode.getId())){
            return currentChatNode;
        }
        if (this.isXmppOnline()) {
            if(null != old){
                if (!old.uninstall(this.getAbstractUser())) {
                    throw new BusinessException("从当前客服卸载失败");
                }
            }

            if (!currentChatNode.joinQueue(this.getAbstractUser())) {
                throw new BusinessException("加入到目标客服失败");
            }
        } else {

            logger.info(JSONUtil.toJson(this.getAbstractUser()));
            currentChatNode.joinQueue(this.getAbstractUser());

        }

        setCurrentChatNode(currentChatNode);

        return old;
    }


    @Override
    public boolean login() throws BusinessException, IOException, XMPPException, SmackException {
        boolean flag = super.login();

        initVCard();

        return flag;
    }


}
