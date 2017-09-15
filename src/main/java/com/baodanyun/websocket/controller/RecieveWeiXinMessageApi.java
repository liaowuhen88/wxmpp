package com.baodanyun.websocket.controller;

import com.baodanyun.robot.service.RobotService;
import com.baodanyun.websocket.bean.Response;
import com.baodanyun.websocket.bean.msg.Msg;
import com.baodanyun.websocket.bean.user.AbstractUser;
import com.baodanyun.websocket.enums.TeminalTypeEnum;
import com.baodanyun.websocket.exception.BusinessException;
import com.baodanyun.websocket.node.*;
import com.baodanyun.websocket.service.MessageSendToWeixin;
import com.baodanyun.websocket.service.OffLineMessageService;
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

/**
 * Created by yutao on 2016/10/4.
 * <p/>
 * 接收微信信息入口等等
 */
@RestController
public class RecieveWeiXinMessageApi extends BaseController {
    protected static Logger logger = LoggerFactory.getLogger(RecieveWeiXinMessageApi.class);

    @Autowired
    private UserServer userServer;

    @Autowired
    private OffLineMessageService offLineMessageService;

    @Autowired
    private MessageSendToWeixin messageSendToWeixin;

    @Autowired
    private WeChatTerminalVisitorFactory weChatTerminalVisitorFactory;
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
            // 初始化数据
            String body = HttpServletRequestUtils.getBody(request);
            logger.info(body);
            Msg msg = msg(body);
            logger.info(JSONUtil.toJson(msg));

            msg.setSource(TeminalTypeEnum.WE_CHAT.getCode()); //消息来源是微信
            AbstractUser user = userServer.initUserByOpenId(msg.getFrom());
            boolean tobot = robotService.executeRobotFlow(msg, user);
            logger.info("tobot {}", tobot);
            if (tobot) {//存在[我要报案]进入机器人流程
                return;
            }

            VisitorChatNode visitorChatNode = initVisitorChatNode(user, msg);

            CustomerChatNode customerChatNode = visitorChatNode.getCurrentChatNode();
            msg.setTo(customerChatNode.getAbstractUser().getId());
            AbstractTerminal node = visitorChatNode.getNode(weChatTerminalVisitorFactory.getId(visitorChatNode.getAbstractUser()));

            if (!StringUtils.isEmpty(msg.getContent()) && msg.getContent().startsWith(keywords)) {
                response = getBindCustomerResponse(visitorChatNode.getAbstractUser(), msg);
            } else {
                boolean cFlag = visitorChatNode.getCurrentChatNode().isOnline();
                logger.info("客服是否在线" + cFlag);
                // 客服不在线
                if (!cFlag) {
                    //String url = request.getRequestURL().toString();
                    response = getLeaveMessageResponse(visitorChatNode, msg);
                } else {
                    response = getOnlineResponse();
                }
            }
            visitorChatNode.receiveFromGod(node, msg);
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
        Msg msg = JSONUtil.toObject(Msg.class, body);

        if (StringUtils.isBlank(msg.getFrom())) {
            throw new BusinessException("openId不能为空");
        }

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

    public Response getLeaveMessageResponse(VisitorChatNode visitorChatNode, Msg msg) {
        Response response = new Response();
        offLineMessageService.dealOfflineMessage(visitorChatNode, msg.getContent());
        String info = Config.offlineWord;
        response.setSuccess(true);
        response.setMsg(info);
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
        Response response = new Response();
        try {
            messageSendToWeixin.send(sendMsg, msg.getFrom(), "sys");
            response.setMsg("change bind customer");
            response.setSuccess(true);
        } catch (Exception e) {
            logger.error("error", e);
        }
        return response;
    }

    public VisitorChatNode initVisitorChatNode(AbstractUser visitor, Msg msg) throws BusinessException, XMPPException, IOException, SmackException, InterruptedException {

        VisitorChatNode visitorChatNode = ChatNodeManager.getVisitorXmppNode(visitor);

        String id = weChatTerminalVisitorFactory.getId(visitor);
        AbstractTerminal node = visitorChatNode.getNode(id);
        if (null == node) {
            ChatNodeAdaptation chatNodeAdaptation = new ChatNodeAdaptation(visitorChatNode);
            node = weChatTerminalVisitorFactory.getNode(chatNodeAdaptation, visitor);
            visitorChatNode.addNode(node);
        }

        visitorChatNode.initCurrentChatNode();

        if (!visitorChatNode.isXmppOnline()) {
            visitorChatNode.login();
            visitorChatNode.online(node);
        }

        return visitorChatNode;
    }
}
