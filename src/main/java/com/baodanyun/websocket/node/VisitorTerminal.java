package com.baodanyun.websocket.node;

import com.baodanyun.websocket.bean.msg.Msg;
import com.baodanyun.websocket.bean.msg.status.StatusMsg;
import com.baodanyun.websocket.bean.user.AbstractUser;
import com.baodanyun.websocket.bean.user.Visitor;
import com.baodanyun.websocket.enums.MsgStatus;
import com.baodanyun.websocket.event.VisitorLoginEvent;
import com.baodanyun.websocket.exception.BusinessException;
import com.baodanyun.websocket.util.Config;
import com.baodanyun.websocket.util.EventBusUtils;
import com.baodanyun.websocket.util.JSONUtil;
import org.jivesoftware.smack.SmackException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;

/**
 * Created by liaowuhen on 2017/5/23.
 */
public abstract class VisitorTerminal extends AbstractTerminal {
    private static final Logger logger = LoggerFactory.getLogger(VisitorTerminal.class);

    VisitorTerminal(ChatNodeAdaptation chatNodeAdaptation) {
        super(chatNodeAdaptation);
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


    }

    @Override
    public void online() throws InterruptedException, BusinessException {
        super.online();

        VisitorLoginEvent vl = new VisitorLoginEvent(this.getAbstractUser(), this.getChatNodeAdaptation().getAbstractUser(), null);

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

        AbstractUser customer = this.getChatNodeAdaptation().getAbstractUser();
        from = customer.getId();
        sendMsg.setFrom(from);
        sendMsg.setIcon(customer.getIcon());
        sendMsg.setFromName(customer.getNickName());


        sendMsg.setTo(to);
        sendMsg.setCt(ct);

        return sendMsg;
    }

    /**
     * 加入客服队列成功
     *
     * @return
     * @throws InterruptedException
     */

    abstract boolean joinQueue();


    /**
     * 被客服移除
     *
     * @return
     * @throws InterruptedException
     */
    abstract boolean uninstall() throws InterruptedException;


    /**
     * 客服上线
     */
    abstract boolean customerOnline();

    /**
     * 客服上线
     */
    abstract boolean customerOffline();


}
