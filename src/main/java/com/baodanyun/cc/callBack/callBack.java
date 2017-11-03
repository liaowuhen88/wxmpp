package com.baodanyun.cc.callBack;

import com.baodanyun.cc.bean.CallSheetEvent;
import com.baodanyun.cc.event.CCEvent;
import com.baodanyun.websocket.bean.Response;
import com.baodanyun.websocket.controller.BaseController;
import com.baodanyun.websocket.exception.BusinessException;
import com.baodanyun.websocket.util.EventBusUtils;
import com.baodanyun.websocket.util.JSONUtil;
import com.baodanyun.websocket.util.Render;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;

/**
 * Created by liaowuhen on 2017/10/31.
 */
@RestController
@RequestMapping(value = "api/call")
public class CallBack extends BaseController {
    protected final Logger logger = LoggerFactory.getLogger(this.getClass());

    //呼叫振铃Ring(话务进入呼叫中心触发的事件)
    @RequestMapping(value = "Ring")
    public void Ring(CallSheetEvent callSheet, HttpServletResponse servletResponse) throws BusinessException {
        sendResponse(servletResponse);
        sendEvent(callSheet, "123");
        //logger.info("Ring------{}", JSONUtil.toJson(callSheet));
    }

    // 被呼振铃Ringing
    @RequestMapping(value = "Ringing")
    public void Ringing(CallSheetEvent callSheet, HttpServletResponse servletResponse) throws BusinessException {
        sendResponse(servletResponse);
        sendEvent(callSheet, "456");
        //logger.info("Ringing------{}", JSONUtil.toJson(callSheet));
    }

    @RequestMapping(value = "Link")
    public void Link(CallSheetEvent callSheet, HttpServletResponse servletResponse) throws BusinessException {
        sendResponse(servletResponse);
        sendEvent(callSheet, "789");
        //logger.info("Link------{}", JSONUtil.toJson(callSheet));
    }

    private void sendResponse(HttpServletResponse servletResponse) {
        Response response = new Response();
        response.setCode(200);
        Render.r(servletResponse, JSONUtil.toJson(response));
    }

    private void sendEvent(CallSheetEvent callSheet, String evnetNum) {
        CCEvent ce = new CCEvent();
        ce.setCallSheet(callSheet);
        ce.setEvnetNum(evnetNum);
        EventBusUtils.post(ce);
    }

}
