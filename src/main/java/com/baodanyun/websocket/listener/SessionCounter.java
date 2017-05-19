package com.baodanyun.websocket.listener;

/**
 * Created by liaowuhen on 2016/12/15.
 */

import com.baodanyun.websocket.bean.user.AbstractUser;
import com.baodanyun.websocket.core.common.Common;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

public class SessionCounter implements HttpSessionListener {
    protected final Logger logger = LoggerFactory.getLogger(this.getClass());

    /* Session创建事件 */
    public void sessionCreated(HttpSessionEvent se) {
        se.getSession().setMaxInactiveInterval(36000);
        logger.info("session created---id:[" + se.getSession().getId() + "]--------过期时间:[" + se.getSession().getMaxInactiveInterval() + "]");
    }

    /* Session失效事件 */
    public void sessionDestroyed(HttpSessionEvent se) {
        AbstractUser user = (AbstractUser) se.getSession().getAttribute(Common.USER_KEY);
        if(null != user){
            logger.info("session Destroyed---id:[" + user.getId() + "]--------过期时间:[" + se.getSession().getMaxInactiveInterval() + "]");
        }else {
            logger.info("session Destroyed---id:[" + se.getSession().getId() + "]--------过期时间:[" + se.getSession().getMaxInactiveInterval() + "]");
        }
    }
}
