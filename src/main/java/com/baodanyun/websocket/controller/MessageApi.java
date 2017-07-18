package com.baodanyun.websocket.controller;

import com.baodanyun.websocket.bean.Message;
import com.baodanyun.websocket.bean.PageResponse;
import com.baodanyun.websocket.bean.Response;
import com.baodanyun.websocket.listener.VisitorListener;
import com.baodanyun.websocket.model.MessageModel;
import com.baodanyun.websocket.service.MessageServer;
import com.baodanyun.websocket.util.AccessControlAllowUtils;
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
 * Created by yutao on 2016/10/4.
 */
@RestController
public class MessageApi extends BaseController {
    protected static Logger logger = LoggerFactory.getLogger(MessageApi.class);

    @Autowired
    private MessageServer messageServer;

    @Autowired
    private VisitorListener visitorListener;

    @RequestMapping(value = "getMessageByCId")
    public void getMessageByCId(MessageModel model, HttpServletResponse httpServletResponse) {
        PageResponse response = new PageResponse();
        try {
            if (StringUtils.isEmpty(model.getCid())) {
                response.setMsg("cid 参数不能为空");
                response.setSuccess(false);
            } else {
                List<Message> li = null;
                int total = 0;
                if (null == model.getStatus()) {
                    li = messageServer.getMessageByCId(model);
                    total = messageServer.getMessageCountByCId(model);
                } else {
                    li = messageServer.getMessageByCIdStatus(model);
                    total = messageServer.getMessageCountByCIdStatus(model);
                }


                response.setPage(model.getPage());
                response.setCount(model.getCount());
                response.setTotal(total);
                response.setPages(total / model.getCount() + 1);
                response.setData(li);
                response.setSuccess(true);


            }


        } catch (Exception e) {
            logger.error("error", e);
            response.setMsg("getTagsAll error");
            response.setSuccess(false);
        }
        AccessControlAllowUtils.access(httpServletResponse);
        Render.r(httpServletResponse, JSONUtil.toJson(response));
    }

    @RequestMapping(value = "getMessageById")
    public void getMessageById(MessageModel model, HttpServletResponse httpServletResponse) {
        PageResponse response = new PageResponse();
        try {
            if (null == model.getId()) {
                response.setMsg("id 参数不能为空");
                response.setSuccess(false);
            } else {
                List<Message> li = messageServer.getMessageById(model);
                int total = messageServer.getMessageCountById(model);

                response.setPage(model.getPage());
                response.setCount(model.getCount());
                response.setTotal(total);
                response.setPages(total / model.getCount() + 1);
                response.setData(li);
                response.setSuccess(true);
            }
        } catch (Exception e) {
            logger.error("error", e);
            response.setMsg("getTagsAll error");
            response.setSuccess(false);
        }
        AccessControlAllowUtils.access(httpServletResponse);
        Render.r(httpServletResponse, JSONUtil.toJson(response));
    }

    /**
     * 留言
     *
     * @param message
     * @param httpServletResponse
     */
    @RequestMapping(value = "addMessage")
    public void addMessage(MessageModel message, HttpServletResponse httpServletResponse) {
        Response response = new Response();
        try {
            if (StringUtils.isEmpty(message.addCheck())) {
                int stu = messageServer.addMessage(message);
                visitorListener.leaveMessage(message.getPhone(), message.getContent());
                response.setData(stu);
                response.setSuccess(true);
            } else {
                response.setMsg(message.addCheck());
                response.setSuccess(false);
            }
        } catch (Exception e) {
            logger.error("error", e);
            response.setMsg("update error");
            response.setSuccess(false);
        }

        AccessControlAllowUtils.access(httpServletResponse);
        Render.r(httpServletResponse, JSONUtil.toJson(response));
    }

    @RequestMapping(value = "updateMessage")
    public void updateMessage(Integer id, String result, HttpServletResponse httpServletResponse) {
        Response response = new Response();
        try {
            if (null == id) {
                response.setMsg("参数[id]不能为空");
                response.setSuccess(false);
            } else {
                if (StringUtils.isEmpty(result)) {
                    response.setMsg("处理内容[result]不能为空");
                    response.setSuccess(false);
                }

                int count = messageServer.updateMessage(id, result);
                response.setData(count);
                response.setSuccess(true);


            }
        } catch (Exception e) {
            logger.error("error", e);
            response.setMsg(e.getMessage());
            response.setSuccess(false);
        }
        Render.r(httpServletResponse, JSONUtil.toJson(response));
    }

}
