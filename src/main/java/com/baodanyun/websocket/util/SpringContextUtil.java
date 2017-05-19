package com.baodanyun.websocket.util;

import org.apache.log4j.Logger;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * 获取spring容器，以访问容器中定义的其他bean
 */
@Component
public class SpringContextUtil implements ApplicationContextAware {
    protected static Logger logger = Logger.getLogger(SpringContextUtil.class);
    // Spring应用上下文环境
    private static ApplicationContext applicationContext;

    /**
     * 实现ApplicationContextAware接口的回调方法，设置上下文环境
     */
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        SpringContextUtil.applicationContext = applicationContext;
    }

    public static ApplicationContext getApplicationContext() {
        return applicationContext;
    }

    /**
     * 获取对象
     * 这里重写了bean方法，起主要作用
     *
     * @param beanId
     * @return Object 一个以所给名字注册的bean的实例
     * @throws BeansException
     */
    public static <T> T getBean(String beanId, Class<T> c) throws BeansException {
        try {
            return applicationContext.getBean(beanId, c);
        } catch (Exception e) {
            logger.error("beanId:" + beanId, e);
            throw e;
        }
    }
}