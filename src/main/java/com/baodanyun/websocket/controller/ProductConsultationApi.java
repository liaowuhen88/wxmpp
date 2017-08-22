package com.baodanyun.websocket.controller;

import com.baodanyun.websocket.bean.Response;
import com.baodanyun.websocket.bean.msg.Msg;
import com.baodanyun.websocket.bean.msg.msg.TextMsg;
import com.baodanyun.websocket.bean.request.ProductConsultation;
import com.baodanyun.websocket.bean.user.AbstractUser;
import com.baodanyun.websocket.bean.user.Visitor;
import com.baodanyun.websocket.enums.TeminalTypeEnum;
import com.baodanyun.websocket.exception.BusinessException;
import com.baodanyun.websocket.node.*;
import com.baodanyun.websocket.service.MessageSendToWeixin;
import com.baodanyun.websocket.service.UserServer;
import com.baodanyun.websocket.util.JSONUtil;
import com.baodanyun.websocket.util.Render;
import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.XMPPException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by yutao on 2016/10/4.
 * <p/>
 * 接收微信信息入口等等
 */
@RestController
public class ProductConsultationApi extends BaseController {
    protected static Logger logger = LoggerFactory.getLogger(ProductConsultationApi.class);
    private static Map<String, Visitor> visitors = new ConcurrentHashMap<>();
    @Autowired
    private UserServer userServer;

    @Autowired
    private MessageSendToWeixin messageSendToWeixin;

    @Autowired
    private WeChatTerminalVisitorFactory weChatTerminalVisitorFactory;

    private String me = "当前客服不在线，请点击以下链接留言";

    @RequestMapping(value = "productConsultation")
    public void getMessageByCId(ProductConsultation pc, HttpServletRequest request, HttpServletResponse httpServletResponse) {

        Response response;
        try {
            logger.info(JSONUtil.toJson(pc));
            VisitorChatNode visitorChatNode = initVisitorChatNode(pc.getU());
            CustomerChatNode customerChatNode = visitorChatNode.getCurrentChatNode();


            boolean cFlag = visitorChatNode.getCurrentChatNode().isXmppOnline();
            logger.info("客服是否在线" + cFlag);
            AbstractTerminal node = visitorChatNode.getNode(weChatTerminalVisitorFactory.getId(visitorChatNode.getAbstractUser()));
            // 客服不在线
            if (!cFlag) {
                String url = request.getRequestURL().toString();
                response = getLeaveMessageResponse(visitorChatNode, url, customerChatNode.getAbstractUser().getId(), pc.getU());
                Render.r(httpServletResponse, JSONUtil.toJson(response)); //客服不在线直接返回
                return;
            } else {
                response = getOnlineResponse();
            }

            String body = "您好，我是豆包网的专属客服，您刚浏览了<a href=\\\"" + pc.getGoodUrl() + "\\\">" + pc.getName() + "</a>，不知道您有什么疑问可以帮到您^_^";
            Msg msg = new TextMsg(body);
            msg.setSource(TeminalTypeEnum.PRODUCT.getCode()); //消息来源是微信
            msg.setTo(customerChatNode.getAbstractUser().getId());
            msg.setFrom(pc.getU());

            messageSendToWeixin.send(msg, pc.getU(), null);
            visitorChatNode.receiveFromGod(node, msg);

        } catch (Exception e) {
            logger.error("error", e);
            response = new Response();
            response.setMsg(e.getMessage());
            response.setSuccess(false);
        }
        Render.r(httpServletResponse, JSONUtil.toJson(response));
    }


    public Response getLeaveMessageResponse(VisitorChatNode visitorChatNode, String url, String to, String openId) {
        Response response = new Response();
        AbstractUser visitor = visitorChatNode.getAbstractUser();
        AbstractUser customer = visitorChatNode.getCurrentChatNode().getAbstractUser();
        logger.info("customer[" + customer.getId() + "] not online");
        String info;
        if (null == visitor.getUid() || 0 == visitor.getUid()) {
            info = "请您到个人中心注册";
        } else {
            int end = url.indexOf("/api/");
            String base = url.substring(0, end);
            String u = base + "/visitorlogin?t=" + to + "&u=" + openId;
            info = me + "[<a href=\\\"" + u + "\\\">我要留言</a>]";
            logger.info("info:" + info);
        }
        Msg sendMsg = new Msg(info);
        sendMsg.setContentType(Msg.MsgContentType.text.toString());
        Long ct = new Date().getTime();
        sendMsg.setTo(customer.getId());
        sendMsg.setCt(ct);
        messageSendToWeixin.send(sendMsg, openId, customer.getId());
        response.setSuccess(true);

        response.setMsg(info);
        return response;
    }

    public Response getOnlineResponse() {
        Response response = new Response();

        response.setSuccess(true);
        return response;
    }

    public VisitorChatNode initVisitorChatNode(String openId) throws BusinessException, XMPPException, IOException, SmackException, InterruptedException {
        AbstractUser visitor = userServer.initUserByOpenId(openId);
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
