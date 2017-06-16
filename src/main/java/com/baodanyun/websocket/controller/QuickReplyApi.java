package com.baodanyun.websocket.controller;

import com.baodanyun.websocket.bean.QuickReply;
import com.baodanyun.websocket.bean.Response;
import com.baodanyun.websocket.service.QuickReplyServer;
import com.baodanyun.websocket.util.JSONUtil;
import com.baodanyun.websocket.util.Render;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * Created by liaowuhen on 2016/11/11.
 */
@RestController
public class QuickReplyApi extends BaseController {
    protected static Logger logger = LoggerFactory.getLogger(CustomerApi.class);

    @Autowired
    private QuickReplyServer quickReplyServer;

    @RequestMapping(value = "updateQuickReply")
    public void updateQuickReply(String cjid, String message, HttpServletResponse httpServletResponse) {
        Response response = new Response();
        try {
            if (StringUtils.isEmpty(cjid)) {
                response.setMsg("cjid参数异常");
                response.setSuccess(false);

            } else {
                List<QuickReply> li = null;
                if (!StringUtils.isEmpty(message)) {
                    Integer co = quickReplyServer.addQuickReply(cjid, message);

                }

                li = quickReplyServer.getQuickReply(cjid);

                response.setData(li);
                response.setSuccess(true);


            }

        } catch (Exception e) {
            logger.error("error", e);
            response.setMsg("update error");
            response.setSuccess(false);
        }
        Render.r(httpServletResponse, JSONUtil.toJson(response));
    }

    @RequestMapping(value = "deleteQuickReply")
    public void deleteQuickReply(Integer id, HttpServletResponse httpServletResponse) {
        Response response = new Response();
        try {
            if (null != id) {
                int count = quickReplyServer.deleteQuickReply(id);
                response.setData(count);
                response.setSuccess(true);
            } else {
                response.setMsg("参数为空");
                response.setSuccess(false);
            }

        } catch (Exception e) {
            logger.error("error", e);
            response.setMsg(e.getMessage());
            response.setSuccess(false);
        }
        Render.r(httpServletResponse, JSONUtil.toJson(response));
    }


}
