package com.baodanyun.websocket.controller;

import com.baodanyun.websocket.bean.Response;
import com.baodanyun.websocket.bean.msg.HistoryMsg;
import com.baodanyun.websocket.bean.user.Customer;
import com.baodanyun.websocket.bean.userInterface.user.PersonalInfo;
import com.baodanyun.websocket.core.common.Common;
import com.baodanyun.websocket.exception.BusinessException;
import com.baodanyun.websocket.quality.dto.QualitySearchDto;
import com.baodanyun.websocket.service.ArchiveMessagesServer;
import com.baodanyun.websocket.service.QualityCheckService;
import com.baodanyun.websocket.util.JSONUtil;
import com.baodanyun.websocket.util.Render;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

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
        Customer customer = (Customer) request.getSession().getAttribute(Common.USER_KEY);
        mv.addObject("customer", customer);
        mv.addObject("isCustomerLeader", customer.getLoginUsername().equals(Common.CUSTOMER_LEADER));

        mv.setViewName("/qualityCheck");
        return mv;
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
}
