package com.baodanyun.websocket.node.dispatcher;

import com.baodanyun.websocket.exception.BusinessException;

/**
 * Created by liaowuhen on 2017/5/24.
 */
public interface VisitorDispather {

    boolean joinQueue() throws InterruptedException, BusinessException;

    boolean uninstall() throws InterruptedException;

}
