package com.baodanyun.websocket.controller;

import com.baodanyun.websocket.bean.Response;
import com.baodanyun.websocket.bean.msg.HistoryMsg;
import com.baodanyun.websocket.bean.userInterface.user.PersonalInfo;
import com.baodanyun.websocket.exception.BusinessException;
import com.baodanyun.websocket.quality.dto.QualitySearchDto;
import com.baodanyun.websocket.service.ArchiveMessagesServer;
import com.baodanyun.websocket.service.QualityCheckService;
import com.baodanyun.websocket.util.DateUtils;
import com.baodanyun.websocket.util.JSONUtil;
import com.baodanyun.websocket.util.Render;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

/**
 * 质量检查之聊天记录查看
 */
@RestController
public class QualityCheckerApi extends BaseController {

    @Autowired
    private QualityCheckService qualityCheckService;
    @Autowired
    private ArchiveMessagesServer archiveMessagesServer;

    /**
     * 跳转到质检首页
     *
     * @return
     * @throws BusinessException
     */
    @RequestMapping(value = "qualityCheck")
    public ModelAndView customerLogin(HttpServletRequest request) throws BusinessException {
        ModelAndView mv = new ModelAndView();

        mv.addObject("totalMap", initCount());
        mv.addObject("startTime", DateUtils.getNowTime("yyyy-MM-dd 00:00:00"));
        mv.addObject("endTime", DateUtils.getNowTime("yyyy-MM-dd 23:59:59"));

        mv.setViewName("/qualityCheck");
        return mv;
    }

    /**
     * 默认加载当天全部的事件数
     *
     * @return
     */
    private Map<String, Integer> initCount() {
        QualitySearchDto searchDto = new QualitySearchDto();
        searchDto.setBeginDate(DateUtils.getNowTime("yyyy-MM-dd 00:00:00"));
        searchDto.setEndDate(DateUtils.getNowTime("yyyy-MM-dd 23:59:59"));

        return qualityCheckService.getEventTotalMap(searchDto);
    }

    /**
     * 查询客服在日期内服务的所有用户列表
     *
     * @param searchDto       请求参数dto
     * @param servletResponse
     */
    @RequestMapping(value = "findAllGuestName")
    public void statisticsAlarm(QualitySearchDto searchDto, HttpServletResponse servletResponse) {
        Response response = new Response();

        if (StringUtils.isBlank(searchDto.getCustomerName())) {
            response.setMsg("客服名不能为空");
            Render.r(servletResponse, JSONUtil.toJson(response));
            return;
        }
        if (StringUtils.isBlank(searchDto.getBeginDate())) {
            response.setMsg("开始日期不能为空");
            Render.r(servletResponse, JSONUtil.toJson(response));
            return;
        }
        if (StringUtils.isBlank(searchDto.getEndDate())) {
            response.setMsg("结束日期不能为空");
            Render.r(servletResponse, JSONUtil.toJson(response));
            return;
        }

        List<String> nameList = qualityCheckService.findAllGuestName(searchDto);
        List<PersonalInfo> personalInfoList = qualityCheckService.findUserList(nameList);
        response.setData(personalInfoList);
        response.setSuccess(true);
        Render.r(servletResponse, JSONUtil.toJson(response));
    }

    /**
     * 从mongo加载用户构造详情
     *
     * @param code
     * @param searchDto
     * @param servletResponse
     */
    @RequestMapping(value = "loadEvtData/{code}")
    public void loadEvtData(@PathVariable int code, QualitySearchDto searchDto, HttpServletResponse servletResponse) {
        Response response = new Response();

        if (StringUtils.isBlank(searchDto.getBeginDate())) {
            response.setMsg("开始日期不能为空");
            Render.r(servletResponse, JSONUtil.toJson(response));
            return;
        }
        if (StringUtils.isBlank(searchDto.getEndDate())) {
            response.setMsg("结束日期不能为空");
            Render.r(servletResponse, JSONUtil.toJson(response));
            return;
        }

        List<PersonalInfo> personalInfoList = qualityCheckService.findMongoEvtData(code, searchDto);
        response.setData(personalInfoList);
        response.setSuccess(true);
        Render.r(servletResponse, JSONUtil.toJson(response));
    }


    /**
     * 查询客服在日期区间内与此用户的所有聊天记录
     *
     * @param searchDto
     * @param servletResponse
     */
    @RequestMapping(value = "loadChatMsgFromUser")
    public void loadChatMsgFromUser(QualitySearchDto searchDto, HttpServletResponse servletResponse) {
        Response response = new Response();
        List<HistoryMsg> userMsgList = qualityCheckService.loadChatMsgFromUser(searchDto);
        archiveMessagesServer.getHistoryMsgList(userMsgList);

        response.setData(userMsgList);
        response.setSuccess(true);
        Render.r(servletResponse, JSONUtil.toJson(response));
    }

    /**
     * 按条件加载事件数量
     *
     * @param searchDto
     * @param servletResponse
     */
    @RequestMapping(value = "statisticCount")
    public void statisticCount(QualitySearchDto searchDto, HttpServletResponse servletResponse) {
        Response response = new Response();
        Map<String, Integer> map = qualityCheckService.getEventTotalMap(searchDto);
        response.setData(map);
        response.setSuccess(true);
        Render.r(servletResponse, JSONUtil.toJson(response));
    }

}
