package com.baodanyun.websocket.core.handle.plugin;

import com.google.common.eventbus.Subscribe;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by yutao on 2016/10/18.
 * 不能是抽象类  抽象类不能被托管
 */
public class PluginEvent implements IEvent {

    protected static Logger logger = LoggerFactory.getLogger(PluginEvent.class);

    @Subscribe
    public void handel(IEvent event){
        event.doHandel();
    }

    @Override
    public void doHandel() {

    }
}
