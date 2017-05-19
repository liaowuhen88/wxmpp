package com.baodanyun.websocket.service.impl.lifecycle;

import com.baodanyun.websocket.bean.msg.Msg;
import com.baodanyun.websocket.bean.user.AbstractUser;
import com.baodanyun.websocket.bean.user.Visitor;
import com.baodanyun.websocket.enums.MsgStatus;
import com.baodanyun.websocket.event.VisitorJoinEvent;
import com.baodanyun.websocket.event.VisitorLoginEvent;
import com.baodanyun.websocket.event.VisitorReciveMsgEvent;
import com.baodanyun.websocket.exception.BusinessException;
import com.baodanyun.websocket.listener.impl.VisitorJoinListener;
import com.baodanyun.websocket.listener.impl.VisitorLoginListener;
import com.baodanyun.websocket.util.EventBusUtils;
import com.baodanyun.websocket.util.JSONUtil;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.XMPPException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by liaowuhen on 2017/3/6.
 */
@Service
public abstract class VisitorUserLifeCycleServiceImpl extends UserLifeCycleServiceImpl {
    private static final Logger logger = Logger.getLogger(VisitorUserLifeCycleServiceImpl.class);

    @Autowired
    protected VisitorLoginListener visitorLoginListener;

    @Autowired
    protected VisitorJoinListener visitorJoinListener;

    @Override
    public boolean online(AbstractUser user) throws InterruptedException, BusinessException, SmackException.NotLoggedInException, XMPPException.XMPPErrorException, SmackException.NotConnectedException, SmackException.NoResponseException {
        super.online(user);

        VisitorLoginEvent vl = new VisitorLoginEvent(user, ((Visitor) user).getCustomer(), getMsgSendService());

        EventBusUtils.post(vl);

        boolean flag = joinQueue(user);
        return flag;
    }

    @Override
    public boolean joinQueue(AbstractUser user) throws InterruptedException {

        VisitorJoinEvent ve = new VisitorJoinEvent(user, ((Visitor) user).getCustomer(), getMsgSendService());

        EventBusUtils.post(ve);

        return true;

    }

    /**
     * 参数为访客
     *
     * @param user
     * @return
     * @throws InterruptedException
     */
    @Override
    public boolean uninstallVisitor(AbstractUser user) throws InterruptedException {

        AbstractUser customer;
        try {
            customer = userCacheServer.getCustomerByVisitorOpenId(user.getOpenId());
            getMsgSendService().sendSMMsgToCustomer(user, customer, MsgStatus.changeOffline);
        } catch (BusinessException e) {
            logger.error(e);
        }


        return true;
    }

    @Override
    public Msg getMsg(AbstractUser user, String content) {
        Visitor visitor = (Visitor) user;
        if (!StringUtils.isEmpty(content)) {
            Msg msg = Msg.handelMsg(content);
            // 因为有转接，所有目的地址需要重新设置
            String destId = null;
            try {
                destId = userCacheServer.getCustomerIdByVisitorOpenId(visitor.getOpenId());
                msg.setTo(destId);
            } catch (BusinessException e) {
                logger.error(e);
            }

            msg.setFrom(visitor.getId());
            if (msg != null && !StringUtils.isEmpty(msg.getFrom()) && !StringUtils.isEmpty(destId)) {

                return msg;
            } else {
                logger.error("handleSendMsg from or to is blank" + JSONUtil.toJson(msg));
            }
        } else {
            logger.error("content is empty");
        }
        return null;
    }


    @Override
    public Msg receiveMessage(AbstractUser user, String content) throws InterruptedException, SmackException.NotConnectedException, BusinessException {
        Msg msg = super.receiveMessage(user, content);
        VisitorReciveMsgEvent vme = new VisitorReciveMsgEvent(user, ((Visitor) user).getCustomer(), msg.getContent());

        EventBusUtils.post(vme);
        return msg;
    }

   /* @Override
    public boolean sendMessage(AbstractUser user, Msg msg) throws InterruptedException {
        vcardService.getVCardUser(user.getId());
        StatusMsg mg = this.getMsgSendService().getSMMsgSendTOVisitor(user, visitor.getCustomer());
        mg.setContentType(msg.getContentType());
        mg.setContent(msg.getContent());
        mg.setType(Msg.Type.msg.toString());

        this.getMsgSendService().produce(mg);
        return true;
    }*/
}
