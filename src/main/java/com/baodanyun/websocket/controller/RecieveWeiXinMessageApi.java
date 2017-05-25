package com.baodanyun.websocket.controller;

import com.baodanyun.websocket.bean.Response;
import com.baodanyun.websocket.bean.msg.Msg;
import com.baodanyun.websocket.bean.user.AbstractUser;
import com.baodanyun.websocket.bean.user.Visitor;
import com.baodanyun.websocket.exception.BusinessException;
import com.baodanyun.websocket.node.NodeManager;
import com.baodanyun.websocket.node.WeChatNode;
import com.baodanyun.websocket.service.*;
import com.baodanyun.websocket.util.*;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;

/**
 * Created by yutao on 2016/10/4.
 *
 * 接收微信信息入口等等
 */
@RestController
public class RecieveWeiXinMessageApi extends BaseController {
    protected static Logger logger = Logger.getLogger(RecieveWeiXinMessageApi.class);

    @Autowired
    private UserServer userServer;

    @Autowired
    private MessageSendToWeixin messageSendToWeixin;

    @Autowired
    private XmppUserOnlineServer xmppUserOnlineServer;

    @Autowired
    private CustomerDispatcherService customerDispatcherService;

    @Autowired
    private UserCacheServer userCacheServer;

    @Autowired
    private XmppServer xmppServer;

    private String me = "当前客服不在线，请点击以下链接留言";
    /**
     * 指定客服关键字
     */
    private String keywords = "@客服";

    @RequestMapping(value = "receiveMsg")
    public void getMessageByCId(HttpServletRequest request, HttpServletResponse httpServletResponse) {
        Response response ;
        try {
            String body = HttpServletRequestUtils.getBody(request);
            Msg msg = msg(body);
            Visitor visitor = userServer.initUserByOpenId(msg.getFrom());
            WeChatNode wn = NodeManager.getWeChatNode(visitor);
            boolean xmppFlag = wn.getXmppNode().isOnline();
            AbstractUser customer;
            if (xmppFlag) {
                customer = customerDispatcherService.getDispatcher(visitor.getOpenId());
            } else {
                customer = customerDispatcherService.getCustomer(visitor.getOpenId());
            }
            visitor.setCustomer(customer);
            msg.setTo(customer.getId());
            logger.info(JSONUtil.toJson(visitor));



            if (!StringUtils.isEmpty(msg.getContent()) && msg.getContent().startsWith(keywords)) {
                response = getBindCustomerResponse(visitor, msg);
            } else {
                if (null != customer && null != customer.getId()) {

                    boolean cFlag = xmppServer.isAuthenticated(customer.getId());
                    if (!cFlag) {
                        cFlag = xmppUserOnlineServer.isOnline(customer.getLoginUsername());
                    }
                    logger.info(cFlag);
                    // 客服不在线
                    if (!cFlag) {
                        String url = request.getRequestURL().toString();
                        response = getLeaveMessageResponse(customer, url, msg);
                    } else {
                        boolean flag = wn.getXmppNode().isOnline();

                        if (!flag) {
                            wn.login();
                            wn.joinQueue();
                        }

                        wn.receiveFromGod(msg);

                        response = getOnlineResponse();
                    }

                } else {
                    throw new BusinessException("客服不存在");
                }
            }

        } catch (Exception e) {
            logger.error(e);
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

        return response;
    }

    public Response getOnlineResponse() {
        Response response = new Response();

        response.setSuccess(true);
        return response;
    }

    public Response getBindCustomerResponse(Visitor visitor, Msg msg) throws BusinessException, InterruptedException {
        String cJid = msg.getContent().substring(keywords.length()).trim();
        userCacheServer.addVisitorCustomerOpenId(msg.getFrom(), XMPPUtil.nameToJid(cJid));

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


}
