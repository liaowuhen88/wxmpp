package com.baodanyun.wxmpp.test;

import com.baodanyun.websocket.exception.BusinessException;
import com.baodanyun.websocket.model.Ofproperty;
import com.baodanyun.websocket.service.OfpropertyService;
import com.baodanyun.websocket.service.OfuserService;
import com.baodanyun.websocket.util.Blowfish;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by liaowuhen on 2017/6/9.
 */
public class OpenfireEncryptedPassWord extends BaseTest {

    @Autowired
    private OfpropertyService ofpropertyService;

    @Autowired
    private OfuserService ofuserService;


    @Test
    public void subString() throws BusinessException {

        Ofproperty passwordKey = ofpropertyService.selectByPrimaryKey("passwordKey");

        String key = passwordKey.getPropvalue();
        System.out.println(key);//will


        // Blowfish bf = new Blowfish("p6N2tvv9kvBeDT2");
        Blowfish bf = new Blowfish(key);
        String encryptedString = bf.decrypt("9fa897614b75122bbc4026a09f0563c8d66b9b9ec803299d");
        System.out.println(encryptedString);//will

        encryptedString = bf.decrypt("G+e5PFjEZKFGDfwT3W1rNUn4bm8=");
        System.out.println(encryptedString);//will

        encryptedString = bf.decrypt("MCCKfV7i2WW0TjGZhORl5C2eXE0=");
        System.out.println(encryptedString);//will

        encryptedString = bf.decrypt("H+1hRs3Wb8YGDlzraSMbBpdQWga2m8l/");
        System.out.println(encryptedString);//will


    }

    @Test
    public void check() throws BusinessException {
        ofuserService.checkOfUser("zwc", "111111");

    }

}
