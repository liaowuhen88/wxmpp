package com.baodanyun.websocket.core.handle.plugin;

import com.google.common.eventbus.EventBus;

/**
 * Created by yutao on 2016/10/18.
 * 事件埋点管理器
 */
public class EventManager {

    private static final EventManager eventManager = new EventManager();
    private static final EventBus BUS = new EventBus();
    static {
        //注册顶级类
        BUS.register(new PluginEvent());
    }
    private EventManager(){}
    public static EventManager getInstance() {
        return eventManager;
    }
    public void post(PluginEvent pluginEvent){
        BUS.post(pluginEvent);
    }
}
