package com.baodanyun.websocket.controller;

import com.baodanyun.robot.dto.AlarmStatisticsDto;
import com.baodanyun.websocket.bean.Response;
import com.baodanyun.websocket.exception.BusinessException;
import com.baodanyun.websocket.model.LoginModel;
import com.baodanyun.websocket.model.Ofuser;
import com.baodanyun.websocket.service.AlarmLogService;
import com.baodanyun.websocket.util.JSONUtil;
import com.baodanyun.websocket.util.Render;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 告警统计
 */
@RestController
public class AlarmStatisticsApi extends BaseController {

    @Autowired
    private AlarmLogService alarmLogService;

    @RequestMapping(value = "alarmStatistics")
    public ModelAndView customerLogin(LoginModel user, HttpServletRequest request) throws BusinessException {
        ModelAndView mv = new ModelAndView();
        mv.setViewName("/alarmStatistics");
        return mv;
    }

    /**
     * 告警次数统计
     *
     * @param alarmStatisticsDto
     * @param servletResponse
     */
    @RequestMapping(value = "alarmStastistics")
    public void statisticsAlarm(AlarmStatisticsDto alarmStatisticsDto, HttpServletResponse servletResponse) {
        Response response = new Response();

        if (alarmStatisticsDto.getBeginDate() == null || alarmStatisticsDto.getEndDate() == null) {
            response.setMsg("日期不能为空");
            Render.r(servletResponse, JSONUtil.toJson(response));
            return;
        }

        List<AlarmStatisticsDto> alarmList = alarmLogService.statisticsAlarm(alarmStatisticsDto);
        response.setData(alarmList); //统计
        response.setSuccess(true);
        Render.r(servletResponse, JSONUtil.toJson(response));
    }
}
