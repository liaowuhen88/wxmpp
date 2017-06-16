package com.baodanyun.websocket.controller;

import com.baodanyun.websocket.bean.Response;
import com.baodanyun.websocket.service.XmppUserOnlineServer;
import com.baodanyun.websocket.util.JSONUtil;
import com.baodanyun.websocket.util.Render;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;

/**
 * Created by liaowuhen on 2016/10/4.
 */
@RestController
public class XmppUserOnlineApi extends BaseController {
    protected static Logger logger = LoggerFactory.getLogger(XmppUserOnlineApi.class);

    @Autowired
    private XmppUserOnlineServer xmppUserOnlineServer;

    @RequestMapping(value = "isOnline")
    public void isOnline(String uid, HttpServletResponse httpServletResponse) {
        Response response = new Response();
        try {
            boolean flag = xmppUserOnlineServer.isOnline(uid);

            response.setData(flag);
            response.setSuccess(true);

        } catch (Exception e) {
            logger.error("error", e);
            response.setMsg("isOnline error");
            response.setSuccess(false);
        }
        Render.r(httpServletResponse, JSONUtil.toJson(response));
    }


}
