package com.baodanyun.websocket.controller;

import com.baodanyun.websocket.bean.Response;
import com.baodanyun.websocket.bean.request.AppKeyVisitorLoginBean;
import com.baodanyun.websocket.bean.user.AbstractUser;
import com.baodanyun.websocket.bean.user.AppCustomer;
import com.baodanyun.websocket.core.common.Common;
import com.baodanyun.websocket.exception.BusinessException;
import com.baodanyun.websocket.node.ChatNodeManager;
import com.baodanyun.websocket.node.CustomerChatNode;
import com.baodanyun.websocket.node.VisitorChatNode;
import com.baodanyun.websocket.service.AppKeyService;
import com.baodanyun.websocket.service.CustomerDispatcherService;
import com.baodanyun.websocket.service.XmppUserOnlineServer;
import com.baodanyun.websocket.util.JSONUtil;
import com.baodanyun.websocket.util.Render;
import com.baodanyun.websocket.util.XMPPUtil;
import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.XMPPException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by yutao on 2016/10/4.
 */
@RestController
@RequestMapping("appKeyCheck")
public class AppKeyVisitorLogin extends BaseController {
    protected final Logger logger = LoggerFactory.getLogger(this.getClass());
    @Autowired
    private AppKeyService appKeyService;

    @Autowired
    private XmppUserOnlineServer xmppUserOnlineServer;

    @Autowired
    private CustomerDispatcherService customerDispatcherService;

    @RequestMapping(method = {RequestMethod.GET, RequestMethod.POST})
    public void visitor(AppKeyVisitorLoginBean re, HttpServletRequest request, HttpServletResponse response) throws IOException, XMPPException, SmackException {
        Response responseMsg = new Response();
        AppCustomer customer;
        try {
            String url = request.getRequestURL().substring(0, request.getRequestURL().indexOf(request.getRequestURI()));
            logger.info("visitorLogin:[" + JSONUtil.toJson(re.getAppKey()) + "]---- url {}", url);
            // 初始化用户,以及用户节点
            customer = appKeyService.getCustomerByAppKey(re.getAppKey(), url);
            AbstractUser visitor = appKeyService.getVisitor(re);
            VisitorChatNode visitorChatNode = ChatNodeManager.getVisitorXmppNode(visitor);

            // 获取客服节点
            CustomerChatNode customerChatNode = ChatNodeManager.getCustomerXmppNode(customer);
            logger.info(JSONUtil.toJson(visitor));
            request.getSession().setAttribute(Common.USER_KEY, visitor);
            boolean flag = customerOnline(customerChatNode);
            // 如果客服在线，用户登录
            if (flag) {
                if (visitorChatNode.login()) {
                    visitorChatNode.setCurrentChatNode(customerChatNode);
                    getOnline(responseMsg, customer);
                } else {
                    throw new BusinessException("接入失败");
                }
            } else {
                //客服不在线
                getOffline(responseMsg, customer);
            }
        } catch (Exception e) {
            logger.error("", e);
            responseMsg.setSuccess(false);
            responseMsg.setMsg(e.getMessage());
        }

        response.setHeader("Access-Control-Allow-Origin","*" );
        Render.r(response, XMPPUtil.buildJson(responseMsg));
    }

    public void getOnline(Response responseMsg, AppCustomer customer) throws BusinessException, InterruptedException, XMPPException, SmackException, IOException {
        // 客服在线
        responseMsg.setSuccess(true);
        responseMsg.setData(customer);
    }


    public void getOffline(Response responseMsg, AppCustomer customer) throws BusinessException {

        responseMsg.setSuccess(false);
        responseMsg.setData(customer);
        responseMsg.setMsg("客服不在线");

    }

    public boolean customerOnline(CustomerChatNode customerChatNode) {
        boolean flag = customerChatNode.isXmppOnline();
        if (!flag) {
            flag = xmppUserOnlineServer.isOnline(customerChatNode.getAbstractUser().getLoginUsername());
        }
        return flag;
    }
}
