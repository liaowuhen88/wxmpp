package com.baodanyun.websocket.controller;

import com.baodanyun.websocket.bean.dubbo.FollowWXResponse;
import com.baodanyun.websocket.bean.dubbo.QRCodeResponse;
import com.baodanyun.websocket.bean.msg.Msg;
import com.baodanyun.websocket.bean.msg.msg.TextMsg;
import com.baodanyun.websocket.bean.request.ProductConsultation;
import com.baodanyun.websocket.bean.user.AbstractUser;
import com.baodanyun.websocket.enums.TeminalTypeEnum;
import com.baodanyun.websocket.exception.BusinessException;
import com.baodanyun.websocket.node.*;
import com.baodanyun.websocket.service.MessageSendToWeixin;
import com.baodanyun.websocket.service.OffLineMessageService;
import com.baodanyun.websocket.service.UserServer;
import com.baodanyun.websocket.service.dubbo.WechatService;
import com.baodanyun.websocket.service.dubbo.WxQRCodeService;
import com.baodanyun.websocket.util.Config;
import com.baodanyun.websocket.util.JSONUtil;
import org.apache.commons.lang.SerializationUtils;
import org.apache.commons.lang.StringUtils;
import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.XMPPException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by yutao on 2016/10/4.
 * <p/>
 * 接收微信信息入口等等
 */
@RestController
public class ConsultationApi extends BaseController {
    protected static Logger logger = LoggerFactory.getLogger(ConsultationApi.class);

    @Autowired
    private UserServer userServer;

    @Autowired
    private OffLineMessageService offLineMessageService;

    @Autowired
    private MessageSendToWeixin messageSendToWeixin;

    @Autowired
    private WeChatTerminalVisitorFactory weChatTerminalVisitorFactory;

    @Autowired
    private WxQRCodeService wxQRCodeService;
    @Autowired
    private WechatService wechatService;

    @RequestMapping(value = "consultation")
    public ModelAndView getMessageByCId(ProductConsultation pc, HttpServletRequest request, HttpServletResponse httpServletResponse) {
        ModelAndView mv = new ModelAndView();
        mv.setViewName("/consultation.html");

        try {
            logger.info(JSONUtil.toJson(pc));
            FollowWXResponse fw = wechatService.getFollow(pc.getU());
            AbstractUser visitor = userServer.initUserByOpenId(pc.getU());
            mv.addObject("follow", fw.isFollow());
            String customerBody;
            String visitorBody = null;
            if (!fw.isFollow()) {
                customerBody = "未关注豆包管家用户咨询客服";
                //TODO 请求关注二维码
                QRCodeResponse qc = wxQRCodeService.getQRCodeTempUrl(pc.getName(), "", pc.getGoodUrl(), (byte) 5);
                if (null != qc && StringUtils.isNotEmpty(qc.getUrl())) {
                    mv.addObject("url", qc.getUrl());
                }
                offLineMessageService.dealUnRegisterMessage(visitor, customerBody);
                logger.info("{} 未关注", visitor.getOpenId());
            } else {
                VisitorChatNode visitorChatNode = initVisitorChatNode(visitor);
                AbstractTerminal node = visitorChatNode.getNode(weChatTerminalVisitorFactory.getId(visitorChatNode.getAbstractUser()));

                CustomerChatNode customerChatNode = visitorChatNode.getCurrentChatNode();
                boolean cFlag = visitorChatNode.getCurrentChatNode().isXmppOnline();
                logger.info("客服是否在线" + cFlag);

                if (!cFlag) {
                    customerBody = Config.offlineWord;
                } else {
                    if (StringUtils.isEmpty(pc.getName()) || StringUtils.isEmpty(pc.getGoodUrl())) {
                        customerBody = "您好，我是豆包网的专属客服,不知道您有什么疑问可以帮到您^_^";
                        visitorBody = "系统消息，用户有产品问题咨询，请跟近";
                    } else {
                        customerBody = "您好，我是豆包网的专属客服，您刚浏览了<a href=\\\"" + pc.getGoodUrl() + "\\\">" + pc.getName() + "</a>，不知道您有什么疑问可以帮到您^_^";
                        visitorBody = "系统消息，用户刚浏览了<a href=\\\"" + pc.getGoodUrl() + "\\\">" + pc.getName() + "</a>";
                    }
                }
                logger.info("info:" + customerBody);
                // 客服不在线
                Msg msg = new TextMsg(customerBody);
                msg.setSource(TeminalTypeEnum.PRODUCT.getCode()); //消息来源是微信
                msg.setTo(customerChatNode.getAbstractUser().getId());
                msg.setFrom(pc.getU());
                if (!cFlag) {
                    offLineMessageService.dealOfflineMessage(visitorChatNode, customerBody);
                } else {
                    messageSendToWeixin.send(msg, pc.getU(), null);
                }

                if (StringUtils.isNotEmpty(visitorBody)) {
                    Msg msgClone = (Msg) SerializationUtils.clone(msg);
                    msgClone.setContent(visitorBody);
                    visitorChatNode.receiveFromGod(node, msg);
                }

            }

        } catch (BusinessException e) {
            logger.error("error", e);

        } catch (Exception e) {
            logger.error("error", e);
        }

        return mv;
    }

    public VisitorChatNode initVisitorChatNode(AbstractUser visitor) throws BusinessException, XMPPException, IOException, SmackException, InterruptedException {

        VisitorChatNode visitorChatNode = ChatNodeManager.getVisitorXmppNode(visitor);

        String id = weChatTerminalVisitorFactory.getId(visitor);
        AbstractTerminal node = visitorChatNode.getNode(id);
        if (null == node) {
            ChatNodeAdaptation chatNodeAdaptation = new ChatNodeAdaptation(visitorChatNode);
            node = weChatTerminalVisitorFactory.getNode(chatNodeAdaptation, visitor);
            visitorChatNode.addNode(node);
        }

        visitorChatNode.initCurrentChatNode();
        logger.info(JSONUtil.toJson(visitorChatNode.getAbstractUser()));

        if (!visitorChatNode.isXmppOnline()) {
            visitorChatNode.login();
            visitorChatNode.online(node);
        }

        return visitorChatNode;
    }
}
