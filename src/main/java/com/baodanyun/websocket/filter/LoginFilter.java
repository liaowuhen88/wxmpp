package com.baodanyun.websocket.filter;

import com.baodanyun.websocket.bean.user.AbstractUser;
import com.baodanyun.websocket.bean.user.Customer;
import com.baodanyun.websocket.core.common.Common;
import com.baodanyun.websocket.exception.BusinessException;
import com.baodanyun.websocket.model.LoginModel;
import com.baodanyun.websocket.model.Ofproperty;
import com.baodanyun.websocket.model.Ofuser;
import com.baodanyun.websocket.service.OfpropertyService;
import com.baodanyun.websocket.service.OfuserService;
import com.baodanyun.websocket.service.impl.OfuserServiceImpl;
import com.baodanyun.websocket.util.Blowfish;
import com.baodanyun.websocket.util.ServletUtil;
import com.baodanyun.websocket.util.SpringContextUtil;
import com.baodanyun.websocket.util.XMPPUtil;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * LoginFilter
 */
@Component
public class LoginFilter implements Filter {
    private static Logger logger = LoggerFactory.getLogger(LoginFilter.class);

    private static final String PLATFORM = "uec"; //uec平台标识

    public void init(FilterConfig var1) throws ServletException {
    }

    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse resp = (HttpServletResponse) response;
        Object user = req.getSession().getAttribute(Common.USER_KEY);

        if (user == null) {
            user = this.startTalkFromUEC(req); //UEC平台
        }

        if (null != user) {
            chain.doFilter(request, response);
        } else {
            if (validate(req)) {
                chain.doFilter(request, response);
            } else {
                String uri = req.getRequestURI();
                if (uri.startsWith(req.getContextPath() + "/api/customer_chat") || uri.startsWith(req.getContextPath() + "/api/customer/chat")) {
                    logger.info("req.getSession().getAttribute(Common.USER_KEY) is null");
                    ServletUtil.redirect(resp, req.getContextPath() + "/customerlogin");
                }
            }
        }

    }

    private boolean validate(HttpServletRequest request) {
        String uri = request.getRequestURI();
        if (uri.startsWith(request.getContextPath() + "/resouces")
                || uri.startsWith(request.getContextPath() + "/api/receiveMsg")
                || uri.startsWith(request.getContextPath() + "/statistics")
                || uri.startsWith(request.getContextPath() + "/index")
                || uri.startsWith(request.getContextPath() + "/api/findLoginImage")
                || uri.startsWith(request.getContextPath() + "/api/loginApi")
                || uri.startsWith(request.getContextPath() + "/appKeyCheck")
                || uri.startsWith(request.getContextPath() + "/sockjs/newVisitor")
                || uri.startsWith(request.getContextPath() + "/visitorlogin")
                || uri.startsWith(request.getContextPath() + "/api/customerLogin")
                || uri.startsWith(request.getContextPath() + "/customerlogin")) {
            return true;
        }
        return false;
    }

    public void destroy() {

    }

    /**
     * UEC平台用客户名就可以登陆
     */
    private Customer startTalkFromUEC(HttpServletRequest req) {
        Customer customer = null;

        String platform = String.valueOf(req.getParameter("platform")); //uec平台
        String userName = String.valueOf(req.getParameter("username")); //客服名
        String to = String.valueOf(req.getParameter("to")); //要接入的用户

        if (StringUtils.isBlank(platform) || StringUtils.isBlank(userName) || StringUtils.isBlank(to)) {
            return null;
        }

        if (platform.equals(PLATFORM)) { //来源UEC平台
            OfuserService ofuserService = SpringContextUtil.getBean("ofuserServiceImpl", OfuserServiceImpl.class);
            Ofuser ofuser = ofuserService.getUserByUsername(userName);
            if (ofuser == null) {
                return null;
            }

            LoginModel loginModel = new LoginModel();
            loginModel.setAccessType("2");
            loginModel.setType("customer");
            loginModel.setUsername(userName);
            loginModel.setTo(to);

            String password = this.encryptPassword(ofuser); //用户密码名
            loginModel.setPassword(password);
            customer = customerInit(loginModel);

            req.getSession().setAttribute(Common.USER_KEY, customer);
        }
        return customer;
    }

    private String encryptPassword(Ofuser ofuser) {
        String encryptedString = "";
        try {
            OfpropertyService ofpropertyService = SpringContextUtil.getBean("ofpropertyServiceImpl", OfpropertyService.class);
            Ofproperty passwordKey = ofpropertyService.selectByPrimaryKey("passwordKey");
            String key = passwordKey.getPropvalue();
            Blowfish bf = new Blowfish(key);
            encryptedString = bf.decrypt(ofuser.getEncryptedpassword());
        } catch (Exception e) {
            e.printStackTrace();
        }

        return encryptedString;
    }

    private Customer customerInit(LoginModel user) {
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

}
