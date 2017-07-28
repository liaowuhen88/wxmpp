package com.baodanyun.websocket.core.interceptors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.sockjs.SockJsException;
import org.springframework.web.socket.sockjs.transport.SockJsServiceConfig;
import org.springframework.web.socket.sockjs.transport.SockJsSession;
import org.springframework.web.socket.sockjs.transport.TransportHandler;
import org.springframework.web.socket.sockjs.transport.TransportType;

/**
 * Created by liaowuhen on 2017/7/3.
 */
public class NewVisitorTransportHandler implements TransportHandler {
    private static final Logger logger = LoggerFactory.getLogger(NewVisitorTransportHandler.class);

    @Override
    public void initialize(SockJsServiceConfig serviceConfig) {

    }

    @Override
    public TransportType getTransportType() {
        return null;
    }

    @Override
    public void handleRequest(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler handler, SockJsSession session) throws SockJsException {
        logger.info("handleRequest");
    }
}
