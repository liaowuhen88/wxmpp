package com.baodanyun.websocket.service.impl;

import com.baodanyun.websocket.dao.OfuserMapper;
import com.baodanyun.websocket.model.Ofproperty;
import com.baodanyun.websocket.model.Ofuser;
import com.baodanyun.websocket.service.OfpropertyService;
import com.baodanyun.websocket.service.OfuserService;
import com.baodanyun.websocket.util.Blowfish;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by liaowuhen on 2017/6/26.
 */
@Service
public class OfuserServiceImpl implements OfuserService {
    protected static Logger logger = LoggerFactory.getLogger(OfuserServiceImpl.class);
    @Autowired
    protected OfuserMapper ofuserMapper;

    @Autowired
    private OfpropertyService ofpropertyService;

    @Override
    public boolean checkOfUser(String userName, String password) {
        if (StringUtils.isEmpty(userName) || StringUtils.isEmpty(password)) {
            return false;
        }
        try {
            Ofuser ofuser = ofuserMapper.selectByPrimaryKey(userName);
            Ofproperty passwordKey = ofpropertyService.selectByPrimaryKey("passwordKey");
            String key = passwordKey.getPropvalue();
            Blowfish bf = new Blowfish(key);
            String encryptedString = bf.decrypt(ofuser.getEncryptedpassword());
            logger.info("password: {} _________ encryptedString: {}", password, encryptedString);
            if (password.equals(encryptedString)) {
                return true;
            }

        } catch (Exception e) {
            logger.info("error", e);
        }


        return false;
    }
}
