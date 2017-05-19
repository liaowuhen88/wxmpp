package com.baodanyun.websocket.util;

import com.baodanyun.websocket.bean.user.Visitor;
import com.baodanyun.websocket.core.common.Common;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * Created by yutao on 2016/7/12.
 */
public class ServletUtil {
    public static void redirect(HttpServletResponse response, String path) {
        try {
            response.sendRedirect(path);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void forward(ServletResponse response, ServletRequest request, String path) {
        RequestDispatcher dispatcher = request.getRequestDispatcher(path);
        try {
            dispatcher.forward(request, response);
        } catch (ServletException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void checkVisitorLoginSession(HttpServletRequest request, HttpServletResponse response) {
        Visitor vistor = (Visitor) request.getSession().getAttribute(Common.USER_KEY);
        if (vistor == null) {
            redirect(response, "/chat.jsp");
            return;
        }
    }

    private static HttpSession getCurrentSession() {
        ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
        return attr.getRequest().getSession(true);
    }

    public static Object getSessionInfo(String sessionKey) {
        return getCurrentSession().getAttribute(sessionKey);
    }

    public static void setSessionInfo(String sessionKey, Object object) {
        getCurrentSession().setAttribute(sessionKey, object);
    }
}
