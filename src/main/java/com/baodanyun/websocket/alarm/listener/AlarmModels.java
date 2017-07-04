package com.baodanyun.websocket.alarm.listener;

import com.baodanyun.websocket.event.AlarmEvent;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author hubo
 * @since 2017-06-30 15:42
 **/
public class AlarmModels {

    private static Queue<AlarmListener> listeners = new ConcurrentLinkedDeque<>();

    private ExecutorService executorService = Executors.newFixedThreadPool(4);

    public void addListener(AlarmListener listener) {
        listeners.add(listener);
    }

    /**
     * 执行告警业务
     */
    public void executeAlarm(final AlarmEvent alarmInfo) {
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