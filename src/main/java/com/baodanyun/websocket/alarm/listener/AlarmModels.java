package com.baodanyun.websocket.alarm.listener;

import com.baodanyun.websocket.event.AlarmEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author hubo
 * @since 2017-06-30 15:42
 **/
public class AlarmModels {
    private final Logger LOGGER = LoggerFactory.getLogger(AlarmModels.class);

    private Queue<AlarmListener> listeners = new ConcurrentLinkedDeque<>();

    private ExecutorService executorService = Executors.newFixedThreadPool(2);

    public synchronized void addListener(AlarmListener listener) {
        listeners.add(listener);
    }

    /**
     * 执行告警业务
     */
    public synchronized void executeAlarm(final AlarmEvent alarmInfo) {
        for (int i = 0, len = listeners.size(); i < len ; i++) {
            executorService.submit(new Runnable() {
                @Override
                public void run() {
                    listeners.poll().alarm(alarmInfo);
                }
            });

        }
    }
}