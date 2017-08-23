package com.baodanyun.robot.handler;

import com.alibaba.fastjson.JSON;
import com.baodanyun.robot.common.RobotConstant;
import com.baodanyun.robot.service.ReportCaseService;
import com.baodanyun.websocket.bean.msg.Msg;
import com.baodanyun.websocket.bean.user.AbstractUser;
import com.baodanyun.websocket.enums.ReportCaseEnum;
import com.baodanyun.websocket.exception.BusinessException;
import com.baodanyun.websocket.service.CacheService;
import com.baodanyun.websocket.service.impl.MemCacheServiceImpl;
import com.baodanyun.websocket.util.SpringContextUtil;

/**
 * 完成责任链
 * 用户输入[Y]关键字时保存更新上传的数据记录成功
 * @author hubo
 * @since 2017-08-18
 */
public class FinishHandler extends AbstractRobotHandler {

    public FinishHandler() {
    }

    public FinishHandler(Builder builder) {
        this.nextRobotHandler = builder.nextHandler;
    }

    @Override
    public void flow(Msg message, AbstractUser user) {
        String content = message.getContent();
        if (content.equals(RobotConstant.FINISH)) {//输入Y成功
            String openId = message.getFrom();
            Msg msg = new Msg();
            msg.setOpenId(openId);
            msg.setFrom(openId);
            msg.setType("text");
            msg.setContentType("text");
            msg.setContent(RobotConstant.SUCCESS_TIP);
            super.sendWechatTip(msg); //微信提示完成

            ReportCaseService reportCaseService = SpringContextUtil.getBean("reportCaseService", ReportCaseService.class);
            reportCaseService.saveReportCase(user, message, ReportCaseEnum.REPORTING.getState());
            reportCaseService.updateReportCaseSuccess(msg); //更新成功，完成上传
        } else {
            if (getNextRobotHandler() != null)
                getNextRobotHandler().flow(message, user);
        }
    }

    public static class Builder {
        private AbstractRobotHandler nextHandler;

        public Builder nextHandler(AbstractRobotHandler nextHandler) {
            this.nextHandler = nextHandler;
            return this;
        }

        public FinishHandler build() {
            return new FinishHandler(this);
        }
    }

}
