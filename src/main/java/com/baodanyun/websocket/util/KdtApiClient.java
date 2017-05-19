package com.baodanyun.websocket.util;

import com.baodanyun.websocket.bean.Response;
import com.baodanyun.websocket.exception.BusinessException;
import org.apache.commons.lang.StringUtils;
import org.slf4j.LoggerFactory;

public class KdtApiClient {

    protected static final org.slf4j.Logger logger = LoggerFactory.getLogger(KdtApiClient.class);

    private static final String apiEntry = Config.userinfointerface;

    private static final String APP_RES = Config.appKey;

    public static String postJson(String method, Object object) {
        String json = null;
        try {
            String url = apiEntry + "/" + method;
            json = HttpUtils.postJson(url, JSONUtil.toJson(object));
            if (!StringUtils.isEmpty(json)) {
                return desc(json);
            } else {
                return null;
            }
        } catch (Exception e) {
            logger.info("post json " + json, e);
        }
        return null;
    }

    public static String desc(String json) throws Exception {
        if (StringUtils.isNotBlank(json)) {
            Response response = JSONUtil.toObject(Response.class, json);
            if (response.isSuccess()) {
                if (response.getData() != null) {
                    return DesUtil.decryptECB(APP_RES, response.getData().toString());
                } else {
                    logger.info("response.getData() is null");
                }

            } else {
                throw new BusinessException(JSONUtil.toJson(response.getData()));
            }
        }

        return  null;
    }

    private static String getJson(String method) {
        String json = null;
        try {
            String url = apiEntry + "/" + method;
            json = HttpUtils.get(url);
            return desc(json);
        } catch (Exception e) {
            logger.info("post json ["+json+"]", e);
        }
        return null;
    }

    public enum APiMethods {
        //获取用户信息
        getUserInfo("examExternal/getPersonMess.do"),

        //获取公司/personMessage/companyMess.do/731
        getUserCompany("examExternal/getPersonCompanyMess.do"),

        //获取用ACCOUNT
        getUserAccount("examExternal/getPersonalUserAccount.do"),

        //获得订单信息
        getOrderInfo("examExternal/orderAll.do"),
        //获取理赔信息
        getClaimsInfo("examExternal/insurance/claims.do"),
        //根据openid获取uid
        getUidByOpenId("wxExternal/findWxFUid"),
        //获取合同信息
        getContract("examExternal/getEnterpriseContractByUid.do"),
        // 查询用户
        getUserByPhone("examExternal/getUsersWeiXinInfo.do");

        private String value;

        APiMethods(String value) {
            this.value = value;
        }

        public String getValue() {
            return this.value;
        }
    }

    /*public static void main(String[] args) {
        KdtApiClient kdtApiClient = new KdtApiClient();
        RequestBean requestBean = new RequestBean();
        Gson gson = new Gson();
        try {
            requestBean.setUid(306l);
            String json = kdtApiClient.postJson(APiMethods.getContract.getValue(), requestBean);
            java.lang.reflect.Type type = new TypeToken<List<EnterpriseContractByUidBean>>() {
            }.getType();
            List<EnterpriseContractByUidBean> list = gson.fromJson(json, type);
            if (!CollectionUtils.isEmpty(list)) {
                for (EnterpriseContractByUidBean contractByUidBean : list) {
                    System.out.println(contractByUidBean);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
//
//        String s ="+LKeU+3ZjmQ=";
//        try {
//            System.out.println(DesUtil.decryptECB(APP_RES, s)); ;
//        }catch (Exception e){
//            e.getStackTrace();
//        }

    }*/

}
