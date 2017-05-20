package com.baodanyun.websocket.filter;

import com.baodanyun.websocket.core.common.Common;
import com.baodanyun.websocket.util.ServletUtil;
import org.apache.log4j.Logger;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * LoginFilter
 */
public class LoginFilter implements Filter {
    private static Logger logger = Logger.getLogger(LoginFilter.class);

    public void init(FilterConfig var1) throws ServletException {

    }

    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse resp = (HttpServletResponse) response;


        if (validate(req)) {
            chain.doFilter(request, response);
        } else {
            String uri = req.getRequestURI();
            if (uri.startsWith(req.getContextPath() + "/api/customer_chat") && null == req.getSession().getAttribute(Common.USER_KEY)) {
                logger.info("req.getSession().getAttribute(Common.USER_KEY) is null");
                ServletUtil.redirect(resp,req.getContextPath() + "/customerlogin");
            }else {
                chain.doFilter(request, response);
            }
        }
    }

    private boolean validate(HttpServletRequest request) {
        String uri = request.getRequestURI();
        if (uri.startsWith(request.getContextPath() + "/resouces")
                //这个api需要放出来
                || uri.startsWith(request.getContextPath() + "/api/findLoginImage")
                || uri.startsWith(request.getContextPath() + "/api/loginApi")
                || uri.startsWith(request.getContextPath() + "/visitorlogin")
                || uri.startsWith(request.getContextPath() + "/customerlogin")) {
            return true;
        }
        return false;
    }

    public void destroy() {

    }

}
