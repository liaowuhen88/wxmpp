package com.baodanyun.websocket.util;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;

/**
 * HttpResReqContext
 * 用于获取全局的req,resp
 */
public class HttpResReqContext {

    private static ThreadLocal<HttpServletRequest> REQ_THREAD_LOCAL = new ThreadLocal<HttpServletRequest>();
    private static ThreadLocal<HttpServletResponse> RSP_THREAD_LOCAL = new ThreadLocal<HttpServletResponse>();

    public static HttpServletRequest getRequest() {
        return (HttpServletRequest) REQ_THREAD_LOCAL.get();
    }

    public static void setRequest(HttpServletRequest request) {
        REQ_THREAD_LOCAL.set(request);
    }

    public static HttpServletResponse getResponse() {
        return (HttpServletResponse) RSP_THREAD_LOCAL.get();
    }

    public static void setResponse(HttpServletResponse response) {
        RSP_THREAD_LOCAL.set(response);
    }

    public static HttpSession getSession() {
        return (HttpSession) ((HttpServletRequest) REQ_THREAD_LOCAL.get()).getSession();
    }

    public static Map<String, String> getHeaders(HttpServletRequest request) {
        Map<String, String> headers = new HashMap<>();
       /* headers.put(NodeProperties.P_LOGIN_TIME, String.valueOf(new Date().getTime()));
        headers.put(NodeProperties.P_PROTOCOL, request.getProtocol());
        headers.put(NodeProperties.P_UA, request.getHeader("User-Agent"));*/
//        headers.put(NodeProperties.P_IP, HttpUtils.getIp(request));
        return headers;
    }
}
