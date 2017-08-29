package com.baodanyun.websocket.springConfig;

import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
 * Created by yutao on 2016/10/4.
 * 页面跳转单独配置
 */
public class SpringMvcConfig extends WebMvcConfigurerAdapter {

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/").setViewName("/WEB-INF/index.jsp");
        registry.addViewController("/index").setViewName("/WEB-INF/views/visitor/index.jsp");
        registry.addViewController("/demo.html").setViewName("/WEB-INF/views/visitor/demo.html");

        registry.addViewController("/visitor").setViewName("/WEB-INF/views/visitor/visitor.jsp");
        registry.addViewController("/visitorError").setViewName("/WEB-INF/views/visitor/visitorError.jsp");
        registry.addViewController("/customer/home").setViewName("/WEB-INF/views/admin/home.jsp");
        registry.addViewController("/customer/chat").setViewName("/WEB-INF/views/admin/chat.jsp");
        registry.addViewController("/customerlogin").setViewName("/customerlogin.jsp");
        registry.addViewController("/customer/history").setViewName("/WEB-INF/views/admin/history.jsp");
        registry.addViewController("/customer/set").setViewName("/WEB-INF/views/admin/set.jsp");
        registry.addViewController("/customerSimple").setViewName("/WEB-INF/views/admin/customerSimple.jsp");
        registry.addViewController("/talkFromUec").setViewName("/talkFromUec.jsp");
        registry.addViewController("/statistics").setViewName("/WEB-INF/views/admin/webChatMessage/statistics.jsp");
        registry.addViewController("/alarmStatistics").setViewName("/WEB-INF/views/admin/alarm/alarmStastistics.jsp");
        // 用户咨询跳转页
        registry.addViewController("/consultation.html").setViewName("/consultation.jsp");
    }
}
