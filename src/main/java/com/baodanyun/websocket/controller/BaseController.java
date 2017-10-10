package com.baodanyun.websocket.controller;

import com.baodanyun.websocket.bean.Response;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by yutao on 2016/10/4.
 */
@RestController
@RequestMapping("api")
public class BaseController {

    @InitBinder
    public void bindingPreparation(WebDataBinder binder) {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        CustomDateEditor dateEditor = new CustomDateEditor(dateFormat, true);
        binder.registerCustomEditor(Date.class, dateEditor);
    }

    protected Response success(Object data) {
        Response response = new Response();
        response.setData(data);
        response.setSuccess(true);

        return response;
    }

    protected Response error(String msg) {
        Response response = new Response();
        response.setSuccess(false);
        response.setMsg(msg);
        response.setCode(500);

        return response;
    }

    protected Response error(String msg, int code) {
        Response response = new Response();
        response.setSuccess(false);
        response.setMsg(msg);
        response.setCode(code);

        return response;
    }
}
