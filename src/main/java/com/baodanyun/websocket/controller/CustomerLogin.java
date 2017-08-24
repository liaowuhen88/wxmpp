package com.baodanyun.websocket.controller;

import com.baodanyun.websocket.bean.Response;
import com.baodanyun.websocket.bean.user.AbstractUser;
import com.baodanyun.websocket.bean.user.Customer;
import com.baodanyun.websocket.bean.user.Visitor;
import com.baodanyun.websocket.core.common.Common;
import com.baodanyun.websocket.enums.TeminalTypeEnum;
import com.baodanyun.websocket.exception.BusinessException;
import com.baodanyun.websocket.model.LoginModel;
import com.baodanyun.websocket.model.Ofproperty;
import com.baodanyun.websocket.model.Ofuser;
import com.baodanyun.websocket.node.*;
import com.baodanyun.websocket.service.CacheService;
import com.baodanyun.websocket.service.OfpropertyService;
import com.baodanyun.websocket.service.OfuserService;
import com.baodanyun.websocket.service.UserServer;
import com.baodanyun.websocket.util.*;
import org.apache.commons.lang.StringUtils;
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
public class CustomerLogin extends BaseController {

    protected static Logger logger = LoggerFactory.getLogger(CustomerApi.class);
    @Autowired
    OfpropertyService ofpropertyService;
    @Autowired
    private UserServer userServer;
    @Autowired
    private OfuserService ofuserService;
    @Autowired
    private AccessWeChatTerminalVisitorFactory accessWeChatTerminalVisitorFactory;
    @Autowired
    private CacheService cacheService;

    @RequestMapping(value = "loginApi", method = RequestMethod.POST)
    public void api(LoginModel user, HttpServletRequest request, HttpServletResponse response) {
        //客服必须填写用户名 和 密码
        logger.info("user" + JSONUtil.toJson(user));
        Customer customer = null;
        Response responseMsg = new Response();
        try {
            // 初始化客服，客服登录
            customer = customerInit(user);
            ofuserService.checkOfUser(customer.getLoginUsername(), customer.getPassWord());
            request.getSession().setAttribute(Common.USER_KEY, customer);
            responseMsg.setSuccess(true);

        } catch (BusinessException e) {
            responseMsg.setMsg(e.getMessage());
            responseMsg.setSuccess(false);
            logger.error("error", e);
        } catch (Exception e) {
            responseMsg.setMsg("系统异常");
            responseMsg.setSuccess(false);
            logger.error("error", e);
        }
        AccessControlAllowUtils.access(response);
        Render.r(response, XMPPUtil.buildJson(responseMsg));
    }


    /**
     * 第三方接入
     *
     * @param user
     * @param request
     * @return
     */
    @RequestMapping(value = "customerLogin")
    public ModelAndView customerLogin(LoginModel user, HttpServletRequest request) throws BusinessException {
        String userName = String.valueOf(request.getParameter("username")); //客服名
        String to = String.valueOf(request.getParameter("to")); //要接入的用户

        ModelAndView mv = new ModelAndView();
        Ofuser ofuser = ofuserService.getUserByUsername(userName);
        if (ofuser == null) {
            mv.addObject("notExistsCustomer", "用户[" + userName + "]不存在");
        } else {
            //客服必须填写用户名 和 密码
            logger.info("user" + JSONUtil.toJson(user));
            mv.addObject("user", user);
        }

        mv.setViewName("/talkFromUec");
        return mv;
    }

    /**
     * 客户没有登陆则用客服名登陆否则直接对话
     *
     * @param user
     * @param request
     * @param response
     */
    @RequestMapping(value = "customerAndJoin")
    public void talkFromUEC(LoginModel user, HttpServletRequest request, HttpServletResponse response) {
        AccessControlAllowUtils.access(response); //允许跨域

        Response responseMsg = new Response();
        responseMsg.setSuccess(true);
        String cacheKey = ""; //标识是第三方来源的key

        //客服必须填写用户名 和 密码
        logger.info("user" + JSONUtil.toJson(user));
        try {
            if (StringUtils.isBlank(user.getTo())) {
                responseMsg.setSuccess(false);
                responseMsg.setMsg("to 参数不能为空");
                Render.r(response, XMPPUtil.buildJson(responseMsg));
                return;
            }

            // 客服处理部分，确保客服在线
            Customer customer = customerInitUec(user);
            logger.info(JSONUtil.toJson(customer));
            CustomerChatNode cx = ChatNodeManager.getCustomerXmppNode(customer);
            if (!cx.isXmppOnline()) {
                responseMsg.setSuccess(false);
                responseMsg.setCode(-1);
                responseMsg.setMsg("客服未登录");
                Render.r(response, XMPPUtil.buildJson(responseMsg));
                return;
            }

            // 因为已经有客服终端启动，所以可以直接初始化用户
            // 初始化用户
            Visitor visitor;
            if (StringUtils.isNumeric(user.getTo())) {
                visitor = userServer.initVisitorByUid(Long.parseLong(user.getTo()));
            } else {
                visitor = userServer.initUserByOpenId(user.getTo());
            }
            logger.info(JSONUtil.toJson(visitor));
            if (null == visitor) {
                responseMsg.setSuccess(false);
                responseMsg.setMsg("获取用户消息失败");
                Render.r(response, XMPPUtil.buildJson(responseMsg));
                return;
            }
            customer.setTo(visitor.getId());

            // 初始化用户终端
            VisitorChatNode visitorChatNode = ChatNodeManager.getVisitorXmppNode(visitor);

            ChatNodeAdaptation chatNodeAdaptation = new ChatNodeAdaptation(visitorChatNode);
            AbstractTerminal wn = accessWeChatTerminalVisitorFactory.getNode(chatNodeAdaptation, visitor);
            visitorChatNode.setCurrentChatNode(cx);
            logger.info(JSONUtil.toJson(visitor));
            visitorChatNode.login();

            // 用户上线并且通知客服
            cacheKey = visitor.getId();
            MsgSourceUtil.put(cacheKey, TeminalTypeEnum.UEC.getCode()); //标识来源于UEC平台

            visitorChatNode.online(wn);
            responseMsg.setCode(1);
        } catch (BusinessException e) {
            responseMsg.setMsg(e.getMessage());
            responseMsg.setSuccess(false);
            MsgSourceUtil.remove(cacheKey);

            logger.error("error", e);
        } catch (Exception e) {
            responseMsg.setMsg("系统异常");
            responseMsg.setSuccess(false);
            MsgSourceUtil.remove(cacheKey);

            logger.error("error", e);
        }

        Render.r(response, XMPPUtil.buildJson(responseMsg));
    }

    /**
     * UEC平台接入的对话，客服不在线则为其登陆
     *
     * @param user
     * @param request
     * @return
     */
    @RequestMapping(value = "doLoginForUecUser")
    public ModelAndView talkFromUEC(LoginModel user, HttpServletRequest request) {
        ModelAndView mv = new ModelAndView();
        try {
            this.startTalkFromUEC(request);
            mv.setViewName("redirect:customer_chat");
        } catch (BusinessException e) {
            logger.error("error", e);
            mv.addObject("msg", e.getMessage());
            mv.setViewName("/talkFromUec");

        } catch (Exception e) {
            logger.error("error", e);
            mv.addObject("msg", "系统异常");
            mv.setViewName("/talkFromUec");

        }

        return mv;
    }

    /**
     * UEC平台用客户名就可以登陆
     */
    private void startTalkFromUEC(HttpServletRequest req) throws BusinessException, XMPPException, IOException, SmackException {
        String userName = String.valueOf(req.getParameter("username")); //客服名
        String to = String.valueOf(req.getParameter("to")); //要接入的用户
        Ofuser ofuser = ofuserService.getUserByUsername(userName);
        if (ofuser == null) {
            throw new BusinessException("账号[" + userName + "]不存在");
        }

        LoginModel loginModel = new LoginModel();
        // 不接入用户
        loginModel.setAccessType(LoginModel.NOT_ACCESS);
        loginModel.setType("customer");
        loginModel.setUsername(userName);
        loginModel.setTo(to);

        String password = this.encryptPassword(ofuser); //用户密码名
        loginModel.setPassword(password);
        Customer customer = customerInitUec(loginModel);

        logger.info(JSONUtil.toJson(customer));
        // CustomerChatNode cx = ChatNodeManager.getCustomerXmppNode(customer);

        req.getSession().setAttribute(Common.USER_KEY, customer);
    }

    /**
     * 解密
     *
     * @param ofuser
     * @return
     */
    private String encryptPassword(Ofuser ofuser) {
        String encryptedString = "";
        try {
            Ofproperty passwordKey = ofpropertyService.selectByPrimaryKey("passwordKey");
            String key = passwordKey.getPropvalue();
            Blowfish bf = new Blowfish(key);
            encryptedString = bf.decrypt(ofuser.getEncryptedpassword());
        } catch (Exception e) {
            e.printStackTrace();
        }

        return encryptedString;
    }

    /**
     * 初始化UEC客户属性
     *
     * @param user
     * @return
     */
    private Customer customerInitUec(LoginModel user) {
        Customer customer = new Customer();

        customer.setPassWord(user.getPassword());
        customer.setLoginUsername(user.getUsername());
        customer.setLoginTime(System.currentTimeMillis());
        customer.setOpenId(user.getUsername());
        customer.setNickName(user.getUsername());
        customer.setId(XMPPUtil.nameToJid(user.getUsername()));
        customer.setAccessType(user.getAccessType());
        customer.setTo(user.getTo());
        customer.setUserType(AbstractUser.UserType.uec); //UEC平台接入的用户

        return customer;
    }

    /**
     * 根据请求数据，
     *
     * @param user
     * @return
     * @throws BusinessException
     */
    public Customer customerInit(LoginModel user) throws BusinessException {
        Customer customer = new Customer();
        if (StringUtils.isBlank(user.getUsername())) {
            throw new BusinessException("[username]参数用户名不能为空");
        } else {
            user.setUsername(user.getUsername().toLowerCase());
        }

        String jid = XMPPUtil.nameToJid(user.getUsername());

        customer.setPassWord(user.getPassword());
        customer.setLoginUsername(user.getUsername());
        customer.setLoginTime(System.currentTimeMillis());
        customer.setOpenId(user.getUsername());
        customer.setNickName(user.getUsername());
        customer.setId(jid);
        customer.setAccessType(user.getAccessType());
        return customer;
    }


}
