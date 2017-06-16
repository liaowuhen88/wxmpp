package com.baodanyun.websocket.core.handle;

import com.baodanyun.websocket.core.handle.plugin.EventManager;
import com.baodanyun.websocket.core.handle.plugin.withoutNode.InitEventManager;
import com.baodanyun.websocket.core.handle.plugin.withoutNode.LoginEventManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by yutao on 2016/9/7.
 * 节点的生命周期
 * 用来描述节点当前运行的状态
 */
public abstract class LifeCycle implements NodeLifeCycle {

    protected static Logger logger = LoggerFactory.getLogger(LifeCycle.class);

    //埋点管理器
    protected EventManager eventManager = EventManager.getInstance();

    //以下是状态描述
    @Override
    public boolean init() {

        logger.debug("init Node done");
        //初始化埋点
        InitEventManager initEventManager = new InitEventManager();
        eventManager.post(initEventManager);
        return true;
    }

    @Override
    public boolean run() {
        logger.debug("run Node done");
        //登录埋点
        LoginEventManager loginEventManager = new LoginEventManager();
        eventManager.post(loginEventManager);
        return true;
    }

    @Override
    public void offline() {
        logger.debug("run Node done");
    }

    public enum CycleStatus {
        init, run, offline
    }

}
