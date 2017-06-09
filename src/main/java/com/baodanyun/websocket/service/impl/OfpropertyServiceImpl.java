package com.baodanyun.websocket.service.impl;

import com.baodanyun.websocket.dao.OfpropertyMapper;
import com.baodanyun.websocket.exception.BusinessException;
import com.baodanyun.websocket.model.Ofproperty;
import com.baodanyun.websocket.service.OfpropertyService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by liaowuhen on 2017/6/9.
 */

@Service
public class OfpropertyServiceImpl implements OfpropertyService {
    @Autowired
    private OfpropertyMapper ofpropertyMapper;

    @Override
    public Ofproperty selectByPrimaryKey(String name) throws BusinessException {
        if (StringUtils.isEmpty(name)) {
            throw new BusinessException("name can not be null");
        } else {
            return ofpropertyMapper.selectByPrimaryKey(name);
        }

    }
}
