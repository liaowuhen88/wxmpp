package com.baodanyun.websocket.controller;

import com.baodanyun.robot.service.RobotService;
import com.baodanyun.websocket.bean.Response;
import com.baodanyun.websocket.bean.msg.Msg;
import com.baodanyun.websocket.bean.user.AbstractUser;
import com.baodanyun.websocket.bean.user.Visitor;
import com.baodanyun.websocket.enums.TeminalTypeEnum;
import com.baodanyun.websocket.exception.BusinessException;
import com.baodanyun.websocket.node.*;
import com.baodanyun.websocket.service.CustomerDispatcherTactics;
import com.baodanyun.websocket.service.MessageSendToWeixin;
import com.baodanyun.websocket.service.UserServer;
import com.baodanyun.websocket.util.Config;
import com.baodanyun.websocket.util.HttpServletRequestUtils;
import com.baodanyun.websocket.util.JSONUtil;
import com.baodanyun.websocket.util.Render;
import org.apache.commons.lang.StringUtils;
import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.XMPPException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
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
public class RecieveWeiXinMessageApi extends BaseController {
    protected static Logger logger = LoggerFactory.getLogger(RecieveWeiXinMessageApi.class);
    private static Map<String, Visitor> visitors = new ConcurrentHashMap<>();
    @Autowired
    private UserServer userServer;

    @Autowired
    private MessageSendToWeixin messageSendToWeixin;

    @Autowired
    private CustomerDispatcherTactics customerDispatcherTactics;

    @Autowired
    private WeChatTerminalVisitorFactory weChatTerminalVisitorFactory;

    private String me = "当前客服不在线，请点击以下链接留言";
    /**
     * 指定客服关键字
     */
    private String keywords = "@客服";

    @Autowired
    @Qualifier("weChatRobotService")
    private RobotService robotService;

    @RequestMapping(value = "receiveMsg")
    public void getMessageByCId(HttpServletRequest request, HttpServletResponse httpServletResponse) {
        Response response;
        try {
            String body = HttpServletRequestUtils.getBody(request);
            Msg msg = msg(body);
            msg.setSource(TeminalTypeEnum.WE_CHAT.getCode()); //消息来源是微信

            if (robotService.executeRobotFlow(msg)) {//存在[我要报案]进入机器人流程
                return;
            }

            VisitorChatNode visitorChatNode = initVisitorChatNode(msg);

            CustomerChatNode customerChatNode = visitorChatNode.getCurrentChatNode();
            msg.setTo(customerChatNode.getAbstractUser().getId());

            if (!StringUtils.isEmpty(msg.getContent()) && msg.getContent().startsWith(keywords)) {
                response = getBindCustomerResponse(visitorChatNode.getAbstractUser(), msg);
            } else {
                boolean cFlag = visitorChatNode.getCurrentChatNode().xmppOnlineServer();
                logger.info("客服是否在线" + cFlag);
                AbstractTerminal node = visitorChatNode.getNode(weChatTerminalVisitorFactory.getId(visitorChatNode.getAbstractUser()));
                // 客服不在线
                if (!cFlag) {
                    String url = request.getRequestURL().toString();
                    response = getLeaveMessageResponse(visitorChatNode.getCurrentChatNode().getAbstractUser(), url, msg);

                    Render.r(httpServletResponse, JSONUtil.toJson(response)); //客服不在线直接返回
                    return;
                } else {
                    response = getOnlineResponse();
                }
                visitorChatNode.receiveFromGod(node, msg);
            }

        } catch (Exception e) {
            logger.error("error", e);
            response = new Response();
            response.setMsg(e.getMessage());
            response.setSuccess(false);
        }
        Render.r(httpServletResponse, JSONUtil.toJson(response));
    }


    /**
     * 初始化消息
     *
     * @param body
     * @return
     * @throws Exception
     */

    public Msg msg(String body) throws Exception {
        if (StringUtils.isEmpty(body)) {
            throw new BusinessException("body is null");
        }
        logger.info(body);
        Msg msg = JSONUtil.toObject(Msg.class, body);

        if (msg.getCt() == null) {
            msg.setCt(System.currentTimeMillis()); //系统时间
        }

        /**
         * 设置默认接入客服
         */
        if (StringUtils.isEmpty(msg.getTo())) {
            msg.setTo(Config.controlId);
        }
        return msg;
    }


    public Response getLeaveMessageResponse(AbstractUser customer, String url, Msg msg) {
        Response response = new Response();

        logger.info("customer[" + customer.getId() + "] not online");

        int end = url.indexOf("/api/");
        String base = url.substring(0, end);
        String u = base + "/visitorlogin?t=" + msg.getTo() + "&u=" + msg.getFrom();
        String info = me + "[<a href=\\\"" + u + "\\\">我要留言</a>]";
        logger.info("info:" + info);
        Msg sendMsg = new Msg(info);
        sendMsg.setContentType(Msg.MsgContentType.text.toString());
        Long ct = new Date().getTime();
        sendMsg.setTo(customer.getId());
        sendMsg.setCt(ct);
        messageSendToWeixin.send(sendMsg, msg.getFrom(), customer.getId());
        response.setSuccess(true);

        response.setMsg("[<a href=\\\"" + u + "\\\">我要留言</a>]");
        return response;
    }

    public Response getOnlineResponse() {
        Response response = new Response();

        response.setSuccess(true);
        return response;
    }

    public Response getBindCustomerResponse(AbstractUser visitor, Msg msg) throws BusinessException, InterruptedException {
        String cJid = msg.getContent().substring(keywords.length()).trim();

        Msg sendMsg = new Msg("切换到客服 [" + cJid + "]成功,欢迎您咨询");

        sendMsg.setContentType(Msg.MsgContentType.text.toString());
        Long ct = new Date().getTime();
        sendMsg.setCt(ct);

        messageSendToWeixin.send(sendMsg, msg.getFrom(), "sys");

        Response response = new Response();
        response.setMsg("change bind customer");
        response.setSuccess(true);

        return response;
    }

    public VisitorChatNode initVisitorChatNode(Msg msg) throws BusinessException, XMPPException, IOException, SmackException, InterruptedException {
        AbstractUser visitor = userServer.initUserByOpenId(msg.getFrom());
        VisitorChatNode visitorChatNode = ChatNodeManager.getVisitorXmppNode(visitor);

        if (null == visitorChatNode || !visitorChatNode.isXmppOnline()) {
            visitorChatNode = ChatNodeManager.getVisitorXmppNode(visitor);
        }

        String id = weChatTerminalVisitorFactory.getId(visitor);
        AbstractTerminal node = visitorChatNode.getNode(id);
        if (null == node) {
            ChatNodeAdaptation chatNodeAdaptation = new ChatNodeAdaptation(visitorChatNode);
            node = weChatTerminalVisitorFactory.getNode(chatNodeAdaptation, visitor);
            visitorChatNode.addNode(node);
        }

        visitorChatNode.initCurrentChatNode();

        logger.info(JSONUtil.toJson(visitorChatNode.getAbstractUser()));

        if (!visitorChatNode.isOnline()) {
            visitorChatNode.login();
            if (null != node) {
                visitorChatNode.online(node);
            } else {
                logger.error("node is null");
            }
        }

        return visitorChatNode;
    }
}
