package com.baodanyun.websocket.controller;

import com.baodanyun.websocket.bean.request.VisitorLoginBean;
import com.baodanyun.websocket.bean.user.AbstractUser;
import com.baodanyun.websocket.bean.user.Visitor;
import com.baodanyun.websocket.core.common.Common;
import com.baodanyun.websocket.exception.BusinessException;
import com.baodanyun.websocket.node.ChatNodeManager;
import com.baodanyun.websocket.node.CustomerChatNode;
import com.baodanyun.websocket.node.VisitorChatNode;
import com.baodanyun.websocket.service.*;
import com.baodanyun.websocket.util.JSONUtil;
import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.XMPPException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * Created by yutao on 2016/10/4.
 */
@RestController
@RequestMapping("visitorlogin")
public class VisitorLogin extends BaseController {

    protected final Logger logger = LoggerFactory.getLogger(this.getClass());
    @Autowired
    private UserServer userServer;

    @Autowired
    private VcardService vcardService;

    @Autowired
    private XmppUserOnlineServer xmppUserOnlineServer;

    @Autowired
    private CustomerDispatcherService customerDispatcherService;

    @RequestMapping(method = {RequestMethod.GET, RequestMethod.POST})
    public ModelAndView visitor(VisitorLoginBean visitorLoginBean, HttpServletRequest request) throws IOException, XMPPException, SmackException {
        ModelAndView mv = new ModelAndView();
        AbstractUser cCard;
        AbstractUser customer;
        try {
            logger.info("visitorLogin:[" + JSONUtil.toJson(visitorLoginBean) + "]");
            // 初始化用户,以及用户节点
            Visitor visitor = userServer.initUserByOpenId(visitorLoginBean.getU());
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
                    visitorChatNode.changeCurrentChatNode(customerChatNode);
                    cCard = vcardService.getVCardUser(customer.getId(), visitor.getId(), AbstractUser.class);
                    mv = getOnline(visitor, customer.getId(), cCard);

                } else {
                    throw new BusinessException("接入失败");
                }

            } else {
                //客服不在线
                mv = getOffline(visitor, customer.getId());
            }


        } catch (Exception e) {
            logger.error("error", "", e);
            mv.addObject("statue", false);
            mv.addObject("customerIsOnline", false);
            mv.addObject("msg", e.getMessage());

            mv.setViewName("/visitor");
        }
        return mv;
    }

    public ModelAndView getOnline(AbstractUser visitor, String customerJid, AbstractUser card) throws BusinessException, InterruptedException, XMPPException, SmackException, IOException {
        // 客服在线
        ModelAndView mv = new ModelAndView();

        if (null == card.getId()) {
            card.setId("-1");
        }
        mv.addObject("statue", true);
        mv.addObject("customerIsOnline", true);
        mv.addObject("visitor", visitor);
        mv.addObject("customerJid", customerJid);
        mv.addObject("card", card);
        mv.setViewName("/visitor");

        return mv;
    }


    public ModelAndView getOffline(AbstractUser visitor, String customerJid) throws BusinessException {
        ModelAndView mv = new ModelAndView();

        if (null == visitor) {
            visitor = userServer.initVisitor();
        }
        mv.addObject("statue", false);
        mv.addObject("visitor", visitor);
        mv.addObject("customerJid", customerJid);
        mv.addObject("customerIsOnline", false);
        mv.setViewName("/visitor");

        return mv;
    }

    public boolean customerOnline(CustomerChatNode customerChatNode) {
        boolean flag = customerChatNode.isXmppOnline();
        if (!flag) {
            flag = xmppUserOnlineServer.isOnline(customerChatNode.getAbstractUser().getLoginUsername());
        }
        return flag;
    }
}
