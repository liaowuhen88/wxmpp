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
 * 消息记录到库
 */
public class WriteDBHandler extends AbstractRobotHandler {
    private final Logger LOGGER = LoggerFactory.getLogger(WriteDBHandler.class);

    public WriteDBHandler() {
    }

    public WriteDBHandler(Builder builder) {
        this.nextRobotHandler = builder.nextHandler;
    }

    @Override
    public void flow(Msg message) {
        String content = message.getContent();
        if (!content.equals(RobotConstant.FINISH) && !content.equals(RobotConstant.CLOSE)) {

            UserServer userServer = SpringContextUtil.getBean("userServerImpl", UserServer.class);
            ReportCaseService reportCaseService = SpringContextUtil.getBean("reportCaseService", ReportCaseService.class);

            try {
                AbstractUser user = userServer.initUserByOpenId(message.getFrom());
                LOGGER.info("当前用户信息: " + JSON.toJSONString(user));
                //记录入库，状态为报案中
                reportCaseService.saveReportCase(user, message, ReportCaseEnum.REPORTING.getState());
            } catch (BusinessException e) {
                LOGGER.error(e.getMessage() + "\n" + JSON.toJSON(message));
            }
        } else {
            if (getNextRobotHandler() != null)
                getNextRobotHandler().flow(message);
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
