package com.baodanyun.websocket.controller;

import com.baodanyun.websocket.bean.Response;
import com.baodanyun.websocket.bean.user.*;
import com.baodanyun.websocket.core.common.Common;
import com.baodanyun.websocket.dao.OfuserMapper;
import com.baodanyun.websocket.exception.BusinessException;
import com.baodanyun.websocket.model.LoginModel;
import com.baodanyun.websocket.node.*;
import com.baodanyun.websocket.service.UserServer;
import com.baodanyun.websocket.service.XmppServer;
import com.baodanyun.websocket.util.JSONUtil;
import com.baodanyun.websocket.util.Render;
import com.baodanyun.websocket.util.XMPPUtil;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by yutao on 2016/10/4.
 */

@RestController
public class CustomerLogin extends BaseController {

    protected static Logger logger = LoggerFactory.getLogger(CustomerApi.class);
    @Autowired
    private OfuserMapper ofuserMapper;

    @Autowired
    private XmppServer xmppServer;

    @Autowired
    private UserServer userServer;

    @Autowired
    private AccessWeChatTerminalVisitorFactory accessWeChatTerminalVisitorFactory;

    @RequestMapping(value = "loginApi", method = RequestMethod.POST)
    public void api(LoginModel user, HttpServletRequest request, HttpServletResponse response) {
        //客服必须填写用户名 和 密码
        logger.info("user" + JSONUtil.toJson(user));
        NewCustomer au = new NewCustomer();
        au.setAccessType(user.getAccessType());
        try {
            customerInit(au, user);

            CustomerChatNode cx = ChatNodeManager.getCustomerXmppNode(au);

            boolean flag = cx.login();
            if (flag) {
                request.getSession().setAttribute(Common.USER_KEY, au);
            }
        } catch (Exception e) {
            logger.error("error", e);
            au = null;
        }

        Render.r(response, XMPPUtil.buildJson(getRespone(au)));
    }


    /**
     * 第三方接入
     *
     * @param user
     * @param request
     * @return
     */
    @RequestMapping(value = "customerLogin")
    public ModelAndView customerLogin(LoginModel user, HttpServletRequest request) {
        //客服必须填写用户名 和 密码
        logger.info("user" + JSONUtil.toJson(user));
        ModelAndView mv = new ModelAndView();
        try {
            if (StringUtils.isBlank(user.getTo())) {
                throw new BusinessException("to 参数不能为空");
            }
            AcsessCustomer customer = new AcsessCustomer();

            customerInit(customer, user);
            CustomerChatNode cx = ChatNodeManager.getCustomerXmppNode(customer);

            if (!cx.isXmppOnline()) {
                throw new BusinessException("客服未登录");
            }

            Visitor visitor;
            if (StringUtils.isNumeric(user.getTo())) {
                visitor = userServer.initVisitorByUid(Long.parseLong(user.getTo()));
            } else {
                visitor = userServer.initUserByOpenId(user.getTo());
            }

            visitor.setCustomer(customer);
            logger.info(JSONUtil.toJson(visitor));

            ChatNode visitorChatNode = ChatNodeManager.getVisitorXmppNode(visitor);
            ChatNodeAdaptation chatNodeAdaptation = new ChatNodeAdaptation(visitorChatNode);
            AbstractTerminal wn = accessWeChatTerminalVisitorFactory.getNode(chatNodeAdaptation,visitor);

            logger.info(JSONUtil.toJson(visitor));
            visitorChatNode.login();
            visitorChatNode.online(wn);
            customer.setTo(visitor.getId());
            request.getSession().setAttribute(Common.USER_KEY, customer);
            mv.addObject("user", JSONUtil.toJson(customer));
            mv.addObject("to", user.getTo() + "@126xmpp");
            mv.setViewName("/customerSimple");
        } catch (BusinessException e) {
            logger.error("error", e);
            mv.setViewName("/index");
            mv.addObject("msg", e.getMessage());
        } catch (Exception e) {
            logger.error("error", e);
            mv.setViewName("/index");
            mv.addObject("msg", "系统异常");
        }
        return mv;
    }


    public Response getRespone(AbstractUser cu) {
        Response responseMsg = new Response();
        if (null == cu) {
            responseMsg.setMsg(Common.ErrorCode.LOGIN_ERROR.getCodeName());
            responseMsg.setCode(Common.ErrorCode.LOGIN_ERROR.getCode());
            responseMsg.setSuccess(false);
        } else {
            responseMsg.setSuccess(true);
        }
        return responseMsg;
    }

    /**
     * 根据请求数据，
     *
     * @param user
     * @return
     * @throws BusinessException
     */


    public void customerInit(Customer customer, LoginModel user) throws BusinessException {
        if (StringUtils.isBlank(user.getUsername())) {
            throw new BusinessException("[username]参数用户名不能为空");
        } else {
            user.setUsername(user.getUsername().toLowerCase());
        }

        String jid = XMPPUtil.nameToJid(user.getUsername());

        if (StringUtils.isBlank(user.getPassword())) {
            customer.setPassWord("111111");
        } else {
            customer.setPassWord(user.getPassword());
        }

        customer.setLoginUsername(user.getUsername());
        customer.setLoginTime(System.currentTimeMillis());
        customer.setOpenId(user.getUsername());
        customer.setId(jid);

    }


}
