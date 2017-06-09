package com.baodanyun.websocket.service;

import com.baodanyun.websocket.exception.BusinessException;
import com.baodanyun.websocket.model.Ofproperty;
import org.springframework.stereotype.Service;

/**
 * Created by liaowuhen on 2017/6/9.
 */
@Service
public interface OfpropertyService {
    Ofproperty selectByPrimaryKey(String name) throws BusinessException;
}
