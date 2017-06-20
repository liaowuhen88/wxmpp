package com.baodanyun.websocket.service.impl;

import com.baodanyun.websocket.dao.FromToAdaptMapper;
import com.baodanyun.websocket.service.FromToAdaptService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by liaowuhen on 2017/6/20.
 */

@Service
public class FromToAdaptServiceImpl implements FromToAdaptService {

    @Autowired
    private FromToAdaptMapper fromToAdaptMapper;

}
