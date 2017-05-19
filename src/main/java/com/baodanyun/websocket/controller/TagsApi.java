package com.baodanyun.websocket.controller;

import com.baodanyun.websocket.bean.Response;
import com.baodanyun.websocket.bean.Tags;
import com.baodanyun.websocket.service.TagsServer;
import com.baodanyun.websocket.util.JSONUtil;
import com.baodanyun.websocket.util.Render;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * Created by yutao on 2016/10/4.
 */
@RestController
public class TagsApi extends BaseController {
    protected static Logger logger = Logger.getLogger(TagsApi.class);

    @Autowired
    private TagsServer tagsServer;

    @RequestMapping(value = "getTagsAll")
    public void getTagsAll(HttpServletResponse httpServletResponse) {
        Response response = new Response();
        try {

            List<Tags> li = tagsServer.getTagsAll();

            response.setData(li);
            response.setSuccess(true);

        } catch (Exception e) {
            logger.error(e);
            response.setMsg("getTagsAll error");
            response.setSuccess(false);
        }
        Render.r(httpServletResponse, JSONUtil.toJson(response));
    }

    @RequestMapping(value = "addTags")
    public void updateQuickReply(String tagname, String uid, HttpServletResponse httpServletResponse) {
        Response response = new Response();
        try {
            if (StringUtils.isEmpty(tagname)) {
                response.setMsg("tagname参数异常");
                response.setSuccess(false);
            } else {
                List<Tags> li = null;
                if (!StringUtils.isEmpty(uid)) {
                    Integer co = tagsServer.addTags(tagname, uid);

                } else {
                    response.setMsg("uid参数异常");
                    response.setSuccess(false);
                }

                li = tagsServer.getTagsAll();

                response.setData(li);
                response.setSuccess(true);

            }

        } catch (Exception e) {
            logger.error(e);
            response.setMsg("update error");
            response.setSuccess(false);
        }
        Render.r(httpServletResponse, JSONUtil.toJson(response));
    }

    @RequestMapping(value = "deleteTags")
    public void deleteQuickReply(Integer id, HttpServletResponse httpServletResponse) {
        Response response = new Response();
        try {
            if (null != id) {
                int count = tagsServer.deleteTags(id);
                List<Tags> li = tagsServer.getTagsAll();
                response.setData(li);
                response.setSuccess(true);
            } else {
                response.setMsg("参数为空");
                response.setSuccess(false);
            }

        } catch (Exception e) {
            logger.error(e);
            response.setMsg(e.getMessage());
            response.setSuccess(false);
        }
        Render.r(httpServletResponse, JSONUtil.toJson(response));
    }

}
