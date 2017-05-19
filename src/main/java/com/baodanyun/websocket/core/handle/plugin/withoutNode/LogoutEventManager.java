package com.baodanyun.websocket.core.handle.plugin.withoutNode;

import com.baodanyun.websocket.core.handle.plugin.PluginEvent;

public class LogoutEventManager extends PluginEvent {
    @Override
    public void doHandel() {
        logger.info("user has logout");
    }
}