package com.baodanyun.robot.service;

import com.alibaba.fastjson.JSON;
import com.baodanyun.robot.common.RobotConstant;
import com.baodanyun.robot.dto.RobotDto;
import com.baodanyun.robot.dto.RobotImages;
import com.baodanyun.robot.dto.RobotSearchDto;
import com.baodanyun.websocket.bean.msg.Msg;
import com.baodanyun.websocket.bean.user.AbstractUser;
import com.baodanyun.websocket.dao.RobotReportCaseMapper;
import com.baodanyun.websocket.enums.ReportCaseEnum;
import com.baodanyun.websocket.model.RobotReportCase;
import com.baodanyun.websocket.model.RobotReportCaseExample;
import com.baodanyun.websocket.service.CacheService;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.Predicate;
import org.apache.commons.collections4.Transformer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
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
            //emoji表情过滤ue开头的
            reportCase.setContent(msg.getContent().replaceAll("[\\ue000-\\uefff]", ""));
            reportCase.setContentType(msg.getContentType());
            reportCase.setContentTime(new Date(msg.getCt()));
            reportCase.setUid(user.getUid());
            reportCase.setUserName(user.getUserName());
            reportCase.setLoginUserName(user.getLoginUsername());
            reportCase.setIcon(user.getIcon());
            reportCase.setNickName(user.getNickName());
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
        List<RobotReportCase> caseList = robotReportCaseMapper.findNotFinishData();
        if (!CollectionUtils.isEmpty(caseList)) {
            for (RobotReportCase reportCase : caseList) {
                this.modifyExpireData(reportCase.getOpenId(), reportCase.getSerialNumber());

                this.expireWechatTip(reportCase.getOpenId()); //超时未提交微信提示
            }
        }
    }

    /**
     * 用户微信消息超时未提交提示
     *
     * @param openId
     */
    private void expireWechatTip(String openId) {
        Msg msg = new Msg();
        msg.setOpenId(openId);
        msg.setFrom(openId);
        msg.setContent(RobotConstant.EXPIRE_TIP);
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
     * @param serialNumber 批次号
     * @return
     */
    @Transactional
    public boolean modifyExpireData(String openId, String serialNumber) {
        try {
            RobotReportCase record = new RobotReportCase();
            record.setState((byte) ReportCaseEnum.EXPIRE.getState());
            record.setUpdateTime(new Date());
            record.setRemark("超过15分钟定时任务更新状态");

            RobotReportCaseExample example = new RobotReportCaseExample();
            example.createCriteria().andOpenIdEqualTo(openId).andSerialNumberEqualTo(serialNumber);

            robotReportCaseMapper.updateByExampleSelective(record, example);
            LOGGER.info("定时任务成功更新(state=4):openId=" + openId + ";批次号=" + serialNumber);
        } catch (Exception e) {
            LOGGER.info("定时任务失败更新(state=4):openId=" + openId + ";批次号=" + serialNumber);
            LOGGER.error(e.getMessage());
            return false;
        }

        return true;
    }

    /**
     * 根据uid查询机器人数据
     *
     * @param uid 用户id
     * @return
     */
    public List<RobotDto> getReportCaseByUid(Long uid) {
        List<RobotDto> resultList = null;
        List<RobotReportCase> serialList = robotReportCaseMapper.findSerialNumberList(uid);
        if (CollectionUtils.isNotEmpty(serialList)) {
            List<RobotReportCase> dataList = this.findListByUid(uid);
            resultList = this.buildRobotList(serialList, dataList);
        }

        return resultList;
    }

    private List<RobotDto> buildRobotList(List<RobotReportCase> serialList, List<RobotReportCase> dataList) {
        List<RobotDto> resultList = new ArrayList<>();
        for (RobotReportCase robot : serialList) {
            List<RobotImages> imagesList = this.buildRobotImageList(dataList, robot.getSerialNumber());
            RobotDto robotDto = new RobotDto();
            BeanUtils.copyProperties(robot, robotDto);
            robotDto.setRobotImages(imagesList);

            resultList.add(robotDto);
        }

        return resultList;
    }

    /**
     * 查询用户上传的报案
     *
     * @param uid
     * @return
     */
    public List<RobotReportCase> findListByUid(Long uid) {
        RobotReportCaseExample example = new RobotReportCaseExample();
        example.setOrderByClause("content_time ASC"); //消息时间
        example.createCriteria()
                .andUidEqualTo(uid)
                .andContentTypeEqualTo("image")
                .andStateEqualTo((byte) ReportCaseEnum.SUCCESS.getState());

        return robotReportCaseMapper.selectByExample(example);
    }

    private List<RobotImages> buildRobotImageList(List<RobotReportCase> dataList, final String serialNumber) {
        List<RobotImages> imagesList = new ArrayList<>();

        List<RobotReportCase> list = (List<RobotReportCase>) CollectionUtils.select(dataList, new Predicate<RobotReportCase>() {
            @Override
            public boolean evaluate(RobotReportCase robotReportCase) {
                return serialNumber.equals(robotReportCase.getSerialNumber());
            }
        });

        if (CollectionUtils.isNotEmpty(list)) {
            for (RobotReportCase reportCase : list) {
                RobotImages images = new RobotImages(reportCase.getContent(), reportCase.getContentTime());
                imagesList.add(images);
            }
        }

        return imagesList;
    }

    /**
     * 根据电话号码查询
     *
     * @param phone
     * @return
     */
    public List<RobotDto> getReportCaseByPhone(Long phone) {
        List<RobotDto> resultList = null;
        List<RobotReportCase> serialList = robotReportCaseMapper.findRobotListByPhone(phone);
        if (CollectionUtils.isNotEmpty(serialList)) {
            List<RobotReportCase> dataList = this.findListByUserName(phone);
            resultList = this.buildRobotList(serialList, dataList);
        }

        return resultList;
    }

    public List<RobotReportCase> findListByUserName(Long phone) {
        RobotReportCaseExample example = new RobotReportCaseExample();
        example.setOrderByClause("content_time ASC"); //消息时间
        example.createCriteria()
                .andUserNameEqualTo(String.valueOf(phone))
                .andContentTypeEqualTo("image")
                .andStateEqualTo((byte) ReportCaseEnum.SUCCESS.getState());

        return robotReportCaseMapper.selectByExample(example);
    }


    /**
     * 所有机器人报案数据
     *
     * @param searchDto
     * @return
     */
    public List<RobotDto> findPageList(RobotSearchDto searchDto) {
        List<RobotDto> resultList = null;

        searchDto.setOffset((searchDto.getPage() - 1) * searchDto.getPage());
        List<RobotReportCase> serialList = robotReportCaseMapper.findSerialNumberPage(searchDto);
        if (CollectionUtils.isNotEmpty(serialList)) {
            List<RobotReportCase> dataList = this.findInSerialNumList(serialList);
            resultList = this.buildRobotList(serialList, dataList);
        }
        return resultList;
    }

    private List<RobotReportCase> findInSerialNumList(List<RobotReportCase> serialList) {
        List<String> list = (List<String>) CollectionUtils.collect(serialList, new Transformer() {
            @Override
            public String transform(Object obj) {
                RobotReportCase reportCase = (RobotReportCase) obj;
                return reportCase.getSerialNumber();
            }
        });
        return this.findInSerialNum(list);
    }

    public List<RobotReportCase> findInSerialNum(List<String> list) {
        RobotReportCaseExample example = new RobotReportCaseExample();
        example.setOrderByClause("content_time DESC"); //消息时间
        example.createCriteria()
                .andSerialNumberIn(list)
                .andContentTypeEqualTo("image")
                .andStateEqualTo((byte) ReportCaseEnum.SUCCESS.getState());

        return robotReportCaseMapper.selectByExample(example);
    }

    /**
     * 批次号的总条数
     *
     * @return
     */
    public long getTotalCount(RobotSearchDto searchDto) {
        return robotReportCaseMapper.getSerialNumberTotalCount(searchDto);
    }
}
