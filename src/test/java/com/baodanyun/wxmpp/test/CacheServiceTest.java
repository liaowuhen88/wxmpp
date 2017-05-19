package com.baodanyun.wxmpp.test;

//import com.baodanyun.websocket.service.order.QDOrderHelper;

import com.baodanyun.websocket.bean.user.AbstractUser;
import com.baodanyun.websocket.bean.userInterface.RequestBean;
import com.baodanyun.websocket.exception.BusinessException;
import com.baodanyun.websocket.service.CacheService;
import com.baodanyun.websocket.service.XmppServer;
import com.baodanyun.websocket.util.CommonConfig;
import com.baodanyun.websocket.util.KdtApiClient;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;
import java.util.Set;

/**
 * Created by 峥桂 on 2016/11/15.
 */
public class CacheServiceTest extends BaseTest{

    @Autowired
    private CacheService cacheService;
    @Test
    public  void desc() {
        boolean flag = cacheService.setOneDay(CommonConfig.USER_OPENID_KEY + "111", "111");

        System.out.print(flag);

    }

    @Test
    public  void delete() {
        boolean flag =  cacheService.remove(CommonConfig.USER_ONLINE );
       // boolean flag2 =  cacheService.remove(CommonConfig.USER_HISTORY );

        System.out.print(flag);

    }


    @Test
    public void getUidByOpenId() throws BusinessException {
        String openId = "oAH_qslG4CziSU43s2NtM5f-b61U";
        KdtApiClient kdtApiClient = new KdtApiClient();
        RequestBean requestBean = new RequestBean();
        requestBean.setOpenId(openId);

        String uid = kdtApiClient.postJson(KdtApiClient.APiMethods.getUidByOpenId.getValue(), requestBean);
        System.out.println("uid-------:" + uid);


        System.out.println(cacheService.setOneDay(CommonConfig.USER_OPENID_KEY + openId,uid));
        String uid_ = (String) cacheService.get(CommonConfig.USER_OPENID_KEY + openId);
        System.out.println("======>>>uid_-------:" + uid_);

    }

    @Test
    public void getCompany() throws BusinessException {
        KdtApiClient kdtApiClient = new KdtApiClient();

       /// String desc = kdtApiClient.getJson(KdtApiClient.APiMethods.getUserCompany.getValue()+"/"+301);

    }

    @Test
    public void getUserHistory() throws BusinessException {
        //String cid = "maqiumeng@126xmpp";
        String cid = "zwv11132@126xmpp";

        Object map = cacheService.get(CommonConfig.USER_ONLINE);
        if(null != map){
            HashMap<String,Set<AbstractUser>> ma = (HashMap<String,Set<AbstractUser>>)map;
            Set<AbstractUser> set = ma.get(cid);
            if(null != set){
               System.out.print(set.size());
            }else {
                System.out.println("set is null");
            }
        }
    }


}
