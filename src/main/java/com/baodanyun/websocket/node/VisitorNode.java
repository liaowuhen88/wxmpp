package com.baodanyun.websocket.node;

import com.baodanyun.websocket.bean.msg.Msg;
import com.baodanyun.websocket.bean.msg.status.StatusMsg;
import com.baodanyun.websocket.bean.user.AbstractUser;
import com.baodanyun.websocket.bean.user.Visitor;
import com.baodanyun.websocket.enums.MsgStatus;
import com.baodanyun.websocket.event.VisitorLoginEvent;
import com.baodanyun.websocket.event.VisitorReciveMsgEvent;
import com.baodanyun.websocket.exception.BusinessException;
import com.baodanyun.websocket.node.dispatcher.VisitorDispather;
import com.baodanyun.websocket.node.xmpp.VisitorXmppNode;
import com.baodanyun.websocket.util.CommonConfig;
import com.baodanyun.websocket.util.Config;
import com.baodanyun.websocket.util.EventBusUtils;
import com.baodanyun.websocket.util.JSONUtil;
import org.apache.log4j.Logger;
import org.jivesoftware.smack.SmackException;

import java.util.Date;

/**
 * Created by liaowuhen on 2017/5/23.
 */
public abstract class VisitorNode extends AbstractNode implements VisitorDispather {
    private static final Logger logger = Logger.getLogger(VisitorNode.class);
    private Visitor visitor;

    public VisitorNode(Visitor visitor) {
        this.visitor = visitor;
    }

    @Override
    public AbstractUser getAbstractUser() {
        return visitor;
    }


    public StatusMsg getSMMsgSendTOVisitor(AbstractUser visitor, MsgStatus status) {
        StatusMsg statusSysMsg = StatusMsg.buildStatus(Msg.Type.status);
        if (null != visitor) {
            statusSysMsg.setToName(visitor.getNickName());
            statusSysMsg.setTo(visitor.getId());
            statusSysMsg.setLoginUsername(visitor.getLoginUsername());
            statusSysMsg.setCt(new Date().getTime());
            statusSysMsg.setOpenId(visitor.getOpenId());
        }
        statusSysMsg.setStatus(status);
        return statusSysMsg;
    }

    @Override
    public void receiveFromGod(Msg msg) throws InterruptedException, BusinessException, SmackException.NotConnectedException {
        super.receiveFromGod(msg);

        VisitorReciveMsgEvent vme = new VisitorReciveMsgEvent(this.getAbstractUser(), ((Visitor) this.getAbstractUser()).getCustomer(), msg.getContent(), CommonConfig.MSG_BIZ_KF_WX_CHAT);

        EventBusUtils.post(vme);

    }

    @Override
    public void online() throws InterruptedException {
        VisitorLoginEvent vl = new VisitorLoginEvent(this.getAbstractUser(), ((Visitor) this.getAbstractUser()).getCustomer(), null);

        EventBusUtils.post(vl);
    }

    public Msg getMsgHelloToVisitor(Visitor user) {
        logger.info("user--->" + JSONUtil.toJson(user));
        String body = Config.greetingWord;
        Msg sendMsg = new Msg(body);
        String from;

        String to = user.getId();

        String type = Msg.Type.msg.toString();
        Long ct = new Date().getTime();

        sendMsg.setType(type);
        sendMsg.setContentType(Msg.MsgContentType.text.toString());

        from = visitor.getCustomer().getId();
        sendMsg.setFrom(from);
        sendMsg.setIcon(visitor.getCustomer().getIcon());
        sendMsg.setFromName(visitor.getCustomer().getNickName());


        sendMsg.setTo(to);
        sendMsg.setCt(ct);

        return sendMsg;
    }

    @Override
    public boolean joinQueue() throws InterruptedException {
        logger.info("保存到缓存--->" + userCacheServer.add(CommonConfig.USER_VISITOR, this.getAbstractUser()));
        return ((VisitorXmppNode) this.getXmppNode()).joinQueue();
    }

    @Override
    public boolean uninstall() throws InterruptedException {
        return ((VisitorXmppNode) this.getXmppNode()).uninstall();
    }

    /*@Override
    public void onlinePush() throws BusinessException, InterruptedException {
        VisitorLoginEvent vl = new VisitorLoginEvent(visitor, visitor.getCustomer(), this.getMsgSendService());

        EventBusUtils.post(vl);


        joinQueue();

        pushOfflineMsg();

    }*/

}
