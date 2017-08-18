package com.baodanyun.robot.handler;

import com.baodanyun.robot.common.RobotConstant;
import com.baodanyun.websocket.bean.Response;
import com.baodanyun.websocket.bean.msg.Msg;
import com.baodanyun.websocket.service.CacheService;
import com.baodanyun.websocket.service.impl.MemCacheServiceImpl;
import com.baodanyun.websocket.util.SpringContextUtil;

/**
 * 关键字handler
 * <ol>
 * <li>第一句是:我要报案</li>
 * <li>后面的消息，语音，图片都执行流程</li>
 * <li>30分钟内的重复关键字忽略</li>
 * <li>超过30分钟或者用户输入"Y"结束流程</li>
 * </ol>
 *
 * @author hubo
 * @since 2017-08-18
 */
public class KeywordsHandler extends AbstractRobotHandler {

    public KeywordsHandler() {
    }

    public KeywordsHandler(Builder builder) {
        this.nextRobotHandler = builder.nextHandler;
    }

    @Override
    public void flow(Msg message) {
        String content = message.getContent();
        if (content.equals(RobotConstant.FINISH)) {//输入Y成功
            Msg msg = new Msg();
            msg.setOpenId(message.getOpenId());
            msg.setFrom(message.getOpenId());
            msg.setType("text");
            msg.setContentType("text");
            msg.setContent(RobotConstant.SUCCESS_TIP);
            super.sendWechatTip(msg); //微信提示

            CacheService cacheService = SpringContextUtil.getBean("cacheService", MemCacheServiceImpl.class);
            cacheService.remove(RobotConstant.ROBOT_KEYP_REFIX);
        } else if (content.equals(RobotConstant.CLOSE)) {//关闭重新申请,撤销

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

        public KeywordsHandler build() {
            return new KeywordsHandler(this);
        }
    }

}
