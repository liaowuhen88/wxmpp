package com.baodanyun.websocket.controller;

import com.baodanyun.websocket.bean.Response;
import com.baodanyun.websocket.model.Transferlog;
import com.baodanyun.websocket.service.TransferlogServer;
import com.baodanyun.websocket.util.JSONUtil;
import com.baodanyun.websocket.util.Render;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;

/**
 * Created by liaowuhen on 2016/11/11.
 */
@RestController
public class TransferlogApi extends BaseController {
    protected static Logger logger = LoggerFactory.getLogger(CustomerApi.class);

    @Autowired
    private TransferlogServer transferlogServer;

    @RequestMapping(value = "updateTransferlog")
    public void updateTransferlog(Transferlog tm, HttpServletResponse httpServletResponse) {
        Response response = new Response();
        try {
            if (StringUtils.isEmpty(tm.getTransferfrom()) || StringUtils.isEmpty(tm.getTransferto())) {
                response.setMsg("参数异常");
                response.setSuccess(false);

            } else {
                Integer co = transferlogServer.insert(tm);

                response.setData(co);
                response.setSuccess(true);

            }

        } catch (Exception e) {
            logger.error("error", e);
            response.setMsg("update error");
            response.setSuccess(false);
        }
        Render.r(httpServletResponse, JSONUtil.toJson(response));
    }

   /* @RequestMapping(value = "getTransferlog")
    public void getTransferlog(Transferlog tm, HttpServletResponse httpServletResponse) {
        Response response = new Response();
        try {
            List<Transferlog> li = transferlogServer
            response.setData(li);
            response.setSuccess(true);

        } catch (Exception e) {
            logger.error("error",e);
            response.setMsg(e.getMessage());
            response.setSuccess(false);
        }
        Render.r(httpServletResponse, JSONUtil.toJson(response));
    }*/


}
