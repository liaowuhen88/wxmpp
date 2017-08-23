package com.baodanyun.robot.handler;

import com.alibaba.fastjson.JSON;
import com.baodanyun.robot.common.RobotConstant;
import com.baodanyun.robot.service.ReportCaseService;
import com.baodanyun.websocket.bean.msg.Msg;
import com.baodanyun.websocket.bean.user.AbstractUser;
import com.baodanyun.websocket.enums.ReportCaseEnum;
import com.baodanyun.websocket.exception.BusinessException;
import com.baodanyun.websocket.service.CacheService;
import com.baodanyun.websocket.service.UserServer;
import com.baodanyun.websocket.service.impl.MemCacheServiceImpl;
import com.baodanyun.websocket.util.SpringContextUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 数据入库责任链
 * 记录报案消息入库
 */
public class WriteDBHandler extends AbstractRobotHandler {
    private final Logger LOGGER = LoggerFactory.getLogger(WriteDBHandler.class);

    public WriteDBHandler() {
    }

    public WriteDBHandler(Builder builder) {
        this.nextRobotHandler = builder.nextHandler;
    }

    @Override
    public void flow(Msg message, AbstractUser user) {
        String content = message.getContent();
        if (!content.equals(RobotConstant.FINISH) && !content.equals(RobotConstant.CLOSE)) {
            //记录入库，状态为报案中
            ReportCaseService reportCaseService = SpringContextUtil.getBean("reportCaseService", ReportCaseService.class);
            reportCaseService.saveReportCase(user, message, ReportCaseEnum.REPORTING.getState());
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

        public WriteDBHandler build() {
            return new WriteDBHandler(this);
        }
    }

}
