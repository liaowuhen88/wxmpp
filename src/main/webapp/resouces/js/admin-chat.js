/**
 * xChat 事件重写
 */

//当前打开的窗口,值为当前菜单的id
var destJid;
var from_icon;
var customerInfo = {};
var base = window.base;
var xchat = new xChat({
    url: base + '/sockjs/customer/chat',
    ut: 'customer',
    lu: window.currentId,
    ic: customerInfo.icon,
    nk: customerInfo.userName
});

// 统计有多少用户未回
xchat.recvMsg = [];
// 统计一个用户有多少消息未读
xchat.recvMsgOne = {};

//控件
xchat.controls = {
    chatShow: 'chatShow',
    chatTimeline: 'chatTimeline',
    holdListCont: '#holdListCont',
    defaultAvatar: window.base + '/resouces/images/default-avatar.jpg',
    default_visitor_icon: window.base + '/resouces/images/user_photo_default.jpg',
    default_kf_icon: window.base + '/resouces/images/kf.png',
    friendList: '#friendList',
    historyFriendList: '#historyFriendList',
    backupfriendList: '#backupfriendList',
    changeOfflineFriendList: '#changeOfflineFriendList',
    msgContainer: '#chatMsgContainer',
    turnList: 'turn-list',
    userTags: '#userTags',
    remark: '#remark',
    remarkSubmit: '#remarkSubmit',
    currentChatId: '#currentChatId',
    // 理赔
    claimsContainer: '#claimsContainer',
    //合同
    contractsContainer: '#contractsContainer',
    //订单
    orderContainer: '#orderContainer',

    waitReplyPerson: '#waitReplyPerson'
};
/*-------------------------------------------------------------*/
/*-------------------------------------------------------------*/
/*-------------------------------------------------------------*/
/*------------------------------接口---------------------------*/
/*-------------------------------------------------------------*/
/*-------------------------------------------------------------*/
/*-------------------------------------------------------------*/

xchat.interface = {
    holdList: base + '/api/queue/2',
    bindCustomer: base + '/api/bindCustomer',
    weiXinVisitorList: base + '/api/weiXinVisitorList',
    callIn: base + '/api/visitorUp/',
    offConnect: base + '/api/visitorOff/',
    login: base + '/customerlogin',
    loadChatList: base + '/api/queue/1',
    userInfo: base + '/api/visitorDetail',
    getCustomerList: base + '/api/onlineCustomerList',
    turn: base + '/api/changeVisitorTo',
    updateUserInfo: base + '/api/upVisitorInfo',
    getTagsAll: base + '/api/getTagsAll',
    changeProfile: base + '/api/upCustomerInfo',
    closeFriendWindow: base + '/api/closeFriendWindow',

};
/*=====================================================================================初始化=====================================================================================*/
//登录成功
xchat.loginSuccessStatusHandelEvent = function () {
    this.setCustomerProfileEventBind();     //设置客服信息
    this.sendImgEventBind();    //初始化图片发送
};
//初始化成功
xchat.initSuccessQueueStatusHandelEvent = function () {
    this.loadChatList();    //对话列表加载
    this.loadChatListEvent();   //对话列表事件绑定
    this.sendEventBind();   //发送消息事件绑定
    // 因为本地缓存和数据库消息对不上，所以这里去除
    // this.loadHistoryEventBind();    //加载历史消息事件绑定
    this.messageListEventBind();    //快速回复事件绑定
    this.holdListEventBind();    //接入列表事件绑定
    this.holdListWeiXInEventBind();   //微信接入绑定
    this.closeEventBind();  //关闭对话事件绑定
    this.callInEventBind(); //接入对话事件绑定
    this.turnEventBind();   //转接事件绑定
    this.getCustomerList();     //获取客服列表
    this.setUserInfoEventBind();    //设置用户详情事件绑定
    this.customerListEventBind();//转接按钮事件


};
//初始化失败
xchat.initErrorStatusHandelEvent = function () {
    window.location.href = window.base + "/customer/chat";
};

// 消息发送失败
xchat.msgFailEvent = function () {
    alert("消息发送失败,您已掉线，请退出重新登录");
};

//登录失败
xchat.loginErrorStatusHandelEvent = function () {
    window.location.href = window.base + "/customerlogin";
};
//客服个人信息设置
xchat.setCustomerProfileEventBind = function () {
    myUtils.load(window.base + "/api/customer/" + window.currentId, 'get', function (customer) {
        var $customer = $('#customerInfo').find('img').eq(0);
        customerInfo = customer.data;
        if (customerInfo.icon !== null && customerInfo.icon !== '' && customerInfo.icon !== 'ic') {
            $customer.attr('src', customerInfo.icon);
        }
        $customer.attr('alt', customerInfo.username);
    }, {});
};
/*=====================================================================================初始化=====================================================================================*/
/*=====================================================================================断开连接=====================================================================================*/
//关闭当前对话
xchat.closeEventBind = function () {
    var _this = this;
    $("#offSession").on('click', function () {
        myUtils.load(_this.interface.offConnect + destJid, 'post', function () {
            $(document.getElementById(destJid)).remove();
            _this.closeFriendWindow();
        }, {})
    });
};
//关闭当前窗口
xchat.closeFriendWindow = function () {
    console.log(destJid);
    $.ajax({
        url: this.interface.closeFriendWindow + '?jid=' + destJid,
        type: 'POST',
        success: function () {
            destJid = "";
            $("#currentChatId").empty();
            $("#hasChat").hide();
            $(".chat-detail").hide();
            $("#noChat").show();
        },
        error: function () {
            alert('关闭窗口失败，请重试');
        }
    });
};
/*=====================================================================================断开连接=====================================================================================*/
/*=====================================================================================接收消息=====================================================================================*/

//接收到消息
xchat.recvMsgEvent = function (json) {
    // 是否打开当前窗口
    console.log(json)
    if (json.from != window.destJid) {
        // 未打开当前窗口
        if (jQuery.inArray(json.from, this.recvMsg) == -1) {
            this.recvMsg.push(json.from);
            // alert(this.recvMsg.length);
            $(this.controls.waitReplyPerson).html(this.recvMsg.length);
        }

        // 新消息移动到表头
        $('#friendList').find('li').each(function () {
            if ($(this).attr('id') === json.from) {
                $('#friendList').prepend($(this));
            }
        });

        // 单个用户
        $(document.getElementById('m' + json.from)).attr("class", "new-message");
        var from = json.from;
        var count = xchat.recvMsgOne[from];
        if (count) {
            count = count + 1;
        } else {
            count = 1;
        }
        xchat.recvMsgOne[from] = count;
        $(document.getElementById('m' + from)).html(count);

        if (json.source && json.source == 1) {//消息来源是微信则绿色背景
            $(document.getElementById('m' + from)).css('background', 'green');
        }

    }
};


//接收到文本消息
xchat.recvTextMsgHandelEvent = function (json) {
    json.content = wechatFace.faceToHTML(json.content, window.base); //表情字符转换对象的图片

    document.getElementById("msgTipAudio").play();
    json.time = myUtils.formatDate(new Date(json.ct));


    if ("synchronize" == json.msgType) {
        json.src = json.to;
        json.icon = json.icon || this.controls.default_kf_icon;
    } else {
        json.icon = json.icon || this.controls.default_visitor_icon;
        json.src = json.from;
    }

    if (json.from == window.destJid) {
        myUtils.renderDivAdd('mleft', json, 'chatMsgContainer');
    } else if (json.to == window.destJid) {
        myUtils.renderDivAdd('mright', json, 'chatMsgContainer');
    }
    myUtils.storage(json);
    xchat.goBottom();
};
//接收到图片消息
xchat.recvImageMsgHandelEvent = function (json) {
    document.getElementById("msgTipAudio").play();
    json.time = myUtils.formatDate(new Date(json.ct));
    json.icon = json.icon || this.controls.default_visitor_icon;
    json.src = json.from;
    if (json.from == window.destJid) {
        myUtils.renderDivAdd('imgLeft', json, 'chatMsgContainer');
    }

    myUtils.storage(json);
    xchat.goBottom();
};

//接收到音频信息
xchat.recvAudioMsgHandelEvent = function (json) {
    document.getElementById("msgTipAudio").play();

    json.time = myUtils.formatDate(new Date(json.ct));
    json.icon = json.icon || this.controls.default_visitor_icon;
    json.src = json.from;
    if (json.from == window.destJid) {
        myUtils.renderDivAdd('audioLeft', json, 'chatMsgContainer');
    }

    myUtils.storage(json);
    xchat.goBottom();
};


/*=====================================================================================接收消息=====================================================================================*/
/*=====================================================================================发送信息=====================================================================================*/
xchat.sendImgEventBind = function () {
    this.imageSendInit(
        "imgUploader",
        "/up/customer/img/" + window.currentId + "?name=123123.png",
        function (o) {
            o.removeClass("sending").addClass("timeOut");
        }
    );
};
//发送信息
xchat.sendEvent = function (msg) {
    if (msg) {
        $("#enterChat").val("");
        var icon;
        if (window.user) {
            icon = window.user.icon;
        }
        xchat.normalSend({
            "content": msg,
            "to": window.destJid,
            "icon": icon || this.controls.default_kf_icon,
            "from": window.currentId,
            "timeOutCall": function (o) {
                o.removeClass("sending").addClass("timeOut");
            }
        });
        xchat.goBottom();
    }
};
//发送消息事件
xchat.sendMsgHandelEvent = function (data) {
    data.time = myUtils.formatDate(new Date(data.ct));
    data.src = data.to;
    var icon;
    if (window.user) {
        icon = window.user.icon;
    }
    data.icon = icon || this.controls.default_kf_icon;
    myUtils.storage(data);

    if (data.contentType == 'image') {
        myUtils.renderDivAdd('imgRight', data, 'chatMsgContainer');
    } else {
        myUtils.renderDivAdd('mright', data, 'chatMsgContainer');
    }
};
xchat.sendEventBind = function () {
    var _this = this;
    //回车发送消息
    $(document).bind('keydown', '#enterChat', function (e) {
        if (e.keyCode == 13) {
            var msg = $("#enterChat").val();
            if (msg !== '') {
                xchat.sendEvent(msg);
                return false;
            } else {
                return false;
            }
        }
    });
    //点击按钮发送
    $("#send-btn").bind("click", function () {
        var msg = $("#enterChat").val();
        _this.sendEvent(msg);
    });
};
/*=====================================================================================发送信息=====================================================================================*/
/*=====================================================================================队列事件=====================================================================================*/
//线上队列
xchat.onlineQueueSuccessStatusHandelEvent = function (json) {
    var _this = this;
    //myUtils.renderQueue(json.from, 'waitfriendList', 'down');
    //myUtils.renderQueue(json.from, 'backupfriendList', 'down');
    //myUtils.renderQueue(json.from, 'friendList', 'down');
    myUtils.renderQueue(json.from, 'historyFriendList', 'remove');
    myUtils.renderQueue(json.from, 'friendList', 'up', function () {
        json.name = json.fromName;
        json.nickname = json.fromName;
        json.icon = json.icon || _this.controls.default_visitor_icon;
        json.onlineStatus = 'online';
        json.time = myUtils.formatDate(json.loginTime);
        myUtils.renderDivPrepend('onlinefriendListTpl', json, 'friendList');
    })
};
//等待队列
xchat.waitQueueSuccessStatusHandelEvent = function (json) {
    myUtils.renderQueue(json.from, 'waitfriendList', 'down');
    myUtils.renderQueue(json.from, 'backupfriendList', 'down');
    myUtils.renderQueue(json.from, 'historyFriendList', 'down');
    myUtils.renderQueue(json.from, 'friendList', 'down');
    //如果有等待的 增加样式
    $("#holdListBtn").addClass("hasJoined");
};
//等待队列中删除
xchat.offlineWaitQueueStatusHandelEvent = function (json) {

    myUtils.renderQueue(json.from, 'waitfriendList', 'down');
    myUtils.renderQueue(json.from, 'backupfriendList', 'down');
    myUtils.renderQueue(json.from, 'historyFriendList', 'down');
    myUtils.renderQueue(json.from, 'friendList', 'down');
};

//客服下线后调用
xchat.customerOfflineStatusHandelEvent = function () {
    this.alertShow('客服已经下线,消息将以离线方式发送');
    this.alertMiss();
    this.leaveMessageShow();
};
//用户下线
xchat.offlineStatusHandelEvent = function (json) {
    var _this = this;
    myUtils.renderQueue(json.from, 'friendList', 'remove');
    myUtils.renderQueue(json.from, 'historyFriendList', 'down', function () {
        json.name = json.fromName;
        json.nickname = json.fromName;
        json.icon = json.icon || _this.controls.default_visitor_icon;
        json.onlineStatus = 'history';
        json.time = myUtils.formatDate(json.loginTime);
        myUtils.renderDivPrepend('onlinefriendListTpl', json, 'historyFriendList');
    })
};


xchat.changeOfflineStatusHandelEvent = function (json) {
    var _this = this;
    myUtils.renderQueue(json.from, 'friendList', 'remove');
    myUtils.renderQueue(json.from, 'historyFriendList', 'down', function () {
        json.name = json.fromName;
        json.nickname = json.fromName;
        json.icon = json.icon || _this.controls.default_visitor_icon;
        json.onlineStatus = 'history';
        json.time = myUtils.formatDate(json.loginTime);
        myUtils.renderDivPrepend('onlinefriendListTpl', json, 'changeOfflineFriendList');
    })
};


//在backqueue下线
xchat.offlineBackQueueStatusHandelEvent = function (json) {
    myUtils.renderQueue(json.from, 'waitfriendList', 'down');
    myUtils.renderQueue(json.from, 'historyFriendList', 'down');

    myUtils.renderQueue(json.from, 'backupfriendList', 'down');
    myUtils.renderQueue(json.from, 'friendList', 'down');
};
//backup队列
xchat.backUpStatusHandelEvent = function (json) {
    var _this = this;
    myUtils.renderQueue(json.from, 'waitfriendList', 'down');
    myUtils.renderQueue(json.from, 'backupfriendList', 'down');
    myUtils.renderQueue(json.from, 'friendList', 'down');
    myUtils.renderQueue(json.from, 'backupfriendList', 'up', function () {
        json.name = json.fromName;
        json.nickname = json.fromName;
        json.icon = json.icon || _this.controls.default_visitor_icon;
        json.onlineStatus = 'online';
        myUtils.renderDivAdd('backupfriendListTpl', json, 'backupfriendList');
    })
};
//对话队列加载
xchat.loadChatList = function () {
    var _this = this;
    var friendList = $(_this.controls.friendList);
    $.get(_this.interface.loadChatList, {}, function (json) {
        if (json.success) {
            if (json.data) {
                for (var i = 0; i < json.data.length; i++) {
                    var friend = json.data[i];
                    friend.from = friend.id;
                    if (friend.icon === undefined || friend.icon === '') {
                        friend.icon = _this.controls.default_visitor_icon;
                    }
                    friend.time = myUtils.formatDate(friend.loginTime);
                    if (friend.onlineStatus == 'online') {
                        myUtils.renderDivAdd('onlinefriendListTpl', friend, 'friendList');
                    } else if (friend.onlineStatus == 'backup') {
                        //backup状态也算是线上状态
                        myUtils.renderDivAdd('backupfriendListTpl', friend, 'backupFriendList');
                    } else if (friend.onlineStatus == 'history') {
                        myUtils.renderDivAdd('onlinefriendListTpl', friend, 'historyFriendList');
                    } else if (friend.onlineStatus == 'changeOffline') {
                        myUtils.renderDivAdd('onlinefriendListTpl', friend, 'changeOfflineFriendList');
                    }

                    var count = xchat.recvMsgOne[friend.from];
                    $(document.getElementById('m' + friend.from)).html(count);

                }
            }
        }
    })
};
//对话队列事件绑定
xchat.loadChatListEvent = function () {
    var _this = this;
    $(_this.controls.friendList).on("click", 'li', function () {
        $(_this.controls.backupfriendList).find("li").removeClass("active");
        var myFriendId = $(this).attr("id");
        var openId = $(this).attr("openId");
        var nickname = $(this).find('.name').text();
        var icon = $(this).find('img').attr("src");
        var isOnline = $(this).attr("class").indexOf("online");

        _this.openFriendWindow(isOnline, myFriendId, nickname, openId, icon);
        $(this).addClass("active");
    });

    $(this.controls.backupfriendList).on("click", 'li', function () {
        $(_this.controls.friendList).find("li").removeClass("active");
        var myFriendId = $(this).attr("id");
        var openId = $(this).attr("openId");
        var nickname = $(this).find('.name').text();
        var isOnline = $(this).attr("class").indexOf("online");
        xchat.openFriendWindow(isOnline, myFriendId, nickname, openId);
        $(this).addClass("active");
    });

    $(this.controls.historyFriendList).on("click", 'li', function () {
        $(_this.controls.friendList).find("li").removeClass("active");
        var myFriendId = $(this).attr("id");
        var openId = $(this).attr("openId");
        var nickname = $(this).find('.name').text();
        var isOnline = $(this).attr("class").indexOf("online");
        xchat.openFriendWindow(isOnline, myFriendId, nickname, openId);
        $(this).addClass("active");
    });

    $(this.controls.changeOfflineFriendList).on("click", 'li', function () {
        $(_this.controls.changeOfflineFriendList).find("li").removeClass("active");
        var myFriendId = $(this).attr("id");
        var openId = $(this).attr("openId");
        var nickname = $(this).find('.name').text();
        xchat.openFriendWindow("changeOffline", myFriendId, nickname, openId);
        $(this).addClass("active");
    });


    $(".chat-source-detail-btn").click(function () {
        _this.visitorProperties();
    });

};
/*=====================================================================================队列事件=====================================================================================*/
//ws关闭后的处理方式
xchat.wsClose = function () {
    window.location.href = this.interface.login;
};
//本地缓存历史数据
xchat.getLocalHistory = function (id) {
    var _this = this;
    var dataList = myUtils.getStorage(id);
    if (dataList) {
        dataList = eval("(" + dataList + ")");
        dataList.map(function (val) {

            if (val.from == window.destJid) {
                val.icon = val.icon || _this.controls.defaultAvatar;
            } else if (val.to == window.destJid) {
                val.icon = val.icon || _this.controls.default_kf_icon;
            }
        });
        myUtils.cacheRenderDiv(window.currentId, dataList, 'chatMsgContainer', function () {
            _this.goBottom();
        });
    }
};
xchat.getRemoteHistory = function (customerPage, cacheLastId, fn) {
    myUtils.customerHistory(customerPage, cacheLastId, fn);
};
//滚动加载历史消息
xchat.loadHistoryEventBind = function () {
    var _this = this;
    var i = 0;
    var customerPage = new myUtils.Page({
        pageNumber: 0,
        pageSize: 10
    });

    $('#chatTimeline').on('mousewheel', function (e) {
        var delta = -e.originalEvent.wheelDelta || e.originalEvent.detail;
        if (delta > 0) {
            i--;
        }
        //上滚
        if (delta < 0) {
            i++;
        }
        if (this.scrollTop === 0 && i >= 2) {
            var currentHeight = this.scrollHeight;

            var timer = setTimeout(function () {
                i = 0;
                clearTimeout(timer);
            }, 300);

            // 获取最早的本地缓存信息id
            var dataList = myUtils.getStorage(window.destJid);
            var cacheLastId;
            if (dataList) {
                dataList = eval("(" + dataList + ")");
                cacheLastId = dataList[0].id;
            }
            _this.getRemoteHistory(customerPage, cacheLastId, function () {
                this.scrollTop = this.scrollHeight - currentHeight;
            });
            i = 0;
        }
    });
};
//获取当前访客的浏览器信息
xchat.visitorProperties = function () {
    myUtils.load(window.base + "/api/propertiesApi?destJid=" + destJid, "get", function (json) {
        if (json.success) {
            var data = json.data;
            if (data) {
                for (var i = 0; i < data.length; i++) {
                    if (data[i].key == '登录时间' || data[i].key == '上次登出时间') {
                        data[i].value = myUtils.formatDate(new Date(parseInt(data[i].value)));
                    }
                }
                data.push({
                    key: '访问来源',
                    value: myUtils.isPc()
                });
                myUtils.renderDiv('visitorPropertiesTpl', data, 'visitorProperties');
            }
        }
    }, {});
};
/*=====================================================================================打开对话=====================================================================================*/
//打开对话的窗口
xchat.openFriendWindow = function (isOnline, id, nickname, openId, icon) {
    var _this = this;
    window.destJid = id;
    window.from_icon = icon;
    //$(id).addClass("active").siblings().removeClass("active");  //设置当前的好友为激活 且 把消息变成已阅读
    $("#currentChatId").empty().append("您正在和" + nickname + "聊天 ").data('id', id); //设置聊天标题
    $("#showAllHistory").attr('value', id);
    $("#showAllHistory").attr('nickName', nickname);

    $("#noChat").hide();  //中间位置消失
    $("#hasChat").show();   //把聊天窗口显示出来
    $(".chat-detail").show();   //把用户详情显示出来
    $(_this.controls.msgContainer).empty(); //清空当前的聊天容器内容
    // 判断是否在线，是否开启聊天窗口
    if (isOnline != '-1') {
        $('#chatInput').removeClass('chat-input-disabled');
    } else {
        $('#chatInput').addClass('chat-input-disabled');
    }

    _this.getLocalHistory(id);
    $(document.getElementById('m' + id)).attr("class", "");     //清空有新消息的提示
    this.recvMsgOne[id] = 0;
    $(document.getElementById('m' + id)).html("");


    // 去除提示
    this.recvMsg.splice(jQuery.inArray(id, this.recvMsg), 1);
    $(this.controls.waitReplyPerson).html(this.recvMsg.length);

    _this.getUserInfo(window.currentId, id, openId);
    _this.getUserLabel();
};
//获取当前用户的详情
xchat.getUserInfo = function (currentId, destJid, openId) {
    var _this = this;
    $("#userDetail").empty();
    $(_this.controls.claimsContainer).empty();
    $(_this.controls.contractsContainer).empty();
    $(_this.controls.orderContainer).empty();


    $.ajax({
        url: _this.interface.userInfo + '?id=' + destJid + '&openid=' + openId,
        type: 'GET',
        success: function (res) {
            if (res.success) {
                var data = res.data;
                if (data) {
                    var basic = data.basic;
                    var vCard = data.vCard;
                    if (basic) {
                        var personalInfo = basic.personalInfo;
                        var claims = basic.claimsInfos;
                        var company = basic.company;
                        var order = basic.orderInfos;
                        var contract = basic.contractInfos;
                        if (personalInfo) {
                            switch (personalInfo.sex) {
                                case 1:
                                    personalInfo.sex = '男';
                                    break;
                                case 2:
                                    personalInfo.sex = '女';
                                    break;
                                default:
                                    personalInfo.sex = '保密';
                            }

                            if (personalInfo.birthday) {
                                personalInfo.birthday = myUtils.formatDate(personalInfo.birthday, 'yyyy-MM-dd');
                            }
                            switch (personalInfo.idcardtype) {
                                case 1:
                                    personalInfo.idcardtype = '身份证';
                                    break;
                                case 2:
                                    personalInfo.idcardtype = '其他证件';
                                    break;
                                default:
                                    personalInfo.idcardtype = '其他证件';
                            }
                        }

                        if (company && company[0]) {
                            personalInfo.company = company[0].ename;
                        }

                        $('#userDetail').empty();
                        myUtils.renderDivAdd('visitorDetail', personalInfo, 'userDetail');


                        if (claims) {
                            _this.claimsComb(claims);                //理赔
                        }

                        if (order) {
                            _this.orderComb(order);           //订单
                        }

                        if (contract) {
                            _this.contractComb(contract);   //合同
                        }
                    }

                    if (vCard) {
                        var tags = vCard.tags;
                        var remark = vCard.remark;
                        _this.setUserLabel(tags);
                        $(_this.controls.remark).val(remark);
                    }
                }
            }
        },
        error: function () {
            $('#userDetail').html('获取用户数据失败');
        }
    });
};
//设置当前用户详情
xchat.setUserInfo = function (cjid, remark, tags) {
    var _this = this;
    remark = remark || '';
    //tags = tags || [];
    $.ajax({
        url: _this.interface.updateUserInfo + '?cjid=' + cjid + '&remark=' + remark + '&tags=' + JSON.stringify(tags),
        type: 'POST',
        success: function (res) {
            if (res.success) {
                alert('修改成功');
            }
        },
        error: function () {
            alert('修改失败');
        }
    });
};
//获取用户标签
xchat.getUserLabel = function () {
    var _this = this;
    $.ajax({
        url: _this.interface.getTagsAll,
        type: 'GET',
        timeout: 3000,
        success: function (res) {
            if (res.success) {
                _this.userLabelComb(res.data);
            }
        },
        error: function () {
            $(_this.controls.userTags).html('获取标签失败');
        }
    });
};
//用户标签拼接
xchat.userLabelComb = function (data) {
    var _this = this;
    var html = '';
    data.map(function (val, index) {
        html += '<li class="tag"><input type="checkbox" name="tags" id="tag_' + val.id + '"><label for="tag_' + val.id + '" data-id="' + val.id + '">' + val.tagname + '</label></li>';
    });
    $(_this.controls.userTags).html(html);
    return html;
};
//用户标签设置
xchat.setUserLabel = function (data) {
    if (data) {
        data.map(function (val) {
            if (val.state) {
                $('#tag_' + val.id).prop('checked', true);
            }
        });
    }
};
//设置用户详情事件绑定
xchat.setUserInfoEventBind = function () {
    var _this = this;

    $(this.controls.remarkSubmit).on('click', function () {
        var cjid = $(_this.controls.currentChatId).data('id');
        var remark = $(_this.controls.remark).val();
        _this.setUserInfo(cjid, remark, []);
    });
    /*$(this.controls.remark).on('focusout', function () {
     var cjid = $(_this.controls.currentChatId).data('id');
     var remark = $(this).val();
     _this.setUserInfo(cjid, remark, []);
     });*/
    $(this.controls.userTags).on('click', 'input', function (e) {
        var cjid = $(_this.controls.currentChatId).data('id');
        var tags = [];
        var $tags = $('[name="tags"]');
        $tags.map(function (index, val) {
            var id = $(val).attr('id');
            var obj = {
                id: id.replace('tag_', ''),
                name: $(val).next('label').text(),
                state: $('#' + id).prop('checked')
            };
            tags.push(obj);
        });
        _this.setUserInfo(cjid, '', tags);
        e.stopPropagation();
    });
};
//理赔拼接
xchat.claimsComb = function (data) {
    //var tabHeader = '<tr><th>理赔单号</th><th>承保公司</th><th>申请人姓名</th><th>受益人姓名</th><th>受益人证件号</th><th>申请时间</th><th>完结时间</th><th>申请金额</th><th>理赔状态</th><th>处理状态</th></tr>';
    //var tabBody = '';
    var html = '';
    var itemHtml = '';
    data.map(function (val, index) {
        //tabBody += '<tr><td>' + val.applycode + '</td><td>' + val.insuranceCompany + '</td><td>' + val.applyPerson + '</td><td>' + val.dangerPerson + '</td><td>' + val.dangerIdCard + '</td><td>' + val.applyDate + '</td><td>' + val.doneTime + '</td><td>' + val.chargeMoney + '</td><td>' + val.claimsStatus + '</td><td>' + val.handleStatus + '</td></tr>';
        if (val.applyDate) {
            val.applyDate = myUtils.formatDate(val.applyDate, 'yyyy-MM-dd');
        } else {
            val.applyDate = '';
        }
        if (val.doneTime) {
            val.doneTime = myUtils.formatDate(val.doneTime, 'yyyy-MM-dd');
        } else {
            val.doneTime = '';
        }
        html += '<li><span class="tag">理赔单号:</span>' + val.applycode + '</li>';
        html += '<li><span class="tag">承保公司:</span>' + val.insuranceCompany + '</li>';
        html += '<li><span class="tag">申请人姓名:</span>' + val.applyPerson + '</li>';
        html += '<li><span class="tag">受益人姓名:</span>' + val.dangerPerson + '</li>';
        html += '<li><span class="tag">受益人证件号:</span>' + val.dangerIdCard + '</li>';
        html += '<li><span class="tag">申请时间:</span>' + val.applyDate + '</li>';
        html += '<li><span class="tag">完结时间:</span>' + val.doneTime + '</li>';
        html += '<li><span class="tag">申请金额:</span>' + val.chargeMoney + '</li>';
        html += '<li><span class="tag">理赔状态:</span>' + val.claimsStatus + '</li>';
        html += '<li><span class="tag">处理状态:</span>' + val.handleStatus + '</li>';
        itemHtml += '<ul class="modal_ul">' + html + '</ul>';
        html = '';
    });
    $(this.controls.claimsContainer).html(itemHtml);
};
//订单拼接
xchat.orderComb = function (data) {
    var html = '';
    var itemHtml = '';
    data.map(function (val, index) {
        if (val.startTime) {
            val.startTime = myUtils.formatDate(val.startTime, 'yyyy-MM-dd');
        } else {
            val.startTime = '';
        }
        if (val.endTime) {
            val.endTime = myUtils.formatDate(val.endTime, 'yyyy-MM-dd');
        } else {
            val.endTime = '';
        }
        switch (val.gender) {
            case 1:
                val.gender = '男';
                break;
            case 2:
                val.gender = '女';
                break;
            default:
                val.gender = '保密';
        }
        switch (val.isBook) {
            case 1:
                val.isBook = '预约';
                break;
            case 0:
                val.isBook = '未预约';
                break;
            default:
                val.isBook = '未预约';
        }
        switch (val.isMarried) {
            case 1:
                val.isMarried = '已婚';
                break;
            case 2:
                val.isMarried = '未婚';
                break;
            default:
                val.isMarried = '保密';
        }
        html += '<li><span class="tag">套餐名称:</span>' + val.caseName + '</li>';
        html += '<li><span class="tag">开始日期:</span>' + val.startTime + '</li>';
        html += '<li><span class="tag">结束日期:</span>' + val.endTime + '</li>';
        html += '<li><span class="tag">性别:</span>' + val.gender + '</li>';
        html += '<li><span class="tag">是否预约:</span>' + val.isBook + '</li>';
        html += '<li><span class="tag">是否体检:</span>' + val.isCheckName + '</li>';
        html += '<li><span class="tag">婚姻状态:</span>' + val.isMarried + '</li>';
        html += '<li><span class="tag">销售模式:</span>' + val.saleModelName + '</li>';
        html += '<li><span class="tag">订单状态:</span>' + val.statusName + '</li>';
        itemHtml += '<ul class="modal_ul">' + html + '</ul>';
        html = '';
    });
    $(this.controls.orderContainer).html(itemHtml);
};
//合同拼接
xchat.contractComb = function (data) {
    var html = '';
    var itemHtml = '';
    data.map(function (val, index) {
        var item = val.enterpriseContract;
        if (item.effectivedate) {
            item.effectivedate = myUtils.formatDate(item.effectivedate, 'yyyy-MM-dd');
        } else {
            item.effectivedate = '';
        }

        if (item.expirydate) {
            item.expirydate = myUtils.formatDate(item.expirydate, 'yyyy-MM-dd');
        } else {
            item.expirydate = '';
        }
        html += '<li><span class="tag">合同名称:</span>' + item.name + '</li>';
        html += '<li><span class="tag">合同编号:</span>' + item.code + '</li>';
        html += '<li><span class="tag">生效日期:</span>' + item.effectivedate + '</li>';
        html += '<li><span class="tag">失效日期:</span>' + item.expirydate + '</li>';
        html += '<li><span class="tag">总保费合计:</span>' + item.money + '</li>';
        html += '<li><span class="tag">缴费方式:</span>' + item.paytype + '</li>';
        html += '<li><span class="tag">争议处理方式:</span>' + item.dispute + '</li>';
        html += '<li><span class="tag">渠道商名称:</span>' + item.channelname + '</li>';
        html += '<li><span class="tag">主被保险人数目:</span>' + item.mianpnum + '</li>';
        html += '<li><span class="tag">合同类型:</span>' + (item.contactType ? "个人":"企业") + (item.isOfficial == 1 ? "正式合同":"体验合同") + '</li>';
        itemHtml += '<ul class="modal_ul">' + html + '</ul>';
        html = '';
    });
    $(this.controls.contractsContainer).html(itemHtml);
};
/*=====================================================================================打开对话=====================================================================================*/
//消息模版事件
xchat.messageListEventBind = function (selector, container) {
    $(selector || '#message_tpl').on('click', 'li', function () {
        $(container || '#enterChat').val($(this).text());
    });
};
/*=====================================================================================等待队列=====================================================================================*/
xchat.getHoldList = function () {
    var _this = this;
    $.get(_this.interface.holdList, {}, function (json) {
        if (json.success) {
            if (json.data) {
                var data = [];
                var html = '';
                for (var i = 0; i < json.data.length; i++) {
                    var friend = json.data[i];
                    friend.from = friend.id;
                    friend.icon = friend.icon || window.base + '/resouces/images/default-avatar.jpg';
                    data.push(friend);
                }
                if (data.length > 0) {
                    data.map(function (val) {
                        html += '<li id="' + val.id + '"><span class="username">' + val.nickname + '</span><span class="call_in">接入</span></li>';
                    });

                } else {
                    html = '无用户接入';
                }
                $('#holdListCont').html(html);
                return html;
            }
        }
    });
};
xchat.holdListEventBind = function () {
    var _this = this;
    $("#holdListBtn").on('click', function () {
        _this.getHoldList();
    });
};

xchat.holdListWeiXInEventBind = function () {
    var _this = this;
    $("#holdListWeiXInBtn").on('click', function () {
        console.log("_this.getHoldList();");
    });


    // 绑定搜索按钮
    $("#wx_search").on('click', function () {
        _this.getUserByNamePhone();
    });


};


xchat.customerListEventBind = function () {
    var _this = this;
    $('#turnBtn').on('click', function () {
        _this.getCustomerList();
    });
};


xchat.callInEventBind = function () {
    var _this = this;
    $(_this.controls.holdListCont).on('click', '.call_in', function () {
        var joinedLength = $(_this.controls.holdListCont).find('li').length;
        var targetId = $(this).parent().attr("id");
        myUtils.load(_this.interface.callIn + targetId, 'get', function (json) {
            if (json.success) {
                $(".modal.fade").hide();
                if (joinedLength == 0) {
                    $("#holdListBtn").removeClass("hasJoined");
                }
            } else {
                alert("接入失败");
            }
        }, {});
    });
};
/*=====================================================================================等待队列=====================================================================================*/
/*=====================================================================================分配客服=====================================================================================*/
xchat.getCustomerList = function () {
    var _this = this;
    $.ajax({
        url: _this.interface.getCustomerList,
        type: 'GET',
        success: function (res) {
            _this.turnComb(res.data);
        },
        error: function () {
            document.getElementById(_this.controls.turnList).innerHTML = '查询失败';
        }
    })
};
xchat.turnComb = function (data) {
    var html = '';
    data.map(function (val) {
        if (val.id !== window.currentId) {
            html += '<li data-id="' + val.id + '">' + val.loginUsername + '<span class="chooseBtn">分配</span></li>';
        }
    });
    $('#turn-list').html(html);
};
xchat.turn = function (vjid, toJid) {
    var _this = this;
    $.ajax({
        url: _this.interface.turn + '?vjid=' + vjid + '&fromJid=' + window.currentId + '&toJid=' + toJid,
        type: 'POST',
        success: function () {
            $('[data-target="turnBtn"]').hide();
            _this.closeFriendWindow();
        },
        error: function () {
            alert('转接失败');
        }
    });
};
xchat.turnEventBind = function () {
    var _this = this;
    $('#turn-list').on('click', 'li span', function () {
        var toJid = $(this).parent().data('id');
        var vjid = $('#currentChatId').data('id');
        _this.turn(vjid, toJid);
    });
};

xchat.getUserByNamePhone = function () {
    var _this = this;
    var phone = $("#wx_phone").val();
    var name = $("#wx_uname").val();
    var nickName = $("#wx_nickName").val();

    $.ajax({
        url: _this.interface.weiXinVisitorList + '?phone=' + phone + "&name=" + name + "&nickName=" + nickName,
        type: 'POST',
        success: function (res) {
            var html = '<tr><th>姓名</th><th>电话</th><th>微信账号</th><th>当前客服账号</th><th>接入</th></tr>';
            if (res.data) {
                res.data.map(function (val) {
                    var cjid;
                    if (val.customer) {
                        cjid = val.customer.id;
                    }
                    html += '<tr><td>' + val.info.pname + '</td>' +
                        '<td>' + val.info.mobile + '</td>' +
                        '<td>' + val.info.nickName + '</td>' +
                        ' <td id="wx_tdLName' + val.info.uid + '">' + cjid + '</td>' +
                        '<td id="wx_td' + val.info.uid + '"><span onclick="xchat.bindCustomer(' + val.info.uid + ',&quot;' + val.info.openId + '&quot;)">接入</span></td></tr>';
                });
                $('#wx_table').html(html);
            } else {
                $('#wx_table').html(html);
            }
        },
        error: function () {
            alert('获取微信用户失败');
        }
    });


}

xchat.bindCustomer = function (uid, vjid) {
    var _this = this;
    $.ajax({
        url: _this.interface.bindCustomer + '?from=' + vjid,
        type: 'POST',
        success: function (res) {
            if (res.data) {
                //$('#wx_tdName' + uid).html(res.data.vCardUser.userName);
                $('#wx_tdLName' + uid).html(res.data.id);
            }
            $('#wx_td' + uid).html('接入成功');
        },
        error: function () {
            alert('接入微信用户失败');
        }
    });
};
/*=====================================================================================分配客服=====================================================================================*/
/*=====================================================================================工具=====================================================================================*/
//滚到地步
xchat.goBottom = function () {
    document.getElementById(this.controls.chatShow).scrollTop = document.getElementById(this.controls.chatTimeline).scrollHeight;
};
//增加提示信息
xchat.addAlert = function () {
    $(Mustache.to_html($('#alert').html())).prependTo("#chatMsgContainer");
};
/*=====================================================================================工具=====================================================================================*/
