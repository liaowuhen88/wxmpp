package com.baodanyun.websocket.springConfig;

import com.baodanyun.websocket.filter.LoginFilter;
import com.baodanyun.websocket.filter.ResReqContentFilter;
import com.baodanyun.websocket.listener.SessionCounter;
import com.thetransactioncompany.cors.CORSConfiguration;
import com.thetransactioncompany.cors.CORSConfigurationException;
import com.thetransactioncompany.cors.CORSFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.web.WebApplicationInitializer;

import javax.servlet.DispatcherType;
import javax.servlet.FilterRegistration;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import java.util.EnumSet;
import java.util.Properties;

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
        Properties props = new Properties();
        props.put("cors.allowOrigin", "http://kf.17doubao.com");
        props.put("cors.supportedMethods", "GET, POST, HEAD, PUT, DELETE");
        props.put("cors.supportedHeaders", "Accept,Origin,X-Requested-With,Content-Type,Last-Modified");
        props.put("cors.exposedHeaders", "Set-Cookie");
        props.put("cors.supportsCredentials", "true");
        try {
            CORSConfiguration cf = new CORSConfiguration(props);
            CORSFilter co = new CORSFilter(cf);
            FilterRegistration.Dynamic corsFilter = servletContext.addFilter(
                    "corsFilter", co);
            corsFilter.addMappingForUrlPatterns(
                    EnumSet.of(DispatcherType.REQUEST, DispatcherType.FORWARD, DispatcherType.INCLUDE), false, "/*");


        } catch (CORSConfigurationException e) {
            throw new RuntimeException(e);
        }


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
