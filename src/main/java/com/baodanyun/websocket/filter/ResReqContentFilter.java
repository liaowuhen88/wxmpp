package com.baodanyun.websocket.filter;

import com.baodanyun.websocket.util.HttpResReqContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * ResReqContentFilter
 */
public class ResReqContentFilter implements Filter {
    protected final Logger logger = LoggerFactory.getLogger(this.getClass());

    public void destroy() {

    }

    public void doFilter(ServletRequest arg0, ServletResponse servletResponse, FilterChain arg2) throws IOException, ServletException {

        HttpServletResponse response = (HttpServletResponse) servletResponse;
        arg0.setCharacterEncoding("UTF-8");
        HttpResReqContext.setRequest((HttpServletRequest) arg0);
        HttpResReqContext.setResponse(response);
        arg2.doFilter(arg0, response);

       /* response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Methods", "POST, GET");
        response.setHeader("Access-Control-Max-Age", "3600");
        response.setHeader("Access-Control-Allow-Headers", "X-Requested-With,content-type");*/
        

    }

    public void init(FilterConfig arg0) throws ServletException {

    }

}
