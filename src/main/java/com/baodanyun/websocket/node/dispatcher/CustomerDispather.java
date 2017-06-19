package com.baodanyun.websocket.node.dispatcher;

import com.baodanyun.websocket.bean.user.AbstractUser;

/**
 * Created by liaowuhen on 2017/5/24.
 */
public interface CustomerDispather {
    /**
     * 接入访客
     *
     * @param abstractUser
     * @return
     * @throws InterruptedException
     */
    boolean joinQueue(AbstractUser abstractUser) throws InterruptedException;

    /**
     * 移除访客
     * @param abstractUser
     * @return
     * @throws InterruptedException
     */
    boolean uninstall(AbstractUser abstractUser) throws InterruptedException;

}
