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
                } else {
                    logger.info(" 非法请求" + uri);

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
                || uri.startsWith(request.getContextPath() + "/customerlogin")
                || uri.startsWith(request.getContextPath() + "/api/doLoginForUecUser")
                || uri.startsWith(request.getContextPath() + "/api/customerAndJoin")
                || uri.startsWith(request.getContextPath() + "/talkFromUec")

                ) {
            return true;
        }
        return false;
    }

    public void destroy() {

    }
}
