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
import com.baodanyun.websocket.util.PropertiesUtil;
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
import java.util.Map;

/**
 * Created by yutao on 2016/10/4.
 */
@RestController
@RequestMapping("appKeyCheck")
public class AppKeyVisitorLogin extends BaseController {
    protected final Logger logger = LoggerFactory.getLogger(this.getClass());
    Map<String, String> map = PropertiesUtil.get(this.getClass().getClassLoader(), "config.properties");
    @Autowired
    private AppKeyService appKeyService;

    @Autowired
    private XmppUserOnlineServer xmppUserOnlineServer;

    @Autowired
    private CustomerDispatcherService customerDispatcherService;

    @RequestMapping(method = {RequestMethod.GET, RequestMethod.POST})
    public void visitor(AppKeyVisitorLoginBean re, HttpServletRequest request, HttpServletResponse response) throws IOException, XMPPException, SmackException {
        Response responseMsg = new Response();
        AbstractUser customer;
        try {
            logger.info("visitorLogin:[" + JSONUtil.toJson(re.getAppKey()) + "]");
            // 初始化用户,以及用户节点
            AbstractUser visitor = appKeyService.getByAppKey(re.getAppKey());

            VisitorChatNode visitorChatNode = ChatNodeManager.getVisitorXmppNode(visitor);
            boolean login = visitorChatNode.isXmppOnline();
            // 根据用户是否已经在线，获取服务客服
            if (login) {
                // 在线获取上次服务客服
                customer = customerDispatcherService.getCustomer(visitor.getOpenId());
            } else {
                // 非在线随机获取客服
                customer = customerDispatcherService.getDispatcher(visitor.getOpenId());
            }

            // 获取客服节点
            CustomerChatNode customerChatNode = ChatNodeManager.getCustomerXmppNode(customer);
            logger.info(JSONUtil.toJson(visitor));
            request.getSession().setAttribute(Common.USER_KEY, visitor);
            boolean flag = customerOnline(customerChatNode);
            // 如果客服在线，用户登录
            if (flag) {
                if (visitorChatNode.login()) {
                    visitorChatNode.setCurrentChatNode(customerChatNode);
                    getOnline(responseMsg, visitor, customer.getId());
                } else {
                    throw new BusinessException("接入失败");
                }
            } else {
                //客服不在线
                getOffline(responseMsg, visitor, customer.getId());
            }
        } catch (Exception e) {
            responseMsg.setSuccess(false);
            responseMsg.setMsg(e.getMessage());
        }


        Render.r(response, XMPPUtil.buildJson(responseMsg));
    }

    public void getOnline(Response responseMsg, AbstractUser visitor, String customerJid) throws BusinessException, InterruptedException, XMPPException, SmackException, IOException {
        // 客服在线

        AppCustomer ac = new AppCustomer();
        ac.setVisitor(visitor);
        ac.setCustomerIsOnline(true);
        ac.setSocketUrl("/sockjs/newVisitor");
        ac.setOssUrl(map.get("oss.upload"));
        responseMsg.setSuccess(true);
        responseMsg.setData(ac);

    }


    public void getOffline(Response responseMsg, AbstractUser visitor, String customerJid) throws BusinessException {

        responseMsg.setSuccess(false);
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
