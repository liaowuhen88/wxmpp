package com.baodanyun.websocket.controller;

import com.baodanyun.websocket.bean.Response;
import com.baodanyun.websocket.service.XmppServer;
import com.baodanyun.websocket.util.JSONUtil;
import com.baodanyun.websocket.util.Render;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Set;

/**
 * Created by yutao on 2016/10/4.
 * 用于监控  ws Session 以及xmpp的状态信息
 */
@RestController
public class MonitorApi extends BaseController {
    protected static Logger logger = LoggerFactory.getLogger(MonitorApi.class);

    @Autowired
    private XmppServer xmppServer;

    @RequestMapping(value = "monitor")
    public void api(HttpServletRequest request, HttpServletResponse response) {
        Response re = new Response();
        re.setSuccess(true);
        re.setData("ALL_PASS=OK");
        Render.r(response, JSONUtil.toJson(re));
    }

    @RequestMapping(value = "getConnectionIds")
    public void getConnectionIds(HttpServletRequest request, HttpServletResponse response) {
        Response re = new Response();
        re.setSuccess(true);
        Set<String> ids = xmppServer.getJids();
        re.setData(JSONUtil.toJson(ids));
        Render.r(response, JSONUtil.toJson(re));
    }
}
