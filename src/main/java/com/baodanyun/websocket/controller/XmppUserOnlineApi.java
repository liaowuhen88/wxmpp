package com.baodanyun.websocket.controller;

import com.baodanyun.websocket.bean.ChatHistoryUser;
import com.baodanyun.websocket.bean.Response;
import com.baodanyun.websocket.bean.Tags;
import com.baodanyun.websocket.bean.user.Visitor;
import com.baodanyun.websocket.service.TagsServer;
import com.baodanyun.websocket.service.XmppUserOnlineServer;
import com.baodanyun.websocket.util.JSONUtil;
import com.baodanyun.websocket.util.Render;
import com.baodanyun.websocket.util.XmllUtil;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.jdom.Element;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.util.Iterator;
import java.util.List;

/**
 * Created by liaowuhen on 2016/10/4.
 */
@RestController
public class XmppUserOnlineApi extends BaseController {
    protected static Logger logger = Logger.getLogger(XmppUserOnlineApi.class);

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
            logger.error(e);
            response.setMsg("isOnline error");
            response.setSuccess(false);
        }
        Render.r(httpServletResponse, JSONUtil.toJson(response));
    }


}
