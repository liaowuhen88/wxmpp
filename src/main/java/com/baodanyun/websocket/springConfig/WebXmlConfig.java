package com.baodanyun.websocket.springConfig;

import com.baodanyun.websocket.filter.LoginFilter;
import com.baodanyun.websocket.filter.ResReqContentFilter;
import com.baodanyun.websocket.listener.SessionCounter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.web.WebApplicationInitializer;

import javax.servlet.DispatcherType;
import javax.servlet.FilterRegistration;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import java.util.EnumSet;

/**
 * Created by liaowuhen on 2016/11/2.
 *  类似于web.xml文件，项目启动会执行onStartup 方法
 */
@Order(1)
public class WebXmlConfig implements WebApplicationInitializer {
    protected static Logger logger = LoggerFactory.getLogger(WebXmlConfig.class);

    @Override
    public void onStartup(ServletContext servletContext)
            throws ServletException {

        //Log4jConfigListener
        servletContext.setInitParameter("contextConfigLocation", "classpath:spring-conf.xml");
        servletContext.addListener(SessionCounter.class);
        /*servletContext.addListener(Log4jConfigListener.class);*/

        //OpenSessionInViewFilter
        ResReqContentFilter ref = new ResReqContentFilter();
        FilterRegistration.Dynamic dref = servletContext.addFilter(
                "resReqContentFilter", ref);
        dref.addMappingForUrlPatterns(
                EnumSet.of(DispatcherType.REQUEST, DispatcherType.FORWARD, DispatcherType.INCLUDE), false, "/*");

        LoginFilter lf = new LoginFilter();
        FilterRegistration.Dynamic dlf = servletContext.addFilter(
                "loginFilter", lf);
        dlf.addMappingForUrlPatterns(
                EnumSet.of(DispatcherType.REQUEST, DispatcherType.FORWARD, DispatcherType.INCLUDE), false, "/*");


      /*  //DemoServlet
        DemoServlet demoServlet = new DemoServlet();
        ServletRegistration.Dynamic dynamic = servletContext.addServlet(
                "demoServlet", demoServlet);
        dynamic.setLoadOnStartup(2);
        dynamic.addMapping("/demo_servlet");*/
        // 设置session过期时间

        servletContext.getSessionCookieConfig().setMaxAge(36000);
        logger.info("WebXmlConfig :onStartup");
    }
}
