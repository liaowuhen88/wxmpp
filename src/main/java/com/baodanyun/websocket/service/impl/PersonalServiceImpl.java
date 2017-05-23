package com.baodanyun.websocket.service.impl;

import com.baodanyun.websocket.bean.hr.HrUser;
import com.baodanyun.websocket.bean.userInterface.Company;
import com.baodanyun.websocket.bean.userInterface.PersonalDetail;
import com.baodanyun.websocket.bean.userInterface.RequestBean;
import com.baodanyun.websocket.bean.userInterface.claims.ClaimsInfo;
import com.baodanyun.websocket.bean.userInterface.order.OrderInfo;
import com.baodanyun.websocket.bean.userInterface.policy.EnterpriseContractByUidBean;
import com.baodanyun.websocket.bean.userInterface.user.PersonalInfo;
import com.baodanyun.websocket.bean.userInterface.user.WeiXinUser;
import com.baodanyun.websocket.controller.CustomerApi;
import com.baodanyun.websocket.exception.BusinessException;
import com.baodanyun.websocket.service.CacheService;
import com.baodanyun.websocket.service.PersonalService;
import com.baodanyun.websocket.util.CommonConfig;
import com.baodanyun.websocket.util.JSONUtil;
import com.baodanyun.websocket.util.KdtApiClient;
import com.google.gson.reflect.TypeToken;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by yutao on 2016/9/27.
 */
@Service
public class PersonalServiceImpl implements PersonalService {
    protected static Logger logger = Logger.getLogger(CustomerApi.class);
    @Autowired
    private CacheService cacheService;

    @Override
    public List<PersonalInfo> getPersonalInfos(Long uid) {
        String userInfo = (String) cacheService.get(CommonConfig.USER_INFO_KEY + uid);
        if (StringUtils.isBlank(userInfo) && null != uid) {
            RequestBean requestBean = new RequestBean(uid);
            userInfo = KdtApiClient.postJson(KdtApiClient.APiMethods.getUserInfo.getValue(), requestBean);
            cacheService.setOneDay(CommonConfig.USER_INFO_KEY + uid, userInfo);
        }

        if (!StringUtils.isEmpty(userInfo)) {
            java.lang.reflect.Type userInfoType = new TypeToken<List<PersonalInfo>>() {
            }.getType();

            logger.info("uid[" + uid + "]getPersonalInfos[" + userInfo + "]");
            List<PersonalInfo> userinfoList = JSONUtil.fromJson(userInfo, userInfoType);
            return userinfoList;
        } else {
            return null;
        }


    }

    @Override
    public List<PersonalInfo> getPersonalInfos(String openId) throws BusinessException {
        Long uid = getUidByOpenId(openId);
        if (null != uid) {
            return getPersonalInfos(uid);
        } else {
            return null;
        }

    }

    @Override
    public PersonalInfo getPersonalInfo(Long uid) {
        List<PersonalInfo> infos = getPersonalInfos(uid);
        if (!CollectionUtils.isEmpty(infos)) {
            for (PersonalInfo personalInfo : infos) {
                //只拿个人信息
                if (1 == personalInfo.getIdentitytype()) {
                    return personalInfo;
                }
            }
        }
        return null;
    }

    @Override
    public Map getPersonalUserAccount(String openId) throws Exception {
        Long uid = getUidByOpenId(openId);
        if (null != uid) {
            return getPersonalUserAccount(uid);
        } else {
            return null;
        }
    }

    @Override
    public Map getPersonalUserAccount(Long uid) throws Exception {
        String userAccount = (String) cacheService.get(CommonConfig.USER_ACCOUNT_KEY + uid);
        if (StringUtils.isBlank(userAccount)) {
            RequestBean requestBean = new RequestBean(uid);
            userAccount = KdtApiClient.postJson(KdtApiClient.APiMethods.getUserAccount.getValue(), requestBean);
            cacheService.setOneDay(CommonConfig.USER_ACCOUNT_KEY + uid, userAccount);
        }

        if (!StringUtils.isEmpty(userAccount)) {
            logger.info("getPersonalUserAccount [" + userAccount + "]");
            Map map = JSONUtil.toObject(Map.class, userAccount);

            return map;
        } else {
            return null;
        }
    }

    @Override
    public PersonalInfo getPersonalInfo(String openId) throws BusinessException {
        Long uid = getUidByOpenId(openId);
        if (null != uid) {
            return getPersonalInfo(uid);
        } else {
            return null;
        }
    }

    @Override
    public List<WeiXinUser> getWeiXinUser(String userName, String phone, String nickName) {
        /*if (!Config.bdyEnv.equals("online")) {
            List<WeiXinUser> li = new ArrayList<>();
            WeiXinUser info = new WeiXinUser();
            info.setMobile("电话");
            info.setPname("姓名");
            info.setOpenId(phone);
            info.setNickName("昵称");
            li.add(info);
            return li;
        }*/
        Map map = new HashMap();
        if (StringUtils.isEmpty(userName) && StringUtils.isEmpty(phone) && StringUtils.isEmpty(nickName)) {
            throw new RuntimeException("参数不能都为空");
        } else if (!StringUtils.isEmpty(phone)) {
            map.put("mobile", phone);
        } else if (!StringUtils.isEmpty(userName)) {
            map.put("pname", userName);
        } else if (!StringUtils.isEmpty(nickName)) {
            map.put("nickName", nickName);
        }
        String PersonalInfo = KdtApiClient.postJson(KdtApiClient.APiMethods.getUserByPhone.getValue(), map);

        if (!StringUtils.isEmpty(PersonalInfo)) {
            java.lang.reflect.Type userInfoType = new TypeToken<List<WeiXinUser>>() {
            }.getType();
            List<WeiXinUser> orderInfos = JSONUtil.fromJson(PersonalInfo, userInfoType);
            return orderInfos;
        } else {
            return null;
        }
    }

    @Override
    public List<OrderInfo> getOrderInfo(Long uid) {

        String orderInfo = (String) cacheService.get(CommonConfig.USER_ORDER_KEY + uid);
        if (StringUtils.isBlank(orderInfo)) {
            RequestBean requestBean = new RequestBean(uid);
            orderInfo = KdtApiClient.postJson(KdtApiClient.APiMethods.getOrderInfo.getValue(), requestBean);
            cacheService.setOneDay(CommonConfig.USER_ORDER_KEY + uid, orderInfo);

        }

        if (!StringUtils.isEmpty(orderInfo)) {
            java.lang.reflect.Type userInfoType = new TypeToken<List<OrderInfo>>() {
            }.getType();
            List<OrderInfo> orderInfos = JSONUtil.fromJson(orderInfo, userInfoType);
            return orderInfos;
        } else {
            return null;
        }

    }

    @Override
    public List<OrderInfo> getOrderInfo(String openId) throws BusinessException {
        Long uid = getUidByOpenId(openId);

        if (null != uid) {

            return getOrderInfo(uid);
        } else {
            return null;
        }


    }

    @Override
    public List<ClaimsInfo> getClaimsInfo(Long uid) {

        String claimsInfo = (String) cacheService.get(CommonConfig.USER_CLAIMS_KEY + uid);
        if (StringUtils.isBlank(claimsInfo)) {
            RequestBean requestBean = new RequestBean(uid);
            claimsInfo = KdtApiClient.postJson(KdtApiClient.APiMethods.getClaimsInfo.getValue(), requestBean);
            cacheService.setOneDay(CommonConfig.USER_CLAIMS_KEY + uid, claimsInfo);

        }

        if (!StringUtils.isEmpty(claimsInfo)) {
            java.lang.reflect.Type claimsInfoType = new TypeToken<List<ClaimsInfo>>() {
            }.getType();
            List<ClaimsInfo> claimsInfos = JSONUtil.fromJson(claimsInfo, claimsInfoType);

            return claimsInfos;
        } else {
            return null;
        }

    }

    @Override
    public List<ClaimsInfo> getClaimsInfo(String openId) throws BusinessException {
        Long uid = getUidByOpenId(openId);

        if (null != uid) {
            return getClaimsInfo(uid);
        } else {
            return null;
        }
    }

    @Override
    public List<EnterpriseContractByUidBean> getContractInfo(Long uid) {

        String contractInfo = (String) cacheService.get(CommonConfig.USER_CONTRACT_KEY + uid);
        if (StringUtils.isBlank(contractInfo)) {
            RequestBean requestBean = new RequestBean(uid);
            contractInfo = KdtApiClient.postJson(KdtApiClient.APiMethods.getContract.getValue(), requestBean);
            cacheService.setOneDay(CommonConfig.USER_CONTRACT_KEY + uid, contractInfo);

        }

        if (!StringUtils.isEmpty(contractInfo)) {
            java.lang.reflect.Type contractInfoType = new TypeToken<List<EnterpriseContractByUidBean>>() {
            }.getType();
            List<EnterpriseContractByUidBean> contractInfos = JSONUtil.fromJson(contractInfo, contractInfoType);

            return contractInfos;
        } else {
            return null;
        }

    }

    @Override
    public List<EnterpriseContractByUidBean> getContractInfo(String openId) throws BusinessException {

        Long uid = getUidByOpenId(openId);
        if (null != uid) {
            return getContractInfo(uid);
        } else {
            return null;
        }
    }

    @Override
    public PersonalDetail getPersonalDetail(String openId) throws BusinessException {
        Long uid = getUidByOpenId(openId);
        if (null != uid) {
            return getPersonalDetail(uid);
        } else {
            return null;
        }

    }

    @Override
    public PersonalDetail getPersonalDetail(Long uid) {
        PersonalDetail pd = new PersonalDetail();

        PersonalInfo personalInfo = getPersonalInfo(uid);
        List<Company> company = getCompany(uid);
        List<PersonalInfo> personalInfos = getPersonalInfos(uid);

        List<ClaimsInfo> claimsInfos = getClaimsInfo(uid);//理赔
        List<OrderInfo> orderInfos = getOrderInfo(uid);//体检订单
        List<EnterpriseContractByUidBean> contractInfos = getContractInfo(uid);//合同订单

        pd.setPersonalInfo(personalInfo);
        pd.setPersonalInfos(personalInfos);
        pd.setOrderInfos(orderInfos);
        pd.setClaimsInfos(claimsInfos);
        pd.setContractInfos(contractInfos);
        pd.setCompany(company);

        return pd;
    }

    @Override
    public List<Company> getCompany(Long uid) {
        String company = (String) cacheService.get(CommonConfig.USER_COMPANY_KEY + uid);
        if (StringUtils.isBlank(company)) {
            RequestBean requestBean = new RequestBean(uid);
            company = KdtApiClient.postJson(KdtApiClient.APiMethods.getUserCompany.getValue(), requestBean);
            cacheService.setOneDay(CommonConfig.USER_COMPANY_KEY + uid, company);
        }

        if (!StringUtils.isEmpty(company)) {
            java.lang.reflect.Type companyType = new TypeToken<List<Company>>() {
            }.getType();
            List<Company> commpanys = JSONUtil.fromJson(company, companyType);

            return commpanys;
        } else {
            return null;
        }
    }


    /**
     * 根据openid获取uid
     *
     * @param openId
     * @return
     */
    public Long getUidByOpenId(String openId) throws BusinessException {

        KdtApiClient kdtApiClient = new KdtApiClient();
        RequestBean requestBean = new RequestBean();
        requestBean.setOpenId(openId);

        String key = CommonConfig.USER_OPENID_KEY + openId;
        String hrUser = (String) cacheService.get(key);
        logger.info("hrUser-------:" + hrUser);

        // 清除缓存错误数据   如果缓存为json，删除
        if (!StringUtils.isEmpty(hrUser)) {
            if (StringUtils.isNumeric(hrUser)) {
                cacheService.remove(key);
                hrUser = null;
            }
        }

        if (StringUtils.isBlank(hrUser)) {
            hrUser = kdtApiClient.postJson(KdtApiClient.APiMethods.getUidByOpenId.getValue(), requestBean);
        }
        if ("-1".equals(hrUser)) {
            hrUser = null;
        }
        logger.info("hruser:" + hrUser);
        HrUser user = null;
        try {
            if (!StringUtils.isEmpty(hrUser)) {
                user = JSONUtil.toObject(HrUser.class, hrUser);
                Long uid = user.getUid();

                if (null == uid || 0 == uid) {
                    throw new BusinessException(openId + "不存在");
                } else {
                    logger.info("uid:[" + uid + "]--------openId[" + openId + "]");
                    logger.info(cacheService.setOneMonth(CommonConfig.USER_OPENID_KEY + openId, hrUser));
                    return uid;

                }
            }

        } catch (Exception e) {
            logger.info(e);
        }

        return null;
    }


}
