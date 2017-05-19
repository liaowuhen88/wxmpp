package com.baodanyun.websocket.controller;

import com.baodanyun.websocket.bean.Response;
import com.baodanyun.websocket.bean.user.AbstractUser;
import com.baodanyun.websocket.bean.user.Visitor;
import com.baodanyun.websocket.service.UserCacheServer;
import com.baodanyun.websocket.util.JSONUtil;
import com.baodanyun.websocket.util.Render;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by yutao on 2016/10/10.
 */
@RestController
public class CloseFriendWindowApi extends BaseController {

    protected static Logger logger = Logger.getLogger(CloseFriendWindowApi.class);

    @Autowired
    private UserCacheServer userCacheServer;

    /**
     *
     * @param httpServletResponse
     */
    @RequestMapping(value = "closeFriendWindow", method = RequestMethod.POST)
    public void keepOnline(String jid, HttpServletRequest request, HttpServletResponse httpServletResponse) {

        AbstractUser u = new Visitor();
        u.setId(jid);

        //userCacheServer.delete(CommonConfig.USER_ONLINE,customer.getId(),u);


        Response response = new Response();

        response.setSuccess(true);

        Render.r(httpServletResponse, JSONUtil.toJson(response));
    }


}
