package com.baodanyun.websocket.core.interceptors;

import com.baodanyun.websocket.bean.user.AbstractUser;
import com.baodanyun.websocket.core.common.Common;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

import java.util.Map;

/**
 * Created by yutao on 2016/7/13.
 */
public class VisitorWebHandshakeInterceptor implements HandshakeInterceptor {

    @Override
    public boolean beforeHandshake(ServerHttpRequest serverHttpRequest, ServerHttpResponse serverHttpResponse, WebSocketHandler webSocketHandler, Map<String, Object> map) throws Exception {
        /** 在拦截器内强行修改websocket协议，将部分浏览器不支持的 x-webkit-deflate-frame 扩展修改成 permessage-deflate */
        if (serverHttpRequest.getHeaders().containsKey("Sec-WebSocket-Extensions")) {
            serverHttpRequest.getHeaders().set("Sec-WebSocket-Extensions", "permessage-deflate");
        }

        ServletServerHttpRequest req = (ServletServerHttpRequest) serverHttpRequest;
        AbstractUser visitor = (AbstractUser) req.getServletRequest().getSession().getAttribute(Common.USER_KEY);


        Map header = (Map) req.getServletRequest().getSession().getAttribute(Common.VISITOR_USER_HEADER);
        map.put(Common.USER_KEY, visitor);
        map.put(Common.VISITOR_USER_HEADER, header);
        map.put(Common.VISITOR_USER_HTTP_SESSION, ((ServletServerHttpRequest) serverHttpRequest).getServletRequest().getSession());
        return true;
    }

    @Override
    public void afterHandshake(ServerHttpRequest serverHttpRequest, ServerHttpResponse serverHttpResponse, WebSocketHandler webSocketHandler, Exception e) {

    }
}
