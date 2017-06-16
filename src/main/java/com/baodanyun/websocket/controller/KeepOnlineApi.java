package com.baodanyun.websocket.controller;

import com.baodanyun.websocket.bean.Response;
import com.baodanyun.websocket.bean.user.AbstractUser;
import com.baodanyun.websocket.core.common.Common;
import com.baodanyun.websocket.util.JSONUtil;
import com.baodanyun.websocket.util.Render;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by yutao on 2016/10/10.
 */
@RestController
public class KeepOnlineApi extends BaseController {

    protected static Logger logger = LoggerFactory.getLogger(KeepOnlineApi.class);

    /**
     * 保持在线的接口
     *
     * @param httpServletResponse
     */
    @RequestMapping(value = "keepOnline", method = RequestMethod.POST)
    public void keepOnline(HttpServletRequest request, HttpServletResponse httpServletResponse) {

        AbstractUser au = (AbstractUser) request.getSession().getAttribute(Common.USER_KEY);

        if (null != au) {
            logger.info("au.getLoginUsername:[" + au.getLoginUsername() + "]----------au.getUserName():[" + au.getUserName() + "]----keepOnline");

        } else {
            logger.info("au.getLoginUsername:[is null]");

        }

        Response response = new Response();

        response.setSuccess(true);

        Render.r(httpServletResponse, JSONUtil.toJson(response));
    }


}
