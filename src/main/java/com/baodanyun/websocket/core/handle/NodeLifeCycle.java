package com.baodanyun.websocket.core.handle;

/**
 * 节点周期
 * 每个节点 都需要有自己的生命周期 便于管理各个节点
 */
public interface NodeLifeCycle {
    boolean init();

    boolean run();

    void offline();
}
