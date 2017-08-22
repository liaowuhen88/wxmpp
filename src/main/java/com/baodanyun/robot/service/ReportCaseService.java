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
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.Date;
import java.util.List;

/**
 * 机器人之我要报案操作表service
 *
 * @author hubo
 * @since 2017-08-21
 */
@Service
public class ReportCaseService {
    private final Logger LOGGER = LoggerFactory.getLogger(ReportCaseService.class);
    @Autowired
    private RobotReportCaseMapper robotReportCaseMapper;

    @Autowired
    private CacheService cacheService;
    @Autowired
    private RobotCheckerService robotCheckerService;

    /**
     * 插入我要报案记录
     *
     * @param user
     * @param msg
     * @param state 状态:1上传中(用户未提交)2完成上传
     */
    @Transactional
    public boolean saveReportCase(AbstractUser user, Msg msg, int state) {
        LOGGER.info("当前用户信息: " + JSON.toJSONString(user));

        boolean flag = false;
        try {
            String serialNumber;//批次号
            String cacheKey = RobotConstant.ROBOT_KEYP_REFIX + msg.getFrom();
            Msg cacheMsg = (Msg) cacheService.get(cacheKey);
            if (cacheMsg != null) {
                serialNumber = cacheMsg.getSerialNumber();
            } else {
                return false;
            }

            RobotReportCase reportCase = new RobotReportCase();
            //emoji表情中有\x09f的字符直接过滤x
            reportCase.setContent(msg.getContent().replaceAll("x", "").trim());
            reportCase.setContentType(msg.getContentType());
            reportCase.setContentTime(new Date(msg.getCt()));
            reportCase.setUid(user.getUid());
            reportCase.setUserName(user.getUserName());
            reportCase.setLoginUserName(user.getLoginUsername());
            reportCase.setIcon(user.getIcon());
            reportCase.setRemark(user.getNickName());
            reportCase.setState((byte) state);
            reportCase.setSerialNumber(serialNumber);
            reportCase.setOpenId(user.getOpenId() != null ? user.getOpenId() : msg.getFrom());
            reportCase.setCreateTime(new Date());

            flag = robotReportCaseMapper.insertSelective(reportCase) > 0; //保存
            LOGGER.info("保存报案消息成功:" + JSON.toJSONString(reportCase));
        } catch (Exception e) {
            LOGGER.error(e.getMessage() + "\n" + JSON.toJSONString(user) + "报案消息失败: " + JSON.toJSONString(msg));
        }

        return flag;
    }

    /**
     * 按批次更新此次报案成功记录
     *
     * @return
     */
    @Transactional
    public boolean updateReportCaseSuccess(Msg msg) {
        boolean flag = false;

        try {
            String cacheKey = RobotConstant.ROBOT_KEYP_REFIX + msg.getFrom();
            Msg cacheMsg = (Msg) cacheService.get(cacheKey);
            if (cacheMsg != null) {
                RobotReportCase record = new RobotReportCase();
                record.setState((byte) ReportCaseEnum.SUCCESS.getState());
                record.setUpdateTime(new Date());

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
                flag = this.updateStateBySerailNumber(serialNum, ReportCaseEnum.WITHDRAW.getState()); //撤消

                if (flag) {
                    cacheService.remove(cacheKey);
                    LOGGER.info(String.format("当前用户消息[%s],更新批次号[%s]的所有状态为3(撤消)", msg.getContent(), serialNum));
                }
            }
        } catch (Exception e) {
            LOGGER.error(e.getMessage() + "\n撤消记录:" + JSON.toJSONString(msg));
            return false;
        }

        return flag;
    }

    /**
     * 定时清理超过15分钟的[我要报案]机器人流程的用户没有完成上传的数据
     */
    public void clearExpireData() {
        List<String> openIdList = robotReportCaseMapper.findNotFinishData();
        if (!CollectionUtils.isEmpty(openIdList)) {
            for (String openId : openIdList) {
                this.modifyExpireData(openId, ReportCaseEnum.EXPIRE.getState());

                this.expireWechatTip(openId);
            }
        }
    }

    /**
     * 用户微信消息超时未提交提示
     * @param openId
     */
    private void expireWechatTip(String openId) {
        Msg msg = new Msg();
        msg.setOpenId(openId);
        msg.setFrom(openId);
        robotCheckerService.sendWechatTip(msg);
    }

    /**
     * 根据批次号更新状态
     *
     * @param serialNumber
     * @param state
     * @return
     */
    @Transactional
    public boolean updateStateBySerailNumber(String serialNumber, int state) {
        RobotReportCase record = new RobotReportCase();
        record.setState((byte) state);
        record.setUpdateTime(new Date());

        RobotReportCaseExample example = new RobotReportCaseExample();
        example.createCriteria().andSerialNumberEqualTo(serialNumber);

        return robotReportCaseMapper.updateByExampleSelective(record, example) > 0;
    }

    /**
     * 清理超时的数据
     *
     * @param openId
     * @param state
     * @return
     */
    @Transactional
    public boolean modifyExpireData(String openId, int state) {
        try {
            RobotReportCase record = new RobotReportCase();
            record.setState((byte) state);
            record.setUpdateTime(new Date());

            RobotReportCaseExample example = new RobotReportCaseExample();
            example.createCriteria().andOpenIdEqualTo(openId);

            return robotReportCaseMapper.updateByExampleSelective(record, example) > 0;
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
            return false;
        }
    }

}
