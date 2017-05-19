package com.baodanyun.websocket.service;

import com.baodanyun.websocket.dao.TransferlogMapper;
import com.baodanyun.websocket.model.Transferlog;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by liaowuhen on 2016/11/11.
 */
@Service
public class TransferlogServer {
    protected final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private TransferlogMapper transferlogMapper;

    public int insert(Transferlog record) {
        return transferlogMapper.insert(record);
    }
}
