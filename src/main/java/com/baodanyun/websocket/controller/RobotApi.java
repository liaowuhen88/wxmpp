package com.baodanyun.websocket.controller;

import com.baodanyun.robot.dto.RobotDto;
import com.baodanyun.robot.service.ReportCaseService;
import com.baodanyun.websocket.bean.Response;
import com.baodanyun.websocket.model.RobotReportCase;
import com.baodanyun.websocket.util.JSONUtil;
import com.baodanyun.websocket.util.Render;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 机器人[我要报案]接口
 */
@RestController
public class RobotApi extends BaseController {
    @Autowired
    private ReportCaseService reportCaseService;

    /**
     * 根据uid查询机器人数据
     *
     * @param uid             用户id
     * @param servletResponse
     */
    @RequestMapping("robot/{uid}")
    public void getReportCaseByUid(@PathVariable Long uid, HttpServletResponse servletResponse) {
        Response response = new Response();
        if (uid == null) {
            response.setMsg("uid不能为空");
            Render.r(servletResponse, JSONUtil.toJson(response));
            return;
        }
        List<RobotDto> reportCaseList = reportCaseService.getReportCaseByUid(uid);
        response.setSuccess(true);
        response.setData(reportCaseList);

        Render.r(servletResponse, JSONUtil.toJson(response));
    }

    /**
     * 根据uid查询机器人数据
     *
     * @param phone           用户电话
     * @param servletResponse
     */
    @RequestMapping("robot/user/{phone}")
    public void getReportCaseByPhone(@PathVariable Long phone, HttpServletResponse servletResponse) {
        Response response = new Response();
        if (phone == null) {
            response.setMsg("电话不能为空");
            Render.r(servletResponse, JSONUtil.toJson(response));
            return;
        }
        List<RobotDto> reportCaseList = reportCaseService.getReportCaseByPhone(phone);
        response.setSuccess(true);
        response.setData(reportCaseList);

        Render.r(servletResponse, JSONUtil.toJson(response));
    }

}
