package com.baodanyun.websocket.controller;

import com.baodanyun.websocket.bean.*;
import com.baodanyun.websocket.bean.msg.HistoryMsg;
import com.baodanyun.websocket.dao.ArchiveMessagesMapper;
import com.baodanyun.websocket.exception.BusinessException;
import com.baodanyun.websocket.model.HistoryMessageModel;
import com.baodanyun.websocket.model.HistoryMessageUserModel;
import com.baodanyun.websocket.bean.msg.Msg;
import com.baodanyun.websocket.service.ArchiveMessagesServer;
import com.baodanyun.websocket.service.MessageHistoryService;
import com.baodanyun.websocket.util.JSONUtil;
import com.mysql.jdbc.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by liaowuhen on 2016/10/18.
 */
@RestController
@RequestMapping("messageHistory")
public class MessageHistoryApi extends BaseController {

    @Autowired
    private MessageHistoryService messageHistoryService;

    @Autowired
    private ArchiveMessagesServer archiveMessagesServer;



    @RequestMapping(value = "query")
    public Response query(HistoryMessageModel model) {
        Response re = new Response();
        if (!StringUtils.isNullOrEmpty(model.check())) {
            re.setMsg(model.check());
            re.setSuccess(false);
            return re;
        }

        List<HistoryMsg> li = archiveMessagesServer.selectByFromAndTo(model);

        archiveMessagesServer.getHistoryMsgList(li);

        re.setSuccess(true);
        re.setData(li);
        return re;
    }

    @RequestMapping(value = "getUserHistoryList")
    public Response getUserHistoryList(HistoryMessageUserModel model) {
        // TODO 参数判断
        PageResponse re = new PageResponse();

        Map<String, Object> map = new HashMap<>();

        if (!org.apache.commons.lang.StringUtils.isEmpty(model.getId())) {

            List<MessageUser> li = null;
            try {
                li = messageHistoryService.getUserHistoryList(model);

                List<ChatHistoryUser> users = messageHistoryService.getUserHistoryList(li);

                Integer m = messageHistoryService.getUserHistoryCount(model);

                map.put("list", users);
                map.put("total", m);
                map.put("page", model.getPage());
                map.put("count", model.getCount());
                if (null != m) {
                    map.put("pages", m / model.getCount() + 1);
                } else {
                    map.put("pages", 0);
                }
                re.setSuccess(true);
                re.setData(map);

            } catch (BusinessException e) {
                e.printStackTrace();
            }
        } else {
            re.setSuccess(false);
            re.setMsg("参数id不能为空");
        }

        return re;
    }


}
