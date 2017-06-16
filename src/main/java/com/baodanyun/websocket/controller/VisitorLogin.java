package com.baodanyun.websocket.controller;

import com.baodanyun.websocket.bean.user.AbstractUser;
import com.baodanyun.websocket.bean.user.Visitor;
import com.baodanyun.websocket.core.common.Common;
import com.baodanyun.websocket.exception.BusinessException;
import com.baodanyun.websocket.node.xmpp.ChatNode;
import com.baodanyun.websocket.node.xmpp.ChatNodeManager;
import com.baodanyun.websocket.service.*;
import com.baodanyun.websocket.util.JSONUtil;
import com.baodanyun.websocket.util.XMPPUtil;
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
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by yutao on 2016/10/4.
 */
@RestController
@RequestMapping("visitorlogin")
public class VisitorLogin extends BaseController {
    private static final String LOGIN_USER = "u";
    protected final Logger logger = LoggerFactory.getLogger(this.getClass());
    @Autowired
    private UserServer userServer;

    @Autowired
    private XmppServer xmppServer;

    @Autowired
    private UserCacheServer userCacheServer;

    @Autowired
    private VcardService vcardService;

    @Autowired
    private XmppUserOnlineServer xmppUserOnlineServer;

    @Autowired
    private CustomerDispatcherService customerDispatcherService;

    @RequestMapping(method = {RequestMethod.GET, RequestMethod.POST})
    public ModelAndView visitor(HttpServletRequest request, HttpServletResponse response) throws IOException, XMPPException, SmackException {
        ModelAndView mv = new ModelAndView();
        String openId = request.getParameter(LOGIN_USER);
        AbstractUser cCard;
        logger.info("accessId:[" + openId + "]");

        try {
            Visitor visitor = userServer.initUserByOpenId(openId);
            AbstractUser customer;
            ChatNode wn = ChatNodeManager.getVisitorXmppNode(visitor);
            boolean login = wn.login();
            if (login) {
                customer = customerDispatcherService.getCustomer(visitor.getOpenId());
            } else {
                customer = customerDispatcherService.getDispatcher(visitor.getOpenId());
            }
            visitor.setCustomer(customer);
            logger.info(JSONUtil.toJson(visitor));

            if (null != customer) {
                boolean flag = customerOnline(customer.getId());
                if (flag) {

                    if (login) {
                        cCard = vcardService.getVCardUser(customer.getId(), visitor.getId(), AbstractUser.class);
                        mv = getOnline(visitor, customer.getId(), cCard);
                        request.getSession().setAttribute(Common.USER_KEY, visitor);
                    } else {
                        throw new BusinessException("接入失败");
                    }

                } else {
                    //客服不在线
                    mv = getOffline(visitor, customer.getId());
                }
            } else {
                throw new BusinessException("客服不存在");
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

    public boolean customerOnline(String cid) {
        boolean flag = xmppServer.isAuthenticated(cid);
        if (!flag) {
            flag = xmppUserOnlineServer.isOnline(XMPPUtil.jidToName(cid));
        }

        return flag;
    }
}
