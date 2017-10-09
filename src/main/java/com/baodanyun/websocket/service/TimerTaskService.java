package com.baodanyun.websocket.service;

import com.baodanyun.admin.service.CustomerService;
import com.baodanyun.robot.service.ReportCaseService;
import com.baodanyun.websocket.alarm.AlarmBoxer;
import com.baodanyun.websocket.node.ChatNodeManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

/**
 * Created by liaowuhen on 2016/11/15.
 */
@Service
public class TimerTaskService {
    protected final Logger logger = LoggerFactory.getLogger(this.getClass());

    private boolean dealMsgSendControl = true;
    @Autowired
    private LastVisitorSendMessageService las;

    @Autowired
    private XmppServer xmppServer;


    @Autowired
    private WebSocketService webSocketService;

    @Autowired
    private MsgConsumer msgConsumer;
    @Autowired
    private ReportCaseService reportCaseService;
    @Autowired
    private CustomerService customerService;


    /**
     * 消息超时处理
     */
    @Scheduled(cron = "0 0/1 * * * ?")
    //需要注意@Scheduled这个注解，它可配置多个属性：cron\fixedDelay\fixedRate
    public void test() {
        try {
            //las.search();
        } catch (Exception e) {
            logger.error("error", "操作失败", e);
        }
    }

    /**
     * xmpp 空闲超时处理
     */
    @Scheduled(cron = "0 0/10 * * * ?")
    //需要注意@Scheduled这个注解，它可配置多个属性：cron\fixedDelay\fixedRate
    public void chatNode() {
        try {
            ChatNodeManager.freeClosed();
        } catch (Exception e) {
            logger.error("error", "操作失败", e);
        }
    }


    /**
     * 启动消息处理线程
     */
    @Scheduled(fixedRate = 1000 * 60 * 60 * 24 * 365)
    public void dealMsgSendControl() {
        if (dealMsgSendControl) {
            Thread th  = new Thread(msgConsumer);

            th.start();

            dealMsgSendControl = false;
        }

    }

    /**
     * 告警任务
     */
    @Scheduled(cron = "0 0/2 * * * ?")
    public void alarmJob() {
        try {
            logger.info("开始计时告警");
            AlarmBoxer.getInstance().doAlarmJob();
        } catch (Exception e) {
            logger.error("error", "告警任务操作失败", e);
        }
    }

    /**
     * 定时清理超过15分钟的[我要报案]机器人流程的用户没有完成上传的数据
     */
    @Scheduled(cron = "0 0/5 * * * ?")
    public void clearExpireDataFromRobotCase() {
        try {
            reportCaseService.clearExpireData();
        } catch (Exception e) {
            logger.error("error", "定时清理超过15分钟的[我要报案]机器人流程失败", e);
        }
    }

    /**
     * 补偿修复更新上传批次的状态
     */
    @Scheduled(cron = "0 0/5 * * * ?")
    public void fixSerialState() {
        try {
            customerService.fixSerialState();
        } catch (Exception e) {
            logger.error("error", "补偿更新上传批次的状态", e);
        }
    }

}