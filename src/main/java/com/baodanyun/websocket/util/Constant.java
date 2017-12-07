package com.baodanyun.websocket.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by yutao on 2015/7/31.
 */
public class Constant {
    //虚拟的渠道商。代表全部渠道
    public static final Long ALL_CHANNEL_ID = -2L;
    /**
     * 消息业务类型编号 只能用三级编号
     */
    public static final int MSG_BIZ_IN = 100000000;//保险
    public static final int MSG_BIZ_IN_BAOQUAN = 100001000;//保险-保全业务
    public static final int MSG_BIZ_IN_QIYUE = 100002000;//保险-契约业务
    public static final int MSG_BIZ_IN_BAOYUE = 100003000;//保险-保全契约(保险管理)
    public static final int MSG_BIZ_IN_BAOYUE_JH = 100003001;//保险-保险管理-保单生效（激活后推送）100003000--100003001
    public static final int MSG_BIZ_PAY_SUCCESS = 100003002;//保险-保险管理-购买成功
    public static final int MSG_BIZ_IN_CLA = 100004000;//保险-理赔业务
    public static final int MSG_BIZ_IN_CLA_TOIN = 100004001;//保险-理赔业务-理赔影像件邮件发送
    public static final int MSG_BIZ_IN_CLA_SUBMIT = 100004002;//保险-理赔-理赔申请提交成功
    public static final int MSG_BIZ_IN_CLA_YXQQ = 100004003;//保险-理赔-理赔影像齐全
    public static final int MSG_BIZ_IN_CLA_ZLQQ = 100004004;//保险-理赔-资料齐全
    public static final int MSG_BIZ_IN_CLA_WTJ = 100004005;//保险-理赔-问题件
    public static final int MSG_BIZ_IN_CLA_PAY = 100004006;//保险-理赔-结案（正常给付）
    public static final int MSG_BIZ_IN_CLA_BOUNCE = 100004007;//保险-理赔-结案（拒付）
    public static final int MSG_BIZ_IN_CLA_CANCLE = 100004008;//保险-理赔-案件撤销
    public static final int MSG_BIZ_IN_CLA_SHTG = 100004009;//保险-理赔-案件审核通过
    public static final int MSG_BIZ_IN_CLA_YWTX = 100004010;//保险-理赔-案件意外报案提醒
    public static final int MSG_BIZ_IN_CLA_101 = 100004011;//报案状态更新为初审处理中
    public static final int MSG_BIZ_IN_CLA_0 = 100004012;//报案状态更新为已作废
    public static final int MSG_BIZ_IN_CLA_THAN_3000 = 100004013;//报案状态更新为录入完成且发票金额大于3000
    public static final int MSG_BIZ_IN_CLA_EDM = 100004000;//保险-理赔-EDM发送邮件

    public static final int MSG_BIZ_HE = 200000000;//健康业务
    public static final int MSG_BIZ_HE_PE = 200001000;//健康业务-体检
    public static final int MSG_BIZ_HE_PE_VAL = 200001001;//健康业务-体检-体检套餐生效
    public static final int MSG_BIZ_HE_PE_DUE = 200001002;//健康业务-体检-产品到期提醒
    public static final int MSG_BIZ_HE_PE_BOOK = 200001003;//健康业务-体检-体检预约成功
    public static final int MSG_BIZ_HE_PE_PRE = 200001004;//健康业务-体检-检前通知
    public static final int MSG_BIZ_HE_PE_MOD = 200001005;//健康业务-体检-体检更改预约时间
    public static final int MSG_BIZ_HE_PE_CAL = 200001007;//健康业务-体检-体检取消预约
    public static final int MSG_BIZ_HE_PE_REP = 200001008;//健康业务-体检-体检报告生成
    public static final int MSG_BIZ_HE_PE_OUT = 200001009;//健康业务-体检-产品到期未预约

    public static final int MSG_BIZ_OTH = 300000000;//其它业务
    //public static final int MSG_BIZ_OTH_QUN        = 300001000;//其它业务--客户关怀
    //public static final int MSG_BIZ_OTH_2016       = 300001001;//其它业务--客户关怀---节日问候
    public static final int MSG_BIZ_OTH_AD = 300002000;//其它业务--管理员
    public static final int MSG_BIZ_OTH_AD_FEE = 300002001;//其它业务--管理员---余额通知
    public static final int MSG_BIZ_OTH_AD_BIZ = 300002002;//其它业务--管理员---业务通知
    public static final int MSG_BIZ_OTH_AD_SER = 300002003;//其它业务--管理员---服务器通知
    public static final int MSG_BIZ_OTH_AD_ANALYS = 300002004;//其它业务--管理员---登陆用户行为分析统计
    public static final int MSG_BIZ_OTH_WEEKALL_ANALYS = 300002005;//其它业务--周统计--企业和个人数据

    public static final int MSG_BIZ_OTH_USER = 300003000;//其它业务--用户
    public static final int MSG_BIZ_OTH_USER_BIND = 300003002;//其它业务--用户---账户绑定
    public static final int MSG_BIZ_OTH_USER_REAL = 300003003;//其它业务--用户---账户解绑
    public static final int MSG_BIZ_OTH_USER_JHYQ = 300003004;//其它业务--用户---激活邀请 100007001 -- 300003004
    public static final int MSG_BIZ_OTH_USER_KHGH = 300003005;//其它业务--用户---节日问候 300001001 -- 300003005


    public static final int MSG_BIZ_OTH_MOBILE_CODE = 300004000;//其它业务--验证码
    public static final int MSG_BIZ_OTH_DYN_CODE = 300004001;//其它业务--验证码--动态密码登录
    public static final int MSG_BIZ_OTH_FOR_CODE = 300004002;//其它业务--验证码--忘记密码
    public static final int MSG_BIZ_OTH_REGISTER = 300004003;//其它业务--验证码--用户注册
    public static final int MSG_BIZ_OTH_FOR_SELF = 300004004;//其它业务--验证码--身份验证
    public static final int MSG_BIZ_OTH_PASSWORD = 300004005;//其它业务--验证码--修改密码
    public static final int MSG_BIZ_OTH_IMG_UPLOAD = 300004006;//其它业务--图片上传失败


    public static final String ZHONGYI_15_DESC_KEY = "ZHONGYI_15_DESC_KEY";

    public static final String ZHONGYI_15_TITLE_KEY = "ZHONGYI_15_TITLE_KEY";

    /**
     * 邀请好友
     */
    public static final int KIND_INVITE_FRIDENT = 5;
    //系统分隔符
    public static final String SPLIT_CHAR = "_";
    public static final String SPLIT_CHAR2 = "/";
    public static final String SPLIT_CHAR3 = "@";
    public static final String IMAGES = "images";
    public static final String SALT = "bdy";
    //系统类型
    public static final String ENTERPRISE_USER = "CU"; //企业
    public static final String ENTERPRISE_MANAGER_USER = "CMU"; //企业管理员
    public static final String CUSTOMER_SERVICE_USER = "CS";//客服
    public static final String PERSONAL_USER = "PU";//个人
    //可登录字段 数据库能登录的用户 字段 要规范 old 不要用了
    public static final String LOGIN_COLUMN_MOBILE = "mobie";
    public static final String LOGIN_COLUMN_EMAIL = "email";
    public static final String LOGIN_COLUMN_USERNAME = "name";
    //这是新的 上边的不要用了
    public static final Byte USER_LOGIN_MOBILE = 1;
    public static final Byte USER_LOGIN_EMAIL = 2;
    public static final Byte USER_LOGIN_USERNAME = 3;
    //状态
    public static final byte STATUS_VALID = 1; //可用
    public static final byte STATUS_INVALID = 0;//不可用
    //性别
    public static final byte SEX_MALE = 1;//男
    public static final byte SEX_FEMALE = 2;//女
    public static final byte SEX_CANNOT_KONW = 3;//未知
    //是否第一次登陆
    public static final byte FIRST_LOGIN_TRUE = 1;//是
    public static final byte FIRST_LOGIN_FALSE = 0;//否
    public static final byte SHARE_NO = 0;
    public static final byte SHARE_YES = 1;
    //用户来源
    public static final byte USER_SOURCE_EMP = 0;//0 或者 0 来源于企业
    public static final byte USER_SOURCE_REG = 1;//1 注册用户
    public static final byte USER_SOURCE_PI = 2;//2 个人合同创建
    //共享者关系
    public static final byte USER_RELATIONSHIP_QR = 1;//亲人
    public static final byte USER_RELATIONSHIP_TX = 2;//同学
    public static final byte USER_RELATIONSHIP_PY = 3;//朋友
    public static final byte USER_RELATIONSHIP_OTHER = 4;//其他
    //身份证正则表达式(15位)
    public static final String IDCARD_15_PATTERN = "^[1-9]\\d{7}((0\\d)|(1[0-2]))(([0|1|2]\\d)|3[0-1])\\d{3}$";
    /*
           三大运营商最新号段 合作版
           移动号段：134 135 136 137 138 139 147 150 151 152 157 158 159 178 182 183 184 187 188
           联通号段：130 131 132 145 155 156 175 176 185 186
           电信号段：133 153 177 180 181 189
           虚拟运营商:170
       */
//    public static final String MOBILE_PATTERN = "^((13[0-9])|(15[^4,\\D])|(18[0-9])|(17[0-9]))\\d{8}$";
//    public static final String EMAIL_PATTERN = "^([a-z0-9A-Z]+[-|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$";
    //身份证正则表达式(18位)
    public static final String IDCARD_18_PATTERN = "^[1-9]\\d{5}[1-9]\\d{3}((0\\d)|(1[0-2]))(([0|1|2]\\d)|3[0-1])\\d{3}([0-9]|X)$";
    //用户登录过期时间
    public static final String USER_KEY = "USER_KEY"; //客服
    /**
     * 用户user保存session的key值
     */
    public static final String USER = "user"; //
    /**
     * 系统url地址
     */
    public static final String SYSTEMURL = "SystemUrl"; //系统url地址
    public static final String SYSTEMNAME = "SystemName"; //系统名称
    public static final String GATEWAYURL = "GatewayUrl"; //门户url地址
    public static final String ENTERPRISE_USER_KEY = "ENTERPRISE_USER_KEY"; //企业
    public static final String PERSONAL_USER_KEY = "PERSONAL_USER_KEY"; //个人
    public static final String LOGIN_TYPE = "LOGIN_TYPE"; //微信登录
    public static final String LOGIN_STATE = "LOGIN_STATE"; //微信state携带
    //SESSION KEY
    public static final String TK_CHANNEL_KEY = "TK_CHANNEL_KEY";
    public static final String SK_FORGET_PASSWORD_UPDATE = "SK_FORGET_PASSWORD_UPDATE"; //
    public static final String SK_INVITE_SUPPLEMENTARY = "SK_INVITE_SUPPLEMENTARY"; //信息补全
    public static final String SK_LOGIN_ERROR_COUNT = "SK_LOGIN_ERROR_COUNT"; //登录错误次数
    public static final String SK_WECHAT_OPENID = "SK_WECHAT_OPENID"; //用户微信登录时session存openid
    public static final String SK_ACCOUNT_MOBILE = "SK_ACCOUNT_MOBILE"; //当前账户登录时所用的手机号
    public static final String SK_RED_TO_URL = "SK_RED_TO_URL"; //登录后跳转地址
    //更改手机号
    public static final String SK_TOKEN_UPDATE_MOBILE = "SK_TOKEN_UPDATE_MOBILE";
    public static final String SK_IS_SHOW_DOUBAO_LOGO = "SK_IS_SHOW_DOUBAO_LOGO";//是否显示logo
    //客服当前权限
    public static final String CUSTOMER_USER_PERMISSIONS = "CUSTOMER_USER_PERMISSIONS";
    //大菜单权限
    public static final String CUSTOMER_USER_BIG_MENU = "CUSTOMER_USER_BIG_MENU";
    //个人用户类型
    public static final byte USER_TYPE_PERSONAL = 1;//个人
    public static final byte USER_TYPE_COMPANY = 2; //企业
    public static final byte USER_TYPE_TJ = 3;      //体检
    //权限
    //menu定义
    public static final byte ROLE_ROOT = 1;//根模块
    public static final byte ROLE_PAGE = 2;//页面模块
    public static final byte ROLE_BUTTON = 3;//按钮权限
    //默认角色id
    public static final long DEFAULT_ROLE_ID = 5;//默认角色
    //默认系统id
    public static final String CHANNEL_SYS_ID = "1";//渠道1
    public static final String CUSTOMER_SYS_ID = "2";//客服2
    //理赔单据ID前缀
    public static final String HEADER = "PA";
    //增员状态 增员
    public static final byte ADD_USER = 1;
    //增员状态 减员
    public static final byte DEL_USER = 0;
    //增员状态 契约
    public static final byte ADD_USER_FIRST = 2;
    //企业增员时产生的用户密码位数
    public static final byte PWD_NUMBER = 8;
    public static final byte SALT_NUMBER = 8;
    //详述是否成功   成功
    public static final byte EMP_CHG_SUCCESS = 1;
    //详述是否成功   失败
    public static final byte EMP_CHG_FAIL = 0;
    //详述是否成功   忽略
    public static final byte EMP_CHG_LOSE = 2;
    //用户类型
    public static final String UP_USER_TYPE_PERSONAL = "person";//个人
    public static final String UP_USER_TYPE_EMP = "emp";//企业
    public static final String UP_USER_TYPE_CUSTOMER = "customerservice";//客服
    //支持的文件类型
    public static final String UP_FILE_TYPE_IMG = "img";
    public static final String UP_FILE_TYPE_DOC = "doc";
    public static final String UP_FILE_TYPE_EXCEL = "excel";
    public static final String UP_FILE_TYPE_VIDEO = "video";
    public static final Map<String, List<String>> IMG_CHECK = new HashMap<String, List<String>>();
    public static final Map<String, List<String>> DOC_CHECK = new HashMap<String, List<String>>();
    public static final String UP_USER_APPLY_BASE_URL = "/upload/up/person/img/";
    public static final String DOWN_USER_APPLY_BASE_URL = "/upload/downDirect/person/img/";
    //excel下的模板
    public static final String DOC_CHILD_TYPE_MODAL = "modal";
    //task增减员业务
    public static final String DOC_CHILD_TYPE_ADD = "add";
    //减员业务
    public static final String DOC_CHILD_TYPE_REMOVE = "remove";
    //契约
    public static final String DOC_CHILD_TYPE_FTHBINS = "fthbins";
    //层级变更
    public static final String DOC_CHILD_TYPE_CHGBINS = "chgbins";
    //教案的
    public static final String DOC_CHILD_TEACHER = "teach";
    //车车的
    public static final String DOC_CHILD_CHECHE = "checheOrder";
    //明亚的
    public static final String DOC_CHILD_MINGYA = "mingyaOrder";
    //平安的
    public static final String DOC_CHILD_PINGAN = "pingAn";
    //太平的
    public static final String DOC_CHILD_TAIPING = "taiPing";
    //图片子类型
    public static final String DJ_TYPE_SY = "djsy";//单据子类型 首页
    public static final String DJ_TYPE_NR = "djnr";//单据子类型 内容
    public static final String DJ_TYPE_BG = "djbg";//单据子类型 变更
    public static final String DJ_TYPE_CLAIMS = "djclaims";//单据子类型 理赔
    public static final byte POLICY_STATUS_VALID = 1;//有效
    public static final byte POLICY_STATUS_INVALID = 2;//失效
    public static final byte POLICY_STATUS_INVALID_0 = 0;//失效
    public static final byte POLICY_STATUS_TIME_OUT = 3;//过期
    public static final byte POLICY_STATUS_OUT_MONEY = 4;//欠费
    public static final byte POLICY_STATUS_UPLOAD = 5;//上传
    public static final byte POLICY_STATUS_SHARE = 6;//共享保单
    public static final byte POLICY_STATUS_ALL = 10;//所有保单
    public static final byte POLICY_STATUS_VERIFY_FAIL = 101;//审核失败
    public static final byte POLICY_STATUS_DEL = 102;// 上传的保单被删除
    //理赔申报 申请事项
    public static final byte REPORT_MATTER_EMG = 1;//门急诊
    public static final byte REPORT_MATTER_CURE = 2;//住院医疗
    public static final byte REPORT_MATTER_SBDY = 3;//住院补贴
    public static final byte REPORT_MATTER_VPDS = 4;//重大疾病
    public static final byte REPORT_MATTER_DSBD = 5;//伤残
    public static final byte REPORT_MATTER_ACDT = 6;//事故
    public static final byte REPORT_MATTER_BITH = 7;//生育
    public static final byte REPORT_MATTER_OTHR = 8;//其他
    // 责任  疾病门诊
    //  public static final String BILITY_DIS_OPT = "8";
    public static final String[] BILITIES_HOP_OPT = {"3", "5", "9", "8", "2", "42", "45", "46"};
    //理赔申报 出险类型
    public static final byte DANGER_ACCIDENT = 1;//意外


    // 责任  意外住院
    //  public static final String BILITY_ADENT_HOP = "3";

    // 责任  意外门诊
    //  public static final String BILITY_ADENT_OPT = "5";


    // 责任  疾病住院
    //  public static final String BILITY_DIS_HOP = "9";
    public static final byte DANGER_DISEASE = 2;//住院医疗
    public static final byte DANGER_OTHER = 3;//住院补贴
    //理赔申请状态   报案
//    public static final byte USER_APPLY_STATUS_REPORT = 1;
    public static final Integer USER_APPLY_STATUS_ONE = 1;//未认领
    public static final Integer USER_APPLY_STATUS_TWO = 2;//已认领 处理中
    public static final Integer USER_APPLY_STATUS_THREE = 3;//影像齐全
    public static final Integer USER_APPLY_STATUS_FOUR = 4;//资料齐全
    public static final Integer USER_APPLY_STATUS_FIVE = 5;//完结
    public static final Integer USER_APPLY_STATUS_SIX = 6;//问题件
    public static final Integer USER_APPLY_STATUS_SEVEN = 7;//问题件已反馈
    public static final Integer USER_APPLY_STATUS_NINE = 9;//9
    public static final Integer USER_APPLY_STATUS_REVOKE = 0; //撤销
    public static final Integer USER_APPLY_STATUS_SHTG = 10; //初审通过
    public static final Integer USER_APPLY_STATUS_NBBA = 11; //内部报案
    public static final Integer USER_APPLY_STATUS_FOUNDER = 12; //方正导入系统
    public static final Integer USER_APPLY_STATUS_CORE = -10; //结案数据导入状态（全量）
    //业务处理状态 状态 未处理
    public static final byte FILE_UPLOAD_STATUS_UNTREATED = 0;
    //业务处理状态 已认领
    public static final byte FILE_UPLOAD_STATUS_EClAIMBNS = 1;
    //业务处理状态 问题下发
    public static final byte FILE_UPLOAD_STATUS_ERROR = 2;
    //业务处理状态 已撤销
    public static final byte FILE_UPLOAD_STATUS_REVOKE = 5;
    //业务处理状态 已确认
    public static final byte FILE_UPLOAD_STATUS_SURE = 4;
    //新改版保险减人状态受理中 HR 申请
    public static final Integer PRESERVATION_REDUCE_IN_STATUS = 1;
    //新改版保险减人状态处理中
    public static final Integer PRESERVATION_REDUCE_COMPLETE_STATUS = 2;
    //新改版保险减人状态处理完成
    public static final Integer PRESERVATION_REDUCE_ACCEPT_STATUS = 3;
    //新改版保险减人状态撤销
    public static final Integer PRESERVATION_REDUCE_REVOKE_STATUS = 4;
    public static final byte FILE_UPLOAD_REMIND_STATUS_YES = 1;
    public static final byte FILE_UPLOAD_REMIND_STATUS_NO = 0;
    //保单共享状态
    public static final byte POLICY_SHARE_STATUS_SHARE = 1;
    public static final byte POLICY_SHARE_STATUS_NO_SHARE = 2;
    //增加好友 删除好友
    public static final byte FRIEND_ADD = 1;
    public static final byte FRIEND_DEL = 2;
    //计划状态
    public static final byte PLAIN_STATUS_YES = 1;
    public static final byte PLAIN_STATUS_NO = 0;
    public static final byte BOOLEAN_TRUE = 1;
    public static final byte BOOLEAN_FALSE = 0;
    //用户信息类型 企业
    public static final byte IDENTITY_TYPE_E = 2;
    //用户信息类型 个人
    public static final byte IDENTITY_TYPE_U = 1;
    //用户信息类型 连带被保险人
    public static final byte IDENTITY_TYPE_J = 0;
    //体检用户
    public static final byte IDENTITY_TYPE_T = 3;
    public static final Map<String, Byte> IDCARD_TYPE_MAP = new HashMap<>();
    public static final byte IDCARD_TYPE_IDEN = 1;//身份证
    public static final byte IDCARD_TYPE_PSPT = 2;//护照
    public static final byte IDCARD_TYPE_OFER = 3;//军官证
    public static final byte IDCARD_TYPE_HK = 4;//港澳居民通行证
    public static final byte IDCARD_TYPE_TAI = 5;//台湾居民通行证
    public static final byte IDCARD_TYPE_OTHER = 6;//其他
    public static final byte IDCARD_TYPE_S = 7;//士兵证
    public static final byte IDCARD_TYPE_F = 8;//返乡证
    public static final byte IDCARD_TYPE_Z = 9;//织机构代码
    public static final byte IDCARD_TYPE_H = 10;//户口簿
    public static final byte IDCARD_TYPE_X = 11;//学生证
    public static final byte IDCARD_TYPE_B = 12;//出生证
    public static final byte IDCARD_TYPE_D = 13;//驾照
    public static final byte IDCARD_TYPE_M = 14;//军官证
    //业务处理状态 未完成
    public static final byte ENBINSINFO_NOFINISHED = 6;//其他
    public static final byte INNER_MSG_READER = 1;//已经阅读
    public static final byte INNER_MSG_NOT_READER = 0;//未阅读
    public static final byte INVITE_USER = 1;//是否是被邀请用户
    public static final byte IS_ADMIN_YES = 1;//是管理员
    public static final byte IS_ADMIN_NO = 2;//非管理员
    public static final String SEND_INNER_MSG_USER = "系统";
    //该业务的excle导入是否完成  是
    public static final byte BSN_EXCEL_ISDONE_TRUE = 1;
    //否
    public static final byte BSN_EXCEL_ISDONE_FALSE = 0;
    public static final String BAOQUAN_LOG_ZY = "YW_ZY";
    public static final String BAOQUAN_LOG_JY = "YW_JY";
    public static final String BAOQUAN_LOG_PC = "YW_PC";
    public static final String BAOQUAN_LOG_QY = "YW_QY";
    public static final String BAOQUAN_LOG_YW_ALL = "YW_ALL";
    public static final String DEFAULT_USER_PWD = "123456";
    public static final Byte CAN_LOGIN_FALSE = 0;
    public static final Byte CAN_LOGIN_TRUE = 1;
    //角色类型  渠道业务员
    public static final Long ROLE_TYPE_FIV = 5L;
    public static final Long ROLE_TYPE_FOUR = 4L;//渠道客服
    //角色类型 豆包网客服
    public static final Long ROLE_TYPE_SND = 2L;
    /**
     * 当前渠道商id标志
     */
    public static final String CUSTOMER_USER_CURR_CHANNELI_D = "CUSTOMER_USER_CURR_CHANNELI_D";
    //
    public static final String PHYSICAL_EXAMINATION = "physicalExamination";
    /**
     *
     */
    public static final String XLS_TJ_CNACCLE = "xls/tj/cancle/";
    public static final String XLS_TJ_BOOK = "xls/tj/book/";
    public static final String OSS_PUBLIC_BUCKET = "http://fpub.17doubao.com/";
    /**
     *
     */
    public static final String ACTIVITY_TJ = "tj";
    public static final String EMP_BNS_EMAIL = "OINTERNAL_EMP_BNS_EMAIL_NTICE";
    public static final String EMP_BNS_WX = "OINTERNAL_EMP_BNS_WX_NTICE";
    public static final String ACTIVITY_SKIING = "skiing";
    /**
     * 上传
     */
    public static final String UPLOAD_SERVER = "uploadServer";
    public static final String ONLINE_UPLOAD_URL = "https://upload.17doubao.com";
    public static final String TEST_UPLOAD_URL = "https://test.17doubao.com/upload";
    /**
     * 17豆包登录企业端链接
     */
    public static final String ENTERPRISE_URL_KEY = "enterpriseUrl";
    public static final String GROUP_URL_KEY = "groupUrl";
    public static final String ONLINE_ENTERPRISE_URL = "b.17doubao.com";
    public static final String TEST_ENTERPRISE_URL = "test.17doubao.com";
    public static final String ONLINE_GROUP_URL = "b.17doubao.com";
    public static final String TEST_GROUP_URL = "test.17doubao.com";
    /**
     * 用户事件统计查询 李扬
     */
    public static final String EVENTSTATISTICS = "http://st.17doubao.com/api/userAction";
    public static final Map<String, String> eventMap = new HashMap<String, String>();
    public static final Map<Long, String> costtypeMap = new HashMap<Long, String>();
    //活动ID
    public static final int QR_ID_FROM_EMAIL = 8;
    public static final int DATA_STATUS_VAL = 1;
    public static final int DATA_STATUS_DEL = -1;
    // 车车订单 EXCEL列名称
    public static final String CHECHE_NAME_1 = "订单号";
    public static final String CHECHE_NAME_2 = "下单日期";
    public static final String CHECHE_NAME_3 = "订单状态";
    public static final String CHECHE_NAME_4 = "车主姓名";
    public static final String CHECHE_NAME_5 = "手机号";
    public static final String CHECHE_NAME_6 = "车牌号";


    //public static final String COMPANYS_CPIC = "太保安联健康保险股份有限公司";
    //public static final String COMPANYS_ZAZX = "众安在线财产保险股份有限公司";
    public static final String CHECHE_NAME_7 = "保险公司";
    public static final String CHECHE_NAME_8 = "车险报价";
    public static final String CHECHE_NAME_9 = "实际付款";
    public static final String CHECHE_NAME_10 = "身份证号";
    // 明亚订单 EXCEL列名称
    public static final String MINGYA_NAME_1 = "保单号";
    public static final String MINGYA_NAME_2 = "产品名称";
    public static final String MINGYA_NAME_3 = "保险公司";
    public static final String MINGYA_NAME_4 = "金额";
    public static final String MINGYA_NAME_5 = "投保日期";
    public static final String MINGYA_NAME_6 = "生效日期";
    public static final String MINGYA_NAME_7 = "失效日期";
    public static final String MINGYA_NAME_8 = "投保人姓名";
    public static final String MINGYA_NAME_9 = "投保人性别";
    public static final String MINGYA_NAME_10 = "投保人证件类型";
    public static final String MINGYA_NAME_11 = "投保人证件号码";
    public static final String MINGYA_NAME_12 = "投保人手机号";
    public static final String MINGYA_NAME_13 = "被保险人姓名";
    public static final String MINGYA_NAME_14 = "被保险人性别";
    public static final String MINGYA_NAME_15 = "被保险人证件类型";
    public static final String MINGYA_NAME_16 = "被保险人证件号码";
    public static final String MINGYA_NAME_17 = "被保险人手机号";
    public static final String MINGYA_NAME_18 = "受益人姓名";
    public static final String MINGYA_NAME_19 = "受益人性别";
    public static final String MINGYA_NAME_20 = "受益人证件类型";
    public static final String MINGYA_NAME_21 = "受益人证件号码";
    public static final String MINGYA_NAME_22 = "受益人手机号";
    // 明亚订单 EXCEL列数
    public static final int MINGYA_COLUMN = 12;
    /*********
     * 工单状态
     ***********/
    public static final String CHANAGE_HR = "hr";
    public static final String CHANAGE_CREATE = "create";//创建
    public static final String CHANAGE_CANCEL = "cancel";//撤销
    public static final String CHANAGE_ACCEPTIN = "acceptIn";//处理中
    public static final String CHANAGE_ACCEPTCOMP = "acceptComp";//处理完成
    public static final String CHANAGE_DISMISSAL = "dismissal";//拒绝处理
    public static final int AUTO_CLOSE_TYPE_SUCCESS = 1;//自动结案成功标志
    public static final int AUTO_CLOSE_TYPE_ERROR = 2;//自动结案异常
    public static final int MANUAL_CLOSE_TYPE_SUCCESS = 0;//手动结案
    public static final String JS_CSS_AUTO_VERSION = "JS_CSS_AUTO_VERSION"; //个人
    public static String CONTEXT_PATH = "";
    public static String BAOQUAN_CARD_LOG = "";
    /************
     * 豆包客服系统
     ***************/
    public static String KF_TEST_URL = "https://open.weixin.qq.com/connect/oauth2/authorize?appid=wxfd62c71a2391558b&redirect_uri=https%3A%2F%2Ftest.17doubao.com%2Fmobile%2Fother%2Fdoubao%2Fkf&response_type=code&scope=snsapi_userinfo&state=STATE#wechat_redirect";
    public static String KF_ONLINE_URL = "https://open.weixin.qq.com/connect/oauth2/authorize?appid=wx06aaa54cd7d90955&redirect_uri=https%3A%2F%2Fm.17doubao.com%2Fother%2Fdoubao%2Fkf&response_type=code&scope=snsapi_userinfo&state=STATE#wechat_redirect";
    /*****************
     * 咨询页面
     ********************/
    public static String CONSULT_TEST_URL = "https://test.17doubao.com/mobile/consult/index";
    public static String CONSULT_ONLINE_URL = "https://m.17doubao.com/consult/index";
    public static String bxEventId = "0401";//个险产品--uec事件完成购买
    public static String tjEventId = "0402";//体检产品--uec事件完成购买
    public static int Event_OType_Product_Finished = 3; //购买完成 --事件中心定义的type
    public static String UEC_WX_REPLY = "020806";//微信回复
    public static String UEC_WX_CLICK = "020807";//微信菜单点击
    public static String UEC_Personal_Contract = "0209";//个人合同失效
    public static String UEC_WX_SCAN = "020808";//微信扫码
    public static String UEC_WX_FOCUS = "020809";//微信关注
    public static String UEC_WX_SCAN_GZ_FHB = "SMGZFHB_";//微信扫码关注发红包
    public static int DELETE_BILL_FEE_TYPE = 9990;//删除账单信息
    public static String DELETE_BILL_FEE = "异常处理_删除账单信息和费用信息";//删除账单信息

    public static Long STOREUPFLAG_1 = 1L;//收藏标记 1—收藏
    public static Long STOREUPFLAG_2 = 2L;//收藏标记 2—未收藏
    public static Long STOREUPFLAG_3 = 3L;//收藏标记 3—游客 （收藏保单）

    static {
        List<String> IMG_CHECK_LIST = new ArrayList<String>();
        IMG_CHECK_LIST.add("jpg");
        IMG_CHECK_LIST.add("jpeg");
        IMG_CHECK_LIST.add("png");
        IMG_CHECK_LIST.add("bmp");
        IMG_CHECK_LIST.add("gif");
        IMG_CHECK.put(UP_FILE_TYPE_IMG, IMG_CHECK_LIST);
    }

    static {
        IDCARD_TYPE_MAP.put("idCard", IDCARD_TYPE_IDEN);
        IDCARD_TYPE_MAP.put("hz", IDCARD_TYPE_PSPT);
        IDCARD_TYPE_MAP.put("jgz", IDCARD_TYPE_OFER);
        IDCARD_TYPE_MAP.put("gajmtxz", IDCARD_TYPE_HK);
        IDCARD_TYPE_MAP.put("twjmtxz", IDCARD_TYPE_TAI);
        IDCARD_TYPE_MAP.put("other", IDCARD_TYPE_OTHER);
    }

    static {
        eventMap.put("login", "登录");
        eventMap.put("integral", "积分事件");
        eventMap.put("msg", "消息事件");
        eventMap.put("index", "首页事件");
        eventMap.put("epsGroupIns", "企业团险");
        eventMap.put("welfare", "弹性福利");
        eventMap.put("epsLogin", "HR平台");
        eventMap.put("insPolicy", "我的保单");
        eventMap.put("vip", "会员专区");
        eventMap.put("companyIntro", "关于豆包");
        eventMap.put("popactive", "红包事件");
        eventMap.put("toClaim", "我要理赔");
        eventMap.put("claims", "理赔查询");
        eventMap.put("yanshi", "演示");
        eventMap.put("exams", "体检");
        eventMap.put("shareperson", "共享人管理");
        eventMap.put("uploadpolicy", "上传保单");
        eventMap.put("handleclaims", "我要理赔");
        eventMap.put("logout", "登出");
    }

    static {
        costtypeMap.put(0L, "理赔支出");
        costtypeMap.put(1L, "利息");
        costtypeMap.put(2L, "管理费");
        costtypeMap.put(3L, "基金转入");
    }


}
