# 客服系统API说明文档

## uec 中心对接客服js文件

http://bj-bdy-public.oss-cn-beijing.aliyuncs.com/kf/js/talkFromUec.js

## 客服修改密码接口说明

```
    url:'/api/upCustomerPwd',
    type:POST,
    data:{
       cjid : string  //客服id
       oldPWD : string  //旧密码
       newPWD : string  //新密码
       confirmPWD : string  //确认密码
    },
    retrun {
        success:true || false   //true 未修改成功
    }
```


## 修改客服信息接口

```
    url:/api/upCustomerInfo,
    type:POST,
    data:{
        cjid : string  //客服id  --111111@126xmpp
        icon : string   //avatar
        nickName : string    //昵称   -- 1234
        userName : string    //真实姓名
        workNumber : string    //工号
        desc : string   //描述
    },
    return {
         success:true || false   //true 未修改成功,
         data:{
            icon : string   //avatar
            //workNumber : string    //工号
            desc : string   //描述
            "limitQueue": 0,    //队列大小
            "userType": "customer",  //类型 customer为客服   visitor为访客
            "id": "111111@126xmpp",     //客服id
            "openId": "111111",     //微信唯一标识符
            "nickName": "1234",         //昵称
            "userName": "111111",       //真实姓名
            "loginUsername": "111111",  //登录账号 登录名
            "loginTime": 1478668687034,     //最近一次登录时间
            "vip": false        //是否VIP
         }
    }

```

## 修改访客信息接口
 
```
    url:/api/upVisitorInfo,
    type:POST,
    data:{
        id: string
        cjid : string  // 111111@126xmpp
        remark : string   //描述
        tags：[{
            id:number,
            name: string,
            state:true
        }]   // 多个标签,分割
    },
    return {
         success:true || false   //true 未修改成功,
         data:{
            icon : string   //avatar
            workNumber : string    //工号
            desc : string   //描述
            "userType": "customer",  //类型 customer为客服   visitor为访客
            "id": "111111@126xmpp",     //客服id
            "openId": "111111",     //微信唯一标识符
            "nickName": "1234",         //昵称
            "userName": "111111",       //真实姓名
            "loginUsername": "111111",  //登录账号 登录名
            "loginTime": 1478668687034,     //最近一次登录时间
            "vip": false        //是否VIP
             "tags":[]
         }
    }

```


## 待接入接口

```
    url:'',
    type:POST,
    data:{
        vjid : string  //手机端用户id
    },
    return {
         success:true || false
         data:{
            username : string    //昵称
         }
    }

```



## 转接接口

```
    url:'/api/changeVisitorTo',
    type:POST,
    data:{
        vjid : string  //手机用户端id  -- yutao11132@126xmpp
        fromJid:String// 转出的客服id  -- 111111@126xmpp
        toJid:String    // 转入的客服id  --222222@126xmpp
    },
    return { 
         success:true || false
         data:[{
            id: number, // 客服id
            name: string            // 客服名称
         },{
            id: number, // 客服id
            name: string            // 客服名称
         }]
    }

```

## 获取历史聊天会话接口

```
    url:'/messageHistory/getUserHistoryList',
    type:POST,
    data:{
        id : string  //当前客服id
        stime;String //   开始统计时间   yyyy-MM-dd 统计接入量的时候用
        etime;String //   结束统计时间   yyyy-MM-dd 统计接入量的时候用
        nikeName:String // 访客昵称
        page : int  //当前页数  默认是0
        count : int  //当前页显示条数 默认是10
    },
    return {
         success:true || false
         "code": 0,
         "msg": null,

         data:{
             page : int  //当前页数  默认是0
             count : int  //当前页显示条数 默认是10
             pages: int //总页数
             total: int //总条数
             list:[{
                 "visitor": {
                 icon: string //访客头像
                 username: string //访客用户名
                 ct: date  //访问时间
                 adminIcon:string    //客服头像
                 role:string //客服角色
             }
             "chattime": "2016-11-11"
               }]
         }
    }

```


## 历史消息记录接口

```
    url:'',
    type:POST,
    data:{
        from : String  //消息发送方 -- 111111@126xmpp
        to : String  //消息接收方   -- yutao11132@126xmpp
    },
    return {
         success:true || false
         data:[{
               返回值同对话窗口
         }]
    }

```

## 获取在线客服接口

```
    url:'/api/onlineCustomerList',
    type:POST,
    data:{
       
    },
    return {
         success:true || false
         data:[{
            id: number, // 客服id
            name: string            // 客服名称
         },{
            id: number, // 客服id
            name: string            // 客服名称
         }]
    }

```



## 增加快捷回复
```
    url:'/api/updateQuickReply',
    type:POST,
    data:{
       cjid:String   // 当前客服id
       message:String // 保存内容
    },
    return {
         success:true || false
         data:[{
            id: number, //
            message: string            
         },{
            id: number, 
            message: string            
         }]
    }

```

## 删除快捷回复
```
    url:'/api/deleteQuickReply',
    type:POST,
    data:{
       id:number   // 当前客服id
    },
    return {
         success:true || false
    }
 
```

## 获取访客详细信息接口


```
{
  "success": true,
  "code": 0,
  "data": {
    "vCard": {
      "customerJid": "111111@126xmpp",
      "userType": "visitor",
      "id": "oAH_qsm6yPTi8I3hv_03mq4lCMKk@126xmpp",
      "openId": "oAH_qsm6yPTi8I3hv_03mq4lCMKk",
      "nickName": "小鱼宝宝",
      "userName": "oAH_qsm6yPTi8I3hv_03mq4lCMKk",
      "loginUsername": "oAH_qsm6yPTi8I3hv_03mq4lCMKk",
      "loginTime": 1478830137255,
      "vip": false,
      tags:[]
    },
    "basic": {
      "personalInfos": [
        {
          "id": 562,
          "useraccountid": 306,
          "pname": "于涛啊",
          "mobile": "18301033658",
          "email": "yutao133@163.com",
          "sex": 1,
          "birthday": "Jul 2, 1986 12:00:00 AM",
          "homeaddress": "河北保定22",
          "identitytype": 1,
          "ct": "Sep 29, 2015 3:56:46 PM",
          "ut": "Jun 6, 2016 6:04:05 PM",
          "idcardtype": 1,
          "idcard": "130603198605020918",
          "prov": "河北",
          "city": "保定",
          "dist": "北市区"
        }
      ],
      "claimsInfos": [
        {
          "applycode": "1472539190382",
          "insuranceCompany": "中国太平洋财产保险股份有限公司",
          "applyPerson": "于涛",
          "dangerPerson": "于涛啊",
          "dangerIdCard": "130603198605020918",
          "applyDate": "Aug 30, 2016 2:39:50 PM",
          "chargeMoney": 0,
          "claimsStatus": 0,
          "handleStatus": "撤销",
          "uid": 306
        },
        {
          "applycode": "1476083209430",
          "insuranceCompany": "中国太平洋财产保险股份有限公司",
          "applyPerson": "于涛",
          "dangerPerson": "于涛啊",
          "dangerIdCard": "130603198605020918",
          "applyDate": "Oct 10, 2016 3:06:49 PM",
          "chargeMoney": 0,
          "claimsStatus": 0,
          "handleStatus": "撤销",
          "uid": 306
        },
        {
          "applycode": "1476084664520",
          "insuranceCompany": "中国太平洋财产保险股份有限公司",
          "applyPerson": "于涛",
          "dangerPerson": "于涛啊",
          "dangerIdCard": "130603198605020918",
          "applyDate": "Oct 10, 2016 3:31:04 PM",
          "chargeMoney": 0,
          "claimsStatus": 0,
          "handleStatus": "撤销",
          "uid": 306
        }
      ],
      "orderInfos": [
        {
          "oid": 1689,
          "uid": 306,
          "userInfoId": 59468,
          "caseName": "测试套餐",
          "status": 1,
          "statusName": "已生效",
          "startTime": "Jul 14, 2016 12:00:00 AM",
          "endTime": "Dec 31, 2019 12:00:00 AM",
          "saleModel": 2,
          "saleModelName": "线上",
          "pid": 1038,
          "isBook": 0,
          "isCheck": 0,
          "isCheckName": "未体检"
        },
        {
          "oid": 3701,
          "uid": 306,
          "userInfoId": 91067,
          "caseName": "个人测试体检（勿动）",
          "status": 1,
          "statusName": "已生效",
          "startTime": "Sep 28, 2016 12:00:00 AM",
          "endTime": "Jul 28, 2017 12:00:00 AM",
          "saleModel": 2,
          "saleModelName": "线上",
          "pid": 1060,
          "isMarried": 1,
          "gender": 0,
          "isBook": 0,
          "isCheck": 0,
          "isCheckName": "未体检"
        },
        {
          "oid": 182,
          "uid": 306,
          "userInfoId": 1505,
          "name": "于涛",
          "caseName": "特定B套餐",
          "status": 1,
          "statusName": "已生效",
          "startTime": "Nov 23, 2015 12:00:00 AM",
          "endTime": "Mar 31, 2016 12:00:00 AM",
          "saleModel": 1,
          "saleModelName": "线下",
          "pid": 8,
          "idCard": "130603198605020918",
          "isBook": 0,
          "isCheck": 0,
          "isCheckName": "未体检"
        },
        {
          "oid": 3700,
          "uid": 306,
          "userInfoId": 91066,
          "caseName": "个人测试体检（勿动）",
          "status": 1,
          "statusName": "已生效",
          "startTime": "Sep 28, 2016 12:00:00 AM",
          "endTime": "Jul 28, 2017 12:00:00 AM",
          "saleModel": 2,
          "saleModelName": "线上",
          "pid": 1060,
          "isMarried": 1,
          "gender": 0,
          "isBook": 0,
          "isCheck": 0,
          "isCheckName": "未体检"
        },
        {
          "oid": 3699,
          "uid": 306,
          "userInfoId": 91065,
          "caseName": "个人测试体检（勿动）",
          "status": 1,
          "statusName": "已生效",
          "startTime": "Sep 28, 2016 12:00:00 AM",
          "endTime": "Jul 28, 2017 12:00:00 AM",
          "saleModel": 2,
          "saleModelName": "线上",
          "pid": 1060,
          "isMarried": 1,
          "gender": 0,
          "isBook": 0,
          "isCheck": 0,
          "isCheckName": "未体检"
        },
        {
          "oid": 3357,
          "uid": 306,
          "userInfoId": 86592,
          "caseName": "个人测试体检（勿动）",
          "status": 1,
          "statusName": "已生效",
          "startTime": "Sep 28, 2016 12:00:00 AM",
          "endTime": "Jul 28, 2017 12:00:00 AM",
          "saleModel": 2,
          "saleModelName": "线上",
          "pid": 1060,
          "isMarried": 1,
          "gender": 0,
          "isBook": 0,
          "isCheck": 0,
          "isCheckName": "未体检"
        },
        {
          "oid": 3704,
          "uid": 306,
          "userInfoId": 91079,
          "caseName": "个人测试体检（勿动）",
          "status": 1,
          "statusName": "已生效",
          "startTime": "Sep 28, 2016 12:00:00 AM",
          "endTime": "Jul 28, 2017 12:00:00 AM",
          "saleModel": 2,
          "saleModelName": "线上",
          "pid": 1060,
          "isMarried": 1,
          "gender": 0,
          "isBook": 0,
          "isCheck": 0,
          "isCheckName": "未体检"
        }
      ]
    }
  }
}
```

```
    url:'/api/visitorDetail',
    type:POST,
    data:{
       openid:String  //  访客
       id:number   // 当前访客客服系统 jid
    },
    return {
         success:true || false
         "data": {
            "basic": {
              "personalInfo": [{
                  pname;//个人姓名
                  mobile;//手机号
                  email;//邮箱
                  sex;//性别
                  birthday;//生日
                  workaddress;//工作地点
                  homeaddress;//家庭住址
                  healthdegree;//健康状况
                  identitytype;//个人信息标志位 个人信息 只能取这个值为1的
              }],
              "claimsInfos": [   ////理赔
                {
                 applycode;//理赔单号
                 insuranceCompany;//承保公司
                 applyPerson;//申请人姓名
                 dangerPerson;//受益人姓名
                 dangerIdCard;//受益人证件号
                 applyDate;//申请时间
                 doneTime;//完结时间
                 chargeMoney;//申请金额
                 claimsStatus;//理赔状态
                 handleStatus;//处理状态
                }
              ],
              "orderInfos": [     // 体检订单
                {
                  Long oid;//订单id
                  
                  Long uid;//用户uid
                  
                  Long userInfoId;//用户id
                  
                  String name;//订单姓名
                  
                  String caseName;//套餐名称
                  
                  Long status;//套餐状态(1有效，-1无效)
                  
                  String statusName;//套餐状态(1有效，-1无效)中文转换
                  
                  Date startTime;//套餐有效开始日期
                  
                  Date endTime;//套餐有效结束日期
                  
                  Long saleModel;//销售模式 1线下销售2线上销售
                  
                  String saleModelName;//1线下销售2线上销售 对应的中文
                  
                  Date orderTime;//预约时间
                  
                  String frontName;//预约门店
                  
                  Date checkTime;//体检时间
                  
                  String tjStoreName;//体检门店
                  /********************预约字段******************/
                  Long pid;//套餐id
                  
                  Long isMarried;//适用婚否(0-未婚1-已婚null-通用)
                  
                  Long gender;//适用性别(0-女1-男null-通用)
                  
                  Long frontId;//门店ID
                  
                  String idCard;//订单身份证
                  
                  Long isBook;//是否预约(1:预约0没预约)
                  
                  Long isCheck;//是否到检(1到检0没有到检)
                  
                  String isCheckName;//体检信息

                }
              ],
              "bilityInfos": [  // 合同，暂时未定
                {
                  "name": "ceshi"
                }
              ]
            },
            "vCard": {
              "customerJid": "111111@126xmpp",
              "tags": [
                "1",
                "2",
                "3",
                "4",
                "5",
                "6"
              ],
              "userType": "visitor",
              "id": "yutao11132@126xmpp",
              "openId": "yutao11132",
              "nickName": "yutao11132",
              "userName": "yutao11132",
              "loginUsername": "yutao11132",
              "loginTime": 1478771878354,
              "vip": false
            }
          }
    }
 
```


## 根据id获取留言
```
    url:'api/getMessageById',
    type:POST,
    data:{
       id:number   // 留言信息的id
       status：     // 1 为未处理消息，2 为已处理消息
    },
    return {
         success:true || false
         "data": [{
         "id": 2,
           "cid": "111111",
           "username": "liaowuhen",
           "content": "我是测试",   // 留言内容
           "phone": "18649041578",  // 处理结果
           "status": 2,
            "dealResult": "已处理啊啊啊",
            "ut": "十一月 15, 2016",
            "ct": "十一月 15, 2016"
            }]
    }
 
```


## 根据客服id获取留言
```
    url:'api/getMessageByCId',
    type:POST,
    data:{
       cid:number   // 当前客服idi
       status：      // 1为未处理留言，1 为已处理留言
    },
    return {
         success:true || false
        "data": [{
                 "id": 2,
                   "cid": "111111",
                   "username": "liaowuhen",
                   "content": "我是测试",   // 留言内容
                   "phone": "18649041578",  // 处理结果
                   "status": 2,
                    "dealResult": "已处理啊啊啊",
                    "ut": "十一月 15, 2016",
                    "ct": "十一月 15, 2016"
                    }]
    }
 
```

## 提交留言信息
```
    url:'api/addMessage',
    type:POST,
    data:{
       cid:number   // 咨询客服id
       content：String     // 咨询问题
       phone:String // 手机
       username：String  // 用户名
      
    },
    return {
         success:true || false
          "code": 0,
          "data": 1
    }
 
```


## 更新留言信息
```
    url:'api/updateMessage',
    type:POST,
    data:{
       id:number   // 消息id
       result：String      // 回复内容
      
    },
    return {
         success:true || false
          "code": 0,
          "data": 1
    }
 
```


## 第三方接口获取个人信息api     解密字符串  _(&%#!_L
```
    url:'https://b.17doubao.com/examExternal/getPersonMess.do',
    type:POST,
    data:{
       uid:Long   // 用户id
    },
    return {
        [{
          "pname";"个人姓名",
          "mobile";"手机号",
          "email";"邮箱",
          "sex";"性别",
          "birthday";"生日",
          "workaddress";"工作地点",
          "homeaddress";"家庭住址",
          "healthdegree";"健康状况",
          "identitytype";"个人信息标志位 个人信息 只能取这个值为1的"
        }]
    }
 
```

## 第三方接口根据openId获取Uid api 
```
    url:'https://b.17doubao.com/wxExternal/findWxFUid',
    type:POST,
    data:{
       openId:String   // 用户id
    },
    return {
             "success": true,
             "code": 200,
             "msg": null,
             "data": uid
           }
           
    
    
   
 
```

## 第三方接口获取个人头像信息 api 
```
    url:'https://b.17doubao.com/examExternal/getPersonalUserAccount.do',
    type:POST,
    data:{
       uid:Long   // 用户id
    },
    return {
             "success": true,
             "code": 200,
             "msg": null,
             "data": {"id":311,
                   "pwd":"9d4b56b508549343643c049444f821f9",
                   "salt":"YyFD6Ehe",
                   "nickname":"dctxf",
                   "idcardtype":1,
                   "idcard":"410182198910106596",
                   "icon":"/upload/down/person/img/2015122114/311_fe9c7f57-bb34-495f-a7c0-f2adb5807f00",
                   "status":1,
                   "isfirstlogin":0,
                   "ct":"Sep 29, 2015 3:56:46 PM",
                   "ut":"May 17, 2016 4:55:19 PM",
                   "mobileLogintime":"Jan 13, 2017 10:50:05 AM",
                   "pcLogintime":"Oct 18, 2016 10:02:57 AM",
                   "logintime":"Jan 13, 2017 10:50:05 AM",
                   "loginip":-782390518,
                   "logincount":25,
                   "pname":"吕晓峰",
                   "inviteuser":0
                 }
           }
           
    
    
   
 
```

11/10
详细信息
    String pname;   姓名
    Byte sex;       性别
    String mobile;  手机
    String email;   邮箱
    String homeaddress;     家庭住址
    Date birthday;  生日
                        银行卡号
    String workaddress; 工作地址

    String qq;

    String weixin;

    标签（多选）

    3个按钮  并且能弹出模态框  理赔 合同 订单

    新增快速回复
    
    
## 我要报案接口
- 根据用户id查找报案
接口: api/robot/{uid}

- 根据手机号查找报案
接口: api/robot/user/{phone}

返回结果
```
{
"success": true,
"code": 0,
"data": [
    {
        "id": 52,
        "uid": 73849,
        "userName": "18511072313",
        "loginUserName": "18511072313",
        "nickName": "胡波",
        "openId": "orT5yw4pTFHOVv3vj7p3quEBpWSA",
        "robotImages": [//报案上传的N张图片
            {
                "imgPath": "https://test.17doubao.com/upload/xmppDownLoad/downLoad?key=chat/image/2017082417/orT5yw4pTFHOVv3vj7p3quEBpWSA__wxId_gcuEYjx-qUpk9p187Gxs7Pa-YO8eyHXcVdwFCef0r33JTV1-x9Enf3-j_BjPm8Fj.png",
                "uploadTime": "Aug 24, 2017 5:01:10 PM"
            },
            {
                "imgPath": "https://test.17doubao.com/upload/xmppDownLoad/downLoad?key=chat/image/2017082417/orT5yw4pTFHOVv3vj7p3quEBpWSA__wxId_XN1Eej-yi3NDZkqYS8VF8OQ5MtqPeVt4YET60f9ZCF7ol45VVCmkYrvPIibZ5pmL.png",
                "uploadTime": "Aug 24, 2017 5:01:20 PM"
            }
        ],
        "contentType": "image",
        "serialNumber": "AD6997BA4D764ACF8C66D29F60CDB6B8",
        "state": 2,
        "remark": "",
        "createTime": "Aug 24, 2017 5:01:10 PM",
        "updateTime": "Aug 24, 2017 5:01:30 PM"
    }
}
}    
```


分页查询机器人报案图片数据

接口: 
- api/robot/repotcases?page=1&count=10&beginTime=2017-08-24 00:25:42&endTime=2017-08-24 23:25:42

参数:
- page  页码  (默认0)
- count 每页条数 (默认10)
- beginTime 开始日期 (eg: 2017-08-24 00:25:42)
- endTime 结束日期   (eg: 2017-08-24 23:59:59)

dto类:
- [RobotDto](https://github.com/liaowuhen88/wxmpp/blob/master/src/main/java/com/baodanyun/robot/dto/RobotDto.java)   
- [RobotImages](https://github.com/liaowuhen88/wxmpp/blob/master/src/main/java/com/baodanyun/robot/dto/RobotImages.java)

返回示例

- 异常情况结果如下:
{"success":false,"code":0,"msg":"endTime日期格式不合法"}

- 正常结果如下:
```java

{
"success": true,
"code": 0,
"data": {
    "total": 9,//总页数
    "count": 10, //每页条数
    "page": 1,页码
    "list": [
        {
            "id": 52,
            "uid": 73849,  //用户id
            "userName": "18511072313",  //用户电话
            "loginUserName": "18511072313",
            "nickName": "胡g",
            "openId": "orT5yw4pTFHOVv3vj7p3quEBpWSA",
            "robotImages": [//报案上传的N张图片
                {
                    "imgPath": "https://test.17doubao.com/upload/xmppDownLoad/downLoad?key=chat/image/2017082417/orT5yw4pTFHOVv3vj7p3quEBpWSA__wxId_XN1Eej-yi3NDZkqYS8VF8OQ5MtqPeVt4YET60f9ZCF7ol45VVCmkYrvPIibZ5pmL.png",
                    "uploadTime": "Aug 24, 2017 5:01:20 PM"
                },
                {
                    "imgPath": "https://test.17doubao.com/upload/xmppDownLoad/downLoad?key=chat/image/2017082417/orT5yw4pTFHOVv3vj7p3quEBpWSA__wxId_gcuEYjx-qUpk9p187Gxs7Pa-YO8eyHXcVdwFCef0r33JTV1-x9Enf3-j_BjPm8Fj.png",
                    "uploadTime": "Aug 24, 2017 5:01:10 PM"
                }
            ],
            "contentType": "image",
            "serialNumber": "AD6997BA4D764ACF8C66D29F60CDB6B8",//批次号
            "state": 2,
            "remark": "",
            "createTime": "Aug 24, 2017 5:01:10 PM", 
            "updateTime": "Aug 24, 2017 5:01:30 PM"
        },
        {
            "id": 49,
            "uid": 73849,
            "userName": "18511072313",
            "loginUserName": "18511072313",
            "nickName": "胡g",
            "openId": "orT5yw4pTFHOVv3vj7p3quEBpWSA",
            "robotImages": [
                {
                    "imgPath": "https://test.17doubao.com/upload/xmppDownLoad/downLoad?key=chat/image/2017082415/orT5yw4pTFHOVv3vj7p3quEBpWSA__wxId_Wfl5z5zxL8pNkSUfw95XkHxGPVqy-ZaFTdRPvtfVyszeDk6PDE5Cde3Kh4l4mabn.png",
                    "uploadTime": "Aug 24, 2017 3:23:56 PM"
                }
            ],
            "contentType": "image",
            "serialNumber": "6C2009E2DFD345A3B0F13AE04608EACD",
            "state": 2,
            "remark": "",
            "createTime": "Aug 24, 2017 3:23:56 PM",
            "updateTime": "Aug 24, 2017 3:23:59 PM"
        },
        {
            "id": 42,
            "uid": 73849,
            "userName": "18511072313",
            "loginUserName": "18511072313",
            "nickName": "胡g",
            "openId": "orT5yw4pTFHOVv3vj7p3quEBpWSA",
            "robotImages": [
                {
                    "imgPath": "https://test.17doubao.com/upload/xmppDownLoad/downLoad?key=chat/image/2017082415/orT5yw4pTFHOVv3vj7p3quEBpWSA__wxId_kRkVLCU8PIhF9pzxMfF7KNVz8aRM95eGM6FCEB8GqdMjkUeU2LSuPqAJSCvObs3X.png",
                    "uploadTime": "Aug 24, 2017 3:23:03 PM"
                },
                {
                    "imgPath": "https://test.17doubao.com/upload/xmppDownLoad/downLoad?key=chat/image/2017082415/orT5yw4pTFHOVv3vj7p3quEBpWSA__wxId_DCQHssQpTbWxDiJOdHX-ihex7bWKYhwZdbDRTOtzhHE_1pOiy_cwDXIRInYW8eHr.png",
                    "uploadTime": "Aug 24, 2017 3:23:03 PM"
                },
                {
                    "imgPath": "https://test.17doubao.com/upload/xmppDownLoad/downLoad?key=chat/image/2017082415/orT5yw4pTFHOVv3vj7p3quEBpWSA__wxId_EkZ-CloRHQ5QzgrmdCfW7JB0hPhDx1Ff3Zb7ZmUZsZ2v048-logyUNYPfBFBsq-G.png",
                    "uploadTime": "Aug 24, 2017 3:23:03 PM"
                },
                {
                    "imgPath": "https://test.17doubao.com/upload/xmppDownLoad/downLoad?key=chat/image/2017082415/orT5yw4pTFHOVv3vj7p3quEBpWSA__wxId_eMzNK1riD4em-F7yXuDN2G36IzPJTexMJlprAvdWuHefYeh7rnCu-pMhkH1vaq6q.png",
                    "uploadTime": "Aug 24, 2017 3:23:03 PM"
                },
                {
                    "imgPath": "https://test.17doubao.com/upload/xmppDownLoad/downLoad?key=chat/image/2017082415/orT5yw4pTFHOVv3vj7p3quEBpWSA__wxId_8LJsUyohLQmMJQuDtkTtmiq-myPj2CeFBT0jzFOkL2acjUQa4mbGodBz8jIaBEoi.png",
                    "uploadTime": "Aug 24, 2017 3:23:02 PM"
                }
            ],
            "contentType": "image",
            "serialNumber": "0C865EA744444D55A381DC34821994BF",
            "state": 2,
            "remark": "",
            "createTime": "Aug 24, 2017 3:23:02 PM",
            "updateTime": "Aug 24, 2017 3:23:07 PM"
        },
        {
            "id": 37,
            "uid": 73849,
            "userName": "18511072313",
            "loginUserName": "18511072313",
            "nickName": "胡g",
            "openId": "orT5yw4pTFHOVv3vj7p3quEBpWSA",
            "robotImages": [
                {
                    "imgPath": "https://test.17doubao.com/upload/xmppDownLoad/downLoad?key=chat/image/2017082415/orT5yw4pTFHOVv3vj7p3quEBpWSA__wxId_fl0YkL-TeTxqHcb_mlvWggZ1xwf7umsGcPteMWYZMnv4hYx5wpPBloGnBnC1xqFH.png",
                    "uploadTime": "Aug 24, 2017 3:22:32 PM"
                },
                {
                    "imgPath": "https://test.17doubao.com/upload/xmppDownLoad/downLoad?key=chat/image/2017082415/orT5yw4pTFHOVv3vj7p3quEBpWSA__wxId_CurDrh0inrRMqH0BOm1vooLWvwXaWoue82t-1iBEDgbwG-YWKa5a9jHR3XpKa-C-.png",
                    "uploadTime": "Aug 24, 2017 3:22:31 PM"
                },
                {
                    "imgPath": "https://test.17doubao.com/upload/xmppDownLoad/downLoad?key=chat/image/2017082415/orT5yw4pTFHOVv3vj7p3quEBpWSA__wxId_yZZ-hDitrh1G1vBUhnx85SRGZ1jFPTXOEkYm_63JYpWO3KD3K2C85Tlv_d042Efc.png",
                    "uploadTime": "Aug 24, 2017 3:22:31 PM"
                }
            ],
            "contentType": "image",
            "serialNumber": "ABCAD9B2EDF04A67AFCF0662E14D44C5",
            "state": 2,
            "remark": "",
            "createTime": "Aug 24, 2017 3:22:31 PM",
            "updateTime": "Aug 24, 2017 3:22:34 PM"
        }
    ]
}
}

```