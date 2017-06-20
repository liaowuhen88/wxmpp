package com.baodanyun.websocket.node;

import com.baodanyun.websocket.bean.msg.Msg;
import com.baodanyun.websocket.bean.msg.status.StatusMsg;
import com.baodanyun.websocket.bean.user.AbstractUser;
import com.baodanyun.websocket.enums.MsgStatus;
import com.baodanyun.websocket.exception.BusinessException;
import com.baodanyun.websocket.model.ConversationCustomer;
import com.baodanyun.websocket.node.dispatcher.CustomerDispather;
import com.baodanyun.websocket.service.ConversationCustomerService;
import com.baodanyun.websocket.util.Config;
import com.baodanyun.websocket.util.JSONUtil;
import com.baodanyun.websocket.util.SpringContextUtil;
import org.jivesoftware.smack.SmackException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;

/**
 * Created by liaowuhen on 2017/5/23.
 */
public abstract class CustomerTerminal extends AbstractTerminal implements CustomerDispather {
    private static final Logger logger = LoggerFactory.getLogger(CustomerTerminal.class);

    ConversationCustomerService conversationCustomerService = SpringContextUtil.getBean("conversationCustomerServiceImpl", ConversationCustomerService.class);

    CustomerTerminal(ChatNodeAdaptation chatNodeAdaptation, AbstractUser customer) {
        super(chatNodeAdaptation);
    }

    @Override
    public void receiveFromGod(Msg msg) throws InterruptedException, BusinessException, SmackException.NotConnectedException {

        super.receiveFromGod(msg);


    }

    @Override
    public boolean joinQueue(AbstractUser abstractUser) throws InterruptedException {
        ConversationCustomer cc = new ConversationCustomer();
        cc.setCjid(this.getAbstractUser().getId());
        cc.setVjid(abstractUser.getId());
        cc.setVisitor(JSONUtil.toJson(abstractUser));
        conversationCustomerService.insert(cc);

        return true;
    }

    @Override
    public boolean uninstall(AbstractUser abstractUser) throws InterruptedException {
        ConversationCustomer cc = new ConversationCustomer();
        cc.setCjid(this.getAbstractUser().getId());
        cc.setVjid(abstractUser.getId());
        conversationCustomerService.delete(cc);
        return true;
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


    public Msg getMsgHelloToCustomer(AbstractUser visitor) {
        logger.info("user--->" + JSONUtil.toJson(this.getAbstractUser()));
        String body = Config.greetingWord;
        Msg sendMsg = new Msg(body);
        String to = this.getAbstractUser().getId();
        String from = visitor.getId();
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

        return sendMsg;
    }


}
