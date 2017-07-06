package com.baodanyun.websocket.controller;

import com.baodanyun.websocket.bean.Response;
import com.baodanyun.websocket.bean.request.AppKeyVisitorLoginBean;
import com.baodanyun.websocket.bean.user.AbstractUser;
import com.baodanyun.websocket.bean.user.AppCustomer;
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
            //String url = "";
            String url = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath();
            logger.info("visitorLogin:[" + JSONUtil.toJson(re.getAppKey()) + "]---- url {}", url);
            // 初始化用户,以及用户节点
            customer = appKeyService.getCustomerByAppKey(re.getAppKey(), url);
            AbstractUser visitor = appKeyService.getVisitor(re, customer.getToken());
            VisitorChatNode visitorChatNode = ChatNodeManager.getVisitorXmppNode(visitor);
            // 获取客服节点
            CustomerChatNode customerChatNode = ChatNodeManager.getCustomerXmppNode(customer);

            visitorChatNode.setCurrentChatNode(customerChatNode);

            boolean flag = customerChatNode.openfireOnline();
            // 如果客服在线，用户登录
            if (flag) {
                if (!visitorChatNode.login()) {
                    throw new BusinessException("接入失败");
                }
                getOnline(responseMsg, customer);
            } else {
                //客服不在线
                getOffline(responseMsg, customer);
            }
            getOnline(responseMsg, customer);


        } catch (Exception e) {
            logger.error("", e);
            responseMsg.setSuccess(false);
            responseMsg.setMsg(e.getMessage());
        }

        response.setHeader("Access-Control-Allow-Credentials", "true");
        response.setHeader("Access-Control-Allow-Origin", "*");

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

}
