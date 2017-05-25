package com.baodanyun.websocket.service;

import com.baodanyun.websocket.bean.userInterface.Company;
import com.baodanyun.websocket.bean.userInterface.PersonalDetail;
import com.baodanyun.websocket.bean.userInterface.claims.ClaimsInfo;
import com.baodanyun.websocket.bean.userInterface.order.OrderInfo;
import com.baodanyun.websocket.bean.userInterface.policy.EnterpriseContractByUidBean;
import com.baodanyun.websocket.bean.userInterface.user.PersonalInfo;
import com.baodanyun.websocket.bean.userInterface.user.WeiXinUser;
import com.baodanyun.websocket.exception.BusinessException;

import java.util.List;
import java.util.Map;

/**
 * Created by yutao on 2016/7/12.
 */

/**
 * 获取用户详细信息接口（第三方接口）
 */
public interface PersonalService {

    /**
     * 根据openid 获取个人信息
     *
     * @param openId
     * @return
     */
    List<PersonalInfo> getPersonalInfos(String openId) throws BusinessException;

    List<PersonalInfo> getPersonalInfos(Long uid);
    /**
     * 根据openid 只获取个人信息
     *
     * @param openId
     * @return
     */
    PersonalInfo getPersonalInfo(String openId) throws BusinessException;

    List<WeiXinUser> getWeiXinUser(String uid, String userName, String phone, String nickName);


    PersonalInfo getPersonalInfo(Long uids);


    /**
     * 根据openid PersonalUserAccount
     *
     * @param openId
     * @return
     */
    Map getPersonalUserAccount(String openId) throws Exception;


    Map getPersonalUserAccount(Long uids) throws Exception;




    /**
     * 根据openid 获取体检订单
     *
     * @param openId
     * @return
     */
    List<OrderInfo> getOrderInfo(String openId) throws BusinessException;

    List<OrderInfo> getOrderInfo(Long uid);

    /**
     * 根据openid 获取理赔
     *
     * @param openId
     * @return
     */
    List<ClaimsInfo> getClaimsInfo(String openId) throws BusinessException;

    List<ClaimsInfo> getClaimsInfo(Long uid);

    /**
     * 根据openid 获取理赔
     *
     * @param openId
     * @return
     */
    List<EnterpriseContractByUidBean> getContractInfo(String openId) throws BusinessException;//合同订单

    List<EnterpriseContractByUidBean> getContractInfo(Long uid);//合同订单

    PersonalDetail getPersonalDetail(String openId) throws BusinessException;

    PersonalDetail getPersonalDetail(Long uid);

    List<Company> getCompany(Long uid) throws Exception;

    Long getUidByOpenId(String openId) throws BusinessException;
}
