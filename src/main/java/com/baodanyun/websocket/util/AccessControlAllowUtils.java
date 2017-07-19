package com.baodanyun.websocket.util;

import javax.servlet.http.HttpServletResponse;

/**
 * Created by liaowuhen on 2017/7/17.
 */
public class AccessControlAllowUtils {
    /**
     * 允许跨域
     */
    public static void access(HttpServletResponse response) {
        response.setHeader("Access-Control-Allow-Credentials", "true");
        response.setHeader("Access-Control-Allow-Origin", "*");
    }

}
