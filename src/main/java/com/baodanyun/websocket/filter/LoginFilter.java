package com.baodanyun.websocket.filter;

import com.baodanyun.websocket.core.common.Common;
import com.baodanyun.websocket.util.ServletUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * LoginFilter
 */
public class LoginFilter implements Filter {
    private static Logger logger = LoggerFactory.getLogger(LoginFilter.class);

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
                || uri.startsWith(request.getContextPath() + "/visitorlogin")
                || uri.startsWith(request.getContextPath() + "/api/customerLogin")
                || uri.startsWith(request.getContextPath() + "/customerlogin")) {
            return true;
        }
        return false;
    }

    public void destroy() {

    }

}
