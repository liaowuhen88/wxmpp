package com.baodanyun.websocket.core.handle.plugin.withoutNode;

import com.baodanyun.websocket.core.handle.plugin.PluginEvent;

public class LoginEventManager extends PluginEvent {
    @Override
    public void doHandel() {
        System.out.println("loginTest");
    }
}