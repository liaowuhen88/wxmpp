package com.baodanyun.websocket.listener.impl;

import com.baodanyun.websocket.listener.EventBusListener;
import com.baodanyun.websocket.util.EventBusUtils;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by liaowuhen on 2017/5/15.
 */

@Service
public abstract class AbstarctEventBusListener<T> implements EventBusListener<T> {

    protected ExecutorService executorService = Executors.newFixedThreadPool(5);

    @Override
    @PostConstruct
    public void init() {
        EventBusUtils.evnetBus.register(this);
    }
}
