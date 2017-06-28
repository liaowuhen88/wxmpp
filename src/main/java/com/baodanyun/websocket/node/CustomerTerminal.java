package com.baodanyun.websocket.node;

import com.baodanyun.websocket.bean.msg.Msg;
import com.baodanyun.websocket.bean.msg.status.StatusMsg;
import com.baodanyun.websocket.bean.user.AbstractUser;
import com.baodanyun.websocket.enums.MsgStatus;
import com.baodanyun.websocket.util.Config;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;

/**
 * Created by liaowuhen on 2017/5/23.
 */
public abstract class CustomerTerminal extends AbstractTerminal {
    private static final Logger logger = LoggerFactory.getLogger(CustomerTerminal.class);

    CustomerTerminal(ChatNodeAdaptation chatNodeAdaptation) {
        super(chatNodeAdaptation);
    }


    public StatusMsg getSMMsgSendTOCustomer(MsgStatus status) {
        StatusMsg statusSysMsg = StatusMsg.buildStatus(Msg.Type.status);
        statusSysMsg.setStatus(status);
        statusSysMsg.setTo(this.getAbstractUser().getId());
        return statusSysMsg;
    }

    public StatusMsg getSMMsgOnlineSendTOCustomer(AbstractUser visitor, MsgStatus status) {
        StatusMsg statusSysMsg = StatusMsg.buildStatus(Msg.Type.status);
        if (null != visitor) {
            statusSysMsg.setFromName(visitor.getNickName());
            statusSysMsg.setFrom(visitor.getId());
            statusSysMsg.setIcon(visitor.getIcon());
            statusSysMsg.setOpenId(visitor.getOpenId());
            statusSysMsg.setLoginUsername(visitor.getLoginUsername());
            statusSysMsg.setLoginTime(visitor.getLoginTime());
        }
        statusSysMsg.setStatus(status);
        statusSysMsg.setTo(this.getAbstractUser().getId());
        return statusSysMsg;
    }


    /**
     * 客服发送给用户的信息
     * <p/>
     * from 为客服
     * <p/>
     * to为用户
     *
     * @param visitor
     * @return
     */
    public Msg getMsgHelloToCustomer(AbstractUser visitor) {

        String body = Config.greetingWord;
        Msg sendMsg = new Msg(body);
        String from = this.getAbstractUser().getId();
        String to = visitor.getId();
        String type = Msg.Type.msg.toString();
        Long ct = new Date().getTime();

        sendMsg.setType(type);
        sendMsg.setContentType(Msg.MsgContentType.text.toString());
        sendMsg.setTo(to);
        sendMsg.setFrom(from);
        sendMsg.setCt(ct);
        // 获取发送端用户
        sendMsg.setIcon(this.getAbstractUser().getIcon());
        sendMsg.setFromName(this.getAbstractUser().getNickName());
        sendMsg.setMsgType("synchronize");
        return sendMsg;
    }

    public abstract boolean joinQueue(AbstractUser abstractUser);

    public abstract boolean uninstall(AbstractUser abstractUser);

    public abstract boolean visitorOffline(AbstractUser abstractUser);
}
