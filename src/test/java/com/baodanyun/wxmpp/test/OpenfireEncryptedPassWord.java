package com.baodanyun.wxmpp.test;

import com.baodanyun.websocket.dao.OfpropertyMapper;
import com.baodanyun.websocket.exception.BusinessException;
import com.baodanyun.websocket.model.Ofproperty;
import com.baodanyun.websocket.service.OfpropertyService;
import com.baodanyun.websocket.util.Blowfish;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by liaowuhen on 2017/6/9.
 */
public class OpenfireEncryptedPassWord extends BaseTest {

    @Autowired
    private OfpropertyService ofpropertyService;

    @Test
    public void subString() throws BusinessException {

        Ofproperty passwordKey = ofpropertyService.selectByPrimaryKey("passwordKey");

        String key = passwordKey.getPropvalue();
        System.out.println(key);//will


        Blowfish bf = new Blowfish("p6N2tvv9kvBeDT2");
        String encryptedString = bf.decrypt("34dcc87e22b82507a69cd7078144cb8a26afc4c6df6d09d9");
        System.out.println(encryptedString);//will

        encryptedString = bf.decrypt("G+e5PFjEZKFGDfwT3W1rNUn4bm8=");
        System.out.println(encryptedString);//will

        encryptedString = bf.decrypt("MCCKfV7i2WW0TjGZhORl5C2eXE0=");
        System.out.println(encryptedString);//will

        encryptedString = bf.decrypt("H+1hRs3Wb8YGDlzraSMbBpdQWga2m8l/");
        System.out.println(encryptedString);//will


    }
}
