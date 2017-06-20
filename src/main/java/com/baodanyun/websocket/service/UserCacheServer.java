package com.baodanyun.websocket.service;

import com.baodanyun.websocket.bean.user.AbstractUser;
import com.baodanyun.websocket.exception.BusinessException;
import org.springframework.stereotype.Service;

/**
 * Created by liaowuhen on 2016/11/11.
 */
@Service
public interface UserCacheServer {

    /**
     * 维护发送消息目的地
     */
    boolean addVisitorCustomerOpenId(String openId, String to) throws BusinessException;

    String getCustomerIdByVisitorOpenId(String openId) throws BusinessException;

    void addCustomer(AbstractUser abstractUser) throws BusinessException;

    AbstractUser getCustomer(String id) throws BusinessException;

/*    AbstractUser getVisitorByUidOrOpenID(String to);*/

   /*

    AbstractUser saveVisitorByUidOrOpenID(String to, AbstractUser user);
    */
    /**
     * 获取发送地址
     *//*



    AbstractUser getCustomerByVisitorOpenId(String openId) throws BusinessException;

    Map<String, AbstractUser> getVisitors();

    Map<String, AbstractUser> getCustomers();

    AbstractUser getUser(String userId);


    Customer getUserCustomer(String userId);

    Visitor getUserVisitor(String userId);

    boolean add(String key, String id, AbstractUser visitorUser);

    boolean addCid(String key, String id, AbstractUser visitorUser);

    boolean add(String key, AbstractUser visitorUser);

    boolean addOpenId(String key, AbstractUser visitorUser);

    Set<AbstractUser> get(String key, String cid);

    Object get(String key);

    void delete(String key, String cid, AbstractUser visitorUser);*/


}
