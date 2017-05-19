package com.baodanyun.websocket.service;

import com.baodanyun.websocket.bean.msg.Msg;
import com.baodanyun.websocket.bean.user.AbstractUser;
import com.baodanyun.websocket.bean.user.VCardUser;
import com.baodanyun.websocket.bean.user.Visitor;
import com.baodanyun.websocket.util.Config;
import com.baodanyun.websocket.util.JSONUtil;
import com.baodanyun.websocket.util.XMPPUtil;
import org.apache.commons.lang.StringUtils;
import org.jivesoftware.smack.packet.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * Created by liaowuhen on 2017/1/12.
 * <p/>
 * 把消息发送到客服pc端的消息队列控制器
 */
@Service
public class MsgSendControl {
    protected final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    public WebSocketService webSocketService;

    @Autowired
    public MessageSendToWeixin messageSendToWeixin;

    @Autowired
    public UserServer userServer;

    @Autowired
    public UserCacheServer userCacheServer;

    @Autowired
    private VcardService vcardService;

    public boolean sendMsg(Msg msg) throws InterruptedException {

        if (webSocketService.hasH5Connected(msg.getTo())) {

            VCardUser vcard = null;
            try {
                vcard = vcardService.getVCardUser(msg.getFrom(), msg.getTo());
                if (null != vcard) {
                    if (!StringUtils.isEmpty(vcard.getIcon())) {
                        logger.info("msg.getFrom() " + msg.getFrom() + "----------user icon is null");
                    } else {
                        msg.setIcon(vcard.getIcon());
                    }

                    if (StringUtils.isEmpty(vcard.getNickName())) {
                        logger.info("msg.getFrom() " + msg.getFrom() + "----------user NickName is null");
                    } else {
                        msg.setFromName(vcard.getNickName());
                    }
                }

            } catch (Exception e) {
                logger.error("", e);
            }
            webSocketService.produce(msg);
        } else {
            try {
                messageSendToWeixin.send(msg, msg.getOpenId(), msg.getId());
                logger.info("msg---" + JSONUtil.toJson(msg) + "send to Weixin ");
            } catch (Exception e) {
                logger.error("", e);
            }
        }
        return true;
    }

    public Msg getMsg(Message msg, AbstractUser user) {
        Msg sendMsg = null;
        String body = msg.getBody();
        if (StringUtils.isNotBlank(body)) {
            sendMsg = new Msg(body);
            String from = XMPPUtil.removeSource(msg.getFrom());
            String to = XMPPUtil.removeSource(msg.getTo());
            String type = Msg.Type.msg.toString();
            Long ct = new Date().getTime();

            sendMsg.setType(type);
            sendMsg.setFrom(from);
            sendMsg.setTo(to);
            sendMsg.setCt(ct);
            sendMsg.setOpenId(user.getOpenId());

            if (StringUtils.isEmpty(msg.getSubject())) {
                sendMsg.setContentType("text");
            } else {
                sendMsg.setContentType(msg.getSubject());
            }
        }
        return sendMsg;
    }

    /**
     * 欢迎语
     * 客服
     *
     * @param
     * @return
     */

    public Msg getMsgHelloToCustomer(AbstractUser visitor, AbstractUser customer) {
        logger.info("user--->" + JSONUtil.toJson(customer));
        String body = Config.greetingWord;
        Msg sendMsg = new Msg(body);
        String to = customer.getId();
        String from = visitor.getId();
        String type = Msg.Type.msg.toString();
        Long ct = new Date().getTime();

        sendMsg.setType(type);
        sendMsg.setContentType(Msg.MsgContentType.text.toString());
        sendMsg.setTo(to);
        sendMsg.setFrom(from);
        sendMsg.setCt(ct);
        // 获取发送端用户
        sendMsg.setIcon(customer.getIcon());
        sendMsg.setFromName(customer.getNickName());

        return sendMsg;
    }

    public Msg getMsgHelloToVisitor(Visitor user, AbstractUser customer) {
        logger.info("user--->" + JSONUtil.toJson(user));
        String body = Config.greetingWord;
        Msg sendMsg = new Msg(body);
        String from;

        String to = user.getId();
        if (user.getType() == 1) {
            to = XMPPUtil.removeSource(user.getOpenId());
            sendMsg.setToType(user.getType());
        } else {
            sendMsg.setToType(0);
        }

        String type = Msg.Type.msg.toString();
        Long ct = new Date().getTime();

        sendMsg.setType(type);
        sendMsg.setContentType(Msg.MsgContentType.text.toString());

        from = customer.getId();
        sendMsg.setFrom(from);
        sendMsg.setIcon(customer.getIcon());
        sendMsg.setFromName(customer.getNickName());


        sendMsg.setTo(to);
        sendMsg.setCt(ct);

        return sendMsg;
    }

}
