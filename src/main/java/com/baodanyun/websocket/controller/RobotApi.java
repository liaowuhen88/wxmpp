package com.baodanyun.websocket.controller;

import com.baodanyun.robot.dto.RobotDto;
import com.baodanyun.robot.dto.RobotSearchDto;
import com.baodanyun.robot.service.ReportCaseService;
import com.baodanyun.websocket.bean.Response;
import com.baodanyun.websocket.util.DateUtils;
import com.baodanyun.websocket.util.JSONUtil;
import com.baodanyun.websocket.util.Render;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    /**
     * 机器人所有报案数据
     * 分页(日期可选择,开始结束日期须同时存在)
     */
    @RequestMapping("robot/repotcases")
    public void loadAllRobotList(RobotSearchDto searchDto, HttpServletResponse servletResponse) {
        Response response = new Response();
        SimpleDateFormat df = new SimpleDateFormat(DateUtils.DATE_FULL_STR);
        String bTime = searchDto.getBeginTime();
        String eTime = searchDto.getEndTime();

        if (searchDto.getCount() <= 0) {
            response.setSuccess(false);
            response.setMsg("每页显示条数不能小于0");
            Render.r(servletResponse, JSONUtil.toJson(response));
            return;
        } else if (searchDto.getCount() > 10) {
            searchDto.setCount(10);
        }

        if (searchDto.getPage() <= 0) {
            searchDto.setPage(1);
        }

        if (StringUtils.isNotBlank(bTime)) {
            try {
                df.parse(bTime);
            } catch (Exception e) {
                response.setSuccess(false);
                response.setMsg("beginTime日期格式不合法");
                Render.r(servletResponse, JSONUtil.toJson(response));
                return;
            }
        }
        if (StringUtils.isNotBlank(eTime)) {
            try {
                df.parse(eTime);
            } catch (Exception e) {
                response.setSuccess(false);
                response.setMsg("endTime日期格式不合法");
                Render.r(servletResponse, JSONUtil.toJson(response));
                return;
            }
        }
        if ((StringUtils.isNotBlank(bTime) && StringUtils.isBlank(eTime)) || (StringUtils.isBlank(bTime) && StringUtils.isNotBlank(eTime))) {
            response.setSuccess(false);
            response.setMsg("开始日期结束日期格式必填");
            Render.r(servletResponse, JSONUtil.toJson(response));
            return;
        }

        Map<String, Object> map = new HashMap<>();

        List<RobotDto> dataList = reportCaseService.findPageList(searchDto);
        long totalCount = reportCaseService.getTotalCount(searchDto);
        map.put("list", dataList); //数据
        map.put("totalCount", totalCount); //总记录数

        boolean flag = totalCount % searchDto.getCount() == 0;
        map.put("totalPages", flag ? (totalCount / searchDto.getCount()) : (totalCount / searchDto.getCount() + 1)); //总页数
        map.put("currentPage", searchDto.getPage());
        map.put("pageSize", searchDto.getCount());

        response.setSuccess(true);
        response.setData(map);
        Render.r(servletResponse, JSONUtil.toJson(response));
    }

}
