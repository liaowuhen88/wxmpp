package com.baodanyun.robot.service;

import com.alibaba.fastjson.JSON;
import com.baodanyun.robot.common.RobotConstant;
import com.baodanyun.websocket.bean.msg.Msg;
import com.baodanyun.websocket.bean.user.AbstractUser;
import com.baodanyun.websocket.dao.RobotReportCaseMapper;
import com.baodanyun.websocket.enums.ReportCaseEnum;
import com.baodanyun.websocket.model.RobotReportCase;
import com.baodanyun.websocket.model.RobotReportCaseExample;
import com.baodanyun.websocket.service.CacheService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * 机器人之我要报案入库操作
 */
@Service
public class ReportCaseService {
    private final Logger LOGGER = LoggerFactory.getLogger(ReportCaseService.class);
    @Autowired
    private RobotReportCaseMapper robotReportCaseMapper;

    @Autowired
    private CacheService cacheService;

    /**
     * 插入我要报案记录
     *
     * @param user
     * @param msg
     * @param state
     */
    public boolean saveReportCase(AbstractUser user, Msg msg, int state) {
        boolean flag = false;

        try {
            RobotReportCase reportCase = new RobotReportCase();
            reportCase.setContent(msg.getContent().trim());
            reportCase.setContentTime(new Date(msg.getCt()));
            reportCase.setUserName(user.getUserName());
            reportCase.setLoginUserName(user.getLoginUsername());
            reportCase.setIcon(user.getIcon());
            reportCase.setState((byte) state);
            reportCase.setOpenId(user.getOpenId() == null ? user.getOpenId() : msg.getFrom());
            reportCase.setCreateTime(new Date());

            String cacheKey = RobotConstant.ROBOT_KEYP_REFIX + msg.getFrom();
            Msg cacheMsg = (Msg) cacheService.get(cacheKey);
            if (cacheMsg != null) {
                reportCase.setSerialNumber(cacheMsg.getSerialNumber()); //批次号
            }

            flag = robotReportCaseMapper.insertSelective(reportCase) > 0; //保存
            LOGGER.info(String.format("保存报案消息: %s, 状态上传中", JSON.toJSONString(msg)));
        } catch (Exception e) {
            LOGGER.error(e.getMessage() + JSON.toJSONString(user) + "报案消息失败: " + JSON.toJSONString(msg));
        }

        return flag;
    }

    /**
     * 按批次更新此次报案成功记录
     *
     * @return
     */
    public boolean updateReportCaseSuccess(Msg msg) {
        boolean flag = false;

        try {
            String cacheKey = RobotConstant.ROBOT_KEYP_REFIX + msg.getFrom();
            Msg cacheMsg = (Msg) cacheService.get(cacheKey);
            if (cacheMsg != null) {
                RobotReportCase record = new RobotReportCase();
                record.setState((byte) ReportCaseEnum.SUCCESS.getState());

                String serialNum = cacheMsg.getSerialNumber(); //批次号
                RobotReportCaseExample example = new RobotReportCaseExample();
                example.createCriteria().andSerialNumberEqualTo(serialNum);

                flag = robotReportCaseMapper.updateByExampleSelective(record, example) > 0;
                if (flag) {
                    cacheService.remove(cacheKey);
                    LOGGER.info(String.format("当前用户消息[%s],更新批次号%s的所有记录成功", msg.getContent(), serialNum));
                }
            } else {
                LOGGER.info(cacheKey + "缓存中为空,无更新");
            }
        } catch (Exception e) {
            LOGGER.error(e.getMessage() + "\n按批次更新失败:" + JSON.toJSONString(msg));
            return false;
        }

        return flag;
    }

    /**
     * 撤消保存的记录
     *
     * @param msg
     */
    public boolean withdraw(Msg msg) {
        boolean flag = false;
        String cacheKey = RobotConstant.ROBOT_KEYP_REFIX + msg.getFrom();

        try {
            Msg cacheMsg = (Msg) cacheService.get(cacheKey);
            if (cacheMsg != null) {
                String serialNum = cacheMsg.getSerialNumber(); //批次号
                RobotReportCaseExample example = new RobotReportCaseExample();
                example.createCriteria().andSerialNumberEqualTo(serialNum)
                        .andStateEqualTo((byte) ReportCaseEnum.REPORTING.getState());

                flag = robotReportCaseMapper.deleteByExample(example) > 0; //删除
                if (flag) {
                    cacheService.remove(cacheKey);
                    LOGGER.info(String.format("当前用户消息[Y],删除批次号[%s]的所有记录成功", msg.getContent(), serialNum));
                }
            }
        } catch (Exception e) {
            LOGGER.error(e.getMessage() + "\n撤消记录:" + JSON.toJSONString(msg));
            return false;
        }

        return flag;
    }
}
