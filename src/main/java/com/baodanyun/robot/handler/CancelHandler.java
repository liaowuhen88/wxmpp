package com.baodanyun.robot.handler;

import com.baodanyun.robot.common.RobotConstant;
import com.baodanyun.robot.service.ReportCaseService;
import com.baodanyun.websocket.bean.msg.Msg;
import com.baodanyun.websocket.bean.user.AbstractUser;
import com.baodanyun.websocket.enums.ReportCaseEnum;
import com.baodanyun.websocket.service.CacheService;
import com.baodanyun.websocket.service.impl.MemCacheServiceImpl;
import com.baodanyun.websocket.util.SpringContextUtil;

/**
 * 取消责任链
 * 用户输入[关闭]关键字时撤消上传的数据记录
 */
public class CancelHandler extends AbstractRobotHandler {

    public CancelHandler() {
    }

    public CancelHandler(Builder builder) {
        this.nextRobotHandler = builder.nextHandler;
    }

    @Override
    public void flow(Msg message, AbstractUser user) {
        if (message.getContent().equals(RobotConstant.CLOSE)) {//关闭
            ReportCaseService reportCaseService = SpringContextUtil.getBean("reportCaseService", ReportCaseService.class);

            reportCaseService.saveReportCase(user, message, ReportCaseEnum.REPORTING.getState());
            reportCaseService.withdraw(message); //撤消
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

        public CancelHandler build() {
            return new CancelHandler(this);
        }
    }

}
