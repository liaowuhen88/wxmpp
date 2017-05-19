package com.baodanyun.websocket.service;

import com.baodanyun.websocket.bean.LogUserEvents;
import org.springframework.stereotype.Service;

/**
 * Created by liaowuhen on 2017/3/3.
 */
@Service
public interface SendToEventCenterService {
    void sendToEventCenter(LogUserEvents le);
}
