/**
 * xChat 方法重写
 * @type {*|jQuery|HTMLElement}
 */
//缓存登录对象
var visitorInfo = {};
var customerInfo = {};

//初始化当前的websocket
var xchat = new xChat({
    url: window.base + '/sockjs/visitor',
    ut: 'visitor',
    lu: window.currentId,
    ic: visitorInfo.icon,
    nk: visitorInfo.userName
});


//控件
xchat.controls = {
    default_visitor_icon: window.base + '/resouces/images/user_photo_default.jpg',
    default_kf_icon: window.base + '/resouces/images/kf.png',
};

/*=====================================================================================初始化=====================================================================================*/
//ws关闭调用
xchat.wsClose = function () {
    this.sendEventUnbind();
    //this.alertShow('链接已断开,请刷新重试');
};
//初始化时调用
xchat.initSuccessQueueStatusHandelEvent = function () {
    this.alertShow('正在连接');
    this.sendImgEventBind();
    this.getLocalHistory();
    // 因为本地缓存和数据库消息对不上，所以这里去除
    //this.getRemoteHistory();

    $('#msgContainer').on('click', function () {
        $('#message').focusout();
    });
};

// 消息发送失败
xchat.msgFailEvent = function () {
    alert("发送失败");
};

//初始化失败调用
xchat.initErrorStatusHandelEvent = function () {
    this.sendEventUnbind();
    this.alertShow("连接失败,请刷新当前页面");
    this.alertMiss();
};
//登录失败后调用
xchat.loginErrorStatusHandelEvent = function (json) {
    this.sendEventUnbind();
    this.alertShow("登录失败,请刷新当前页面");
    //this.alertMiss();
};
//客服登录成功后调用
xchat.customerOnlineStatusHandelEvent = function () {
    this.alertShow("当前客服已经上线");
    this.alertMiss();
    this.leaveMessageHide();
};
//客服下线后调用
xchat.customerOfflineStatusHandelEvent = function () {
    this.alertShow('客服已经下线,消息将以离线方式发送');
    this.alertMiss();
    this.leaveMessageShow();
};
//从等待队列移动到backup队列中调用
xchat.backUpStatusHandelEvent = function () {
    this.sendEventBind();
    this.alertShow("您已经被客服接入");
    this.alertMiss();
};
//加载到线上队列出错
xchat.onlineQueueErrorStatusHandelEvent = function () {
    this.sendEventUnbind();
    this.alertShow("错误，请刷新当前页面");
};
//加载到等待队列出错
xchat.waitQueueErrorStatusHandelEvent = function () {
    this.sendEventUnbind();
    this.alertShow("等待队列已满，请联系其他客服");
};
//登录成功后调用
xchat.loginSuccessStatusHandelEvent = function () {
    this.alertShow('登录成功');
    this.getVisitorInfo();  //获取登录者信息
    this.getCustomerInfo(); //获取客服的信息
    this.sendEventBind();   //发送信息事件绑定
    this.sendAgainEventBind();  //重发消息事件绑定
};
/*=====================================================================================初始化=====================================================================================*/
//获取本地缓存历史
xchat.getLocalHistory = function () {
    var dataList = myUtils.getStorage(window.destJid);
    var _this = this;
    if (dataList) {
        dataList = eval("(" + dataList + ")");
        myUtils.cacheRenderDiv(window.currentId, dataList, 'msgContainer', function () {
            var lastTime = myUtils.formatDate(new Date(dataList[dataList.length - 1].ct));
            _this.componentAdd(lastTime);
            _this.goBottom();
        });
    }
};
//获取远程历史
xchat.getRemoteHistory = function () {
    var visitorPage = new myUtils.Page({
        pageNumber: 0,
        pageSize: 10
    });
    //下拉刷新
    $('#msgContainer').dropload({
        domUp: {
            domClass: 'dropload-up',
            domRefresh: '<div class="dropload-refresh">↓加载历史记录</div>',
            domUpdate: '<div class="dropload-update">↑释放加载</div>',
            domLoad: '<div class="dropload-load">○加载中...</div>'
        },
        loadUpFn: function (me) {
            // 缓存的最后一条信息
            var dataList = myUtils.getStorage(window.destJid);
            var cacheLastId;
            if (dataList) {
                dataList = eval("(" + dataList + ")");
                cacheLastId = dataList[0].id;
            }
            myUtils.visitorHistory(cacheLastId, visitorPage, 'msgContainer');
            me.resetload();
        },
        loadDwonFn: function (me) {
            me.lock('down');
        }
    });
};
//获取登录者信息
xchat.getVisitorInfo = function () {
    myUtils.load(window.base + "/api/visitor/" + window.currentId, 'get', function (visitor) {
        visitorInfo = visitor.data;
    }, {});
};
//获取客服信息
xchat.getCustomerInfo = function () {
    myUtils.load(window.base + "/api/customer/" + window.destJid, 'get', function (customer) {
        customerInfo = customer.data;
    }, {});
};
/*=====================================================================================发送信息=====================================================================================*/
//发送消息包装类
xchat.sendEvent = function () {
    var message = $("#message");
    var msg = message.val();
    var _this = this;
    msg = msg.replace(/<[^>]+>/g, "");
    if (msg) {
        this.normalSend({
            "content": msg,
            "to": destJid,
            "timeOutCall": function (obj) {
                _this.sendStatusFailed(obj);
            }
        });
        message.val('');
    }
};
//发送消息
xchat.sendMsgHandelEvent = function (data) {
    data.fromName = window.card.nickName;
    data.icon = window.card.icon || this.controls.default_visitor_icon;

    data.src = data.to;
    //图片等待返回真正地址之后 才保存本地缓存
    if (data.contentType == 'image') {
        myUtils.renderDivAdd('imgRight', data, 'msgContainer');
    } else {
        myUtils.storage(data);
        myUtils.renderDivAdd('mright', data, 'msgContainer');
    }
    this.goBottom();
};
xchat.sendEventUnbind = function () {
    var sendBtn = $("#sendBtn");
    sendBtn.unbind("click");
};
//发送消息事件绑定
xchat.sendEventBind = function () {
    var sendBtn = $("#sendBtn");
    var _this = this;
    sendBtn.bind('click', function (e) {
        _this.sendEvent();
        $('#message').focus();
        e.stopPropagation();
        e.preventDefault();
        return false;
    });
    //发送消息的事件
    $(document).on('keydown', '#message', function (e) {
        if (e.keyCode == 13) {
            xchat.sendEvent();
            return false;
        }
    });
};
//发送图片消息事件绑定
xchat.sendImgEventBind = function () {
    var _this = this;
    this.imageSendInit(
        "imgUploader",
        "/up/visitor/img/" + window.currentId + "?name=123123.png",
        function (obj) {
            _this.sendStatusFailed(obj);
        }
    );
};
xchat.sendText = function (text, fn) {
    var _this = this;
    text = text.replace(/<[^>]+>/g, "");
    if (text) {
        this.normalSend({
            "content": text,
            "to": destJid,
            "timeOutCall": function (obj) {
                _this.sendStatusFailed(obj);
            }
        });
        if (fn) {
            fn.call(this);
        }
    }
};
//重发消息事件绑定
xchat.sendAgainEventBind = function () {
    var _this = this;
    $('#msgContainer').on('click', '.send-failed', function () {
        var isText = $(this).find('.text').length >= 1;
        var isImg = $(this).find('.emoji').length >= 1;
        if (isText) {
            var text = $(this).find('.text').text();
            _this.sendText(text);
        } else if (isImg) {
            var img = $(this).find('img').attr('src');
            console.log('这是图片信息', img);
        } else {
            console.log('这是什么？');
        }
    });
};
/*=====================================================================================发送信息=====================================================================================*/
/*=====================================================================================接收消息=====================================================================================*/
//接收到文本消息
xchat.recvTextMsgHandelEvent = function (json) {
    json.icon = json.icon || this.controls.default_kf_icon;
    json.src = json.from;
    myUtils.storage(json);
    myUtils.renderDivAdd('mleft', json, 'msgContainer');
    this.goBottom();
};
//接收到图片消息
xchat.recvImageMsgHandelEvent = function (json) {
    json.icon = json.icon || this.controls.default_kf_icon;
    json.src = json.from;
    myUtils.storage(json);
    myUtils.renderDivAdd("imgLeft", json, "msgContainer");
    this.goBottom();
};
//服务器对消息确认
xchat.sendACKStatusHandelEvent = function (data) {
    //var msgDiv = $("#msgContainer").find(data.ackId);
    var msgDiv = $("#" + data.ackId);
    if (msgDiv) {
        this.sendStatusDone(msgDiv);
        console.log('已发送');
    }
    console.log(data);
};
xchat.sendStatusDone = function (obj) {
    obj.find('.content').removeClass('sending send-failed').addClass('send-done');
};
xchat.sendStatusFailed = function (obj) {
    //alert("发送失败");
    obj.find('.content').removeClass('sending').addClass('send-failed');
};
/*=====================================================================================接收消息=====================================================================================*/
/*=====================================================================================队列事件=====================================================================================*/
//已经排队到线上队列
xchat.onlineQueueSuccessStatusHandelEvent = function (json) {
    this.alertShow('您好！欢迎使用豆包网客服.');
    this.alertMiss();
};
//在等待队列中排队
xchat.waitQueueSuccessStatusHandelEvent = function (json) {
    this.sendEventUnbind();
    this.alertShow('当前正在排队:' + json.waitIndex);
};
/*=====================================================================================队列事件=====================================================================================*/
//离线留言显示
xchat.leaveMessageShow = function () {
    $('#leaveMessage').show();
};
//离线留言隐藏
xchat.leaveMessageHide = function () {
    $('#leaveMessage').hide();
};
xchat.goBottom = function () {
    var container = document.getElementById('msgContainer');
    container.scrollTop = container.scrollHeight;
};

xchat.alertShow = function (content) {
    $('#alert').show().html(content);
};

xchat.alertMiss = function (time) {
    var timer = setTimeout(function () {
        $('.alert').hide();
        clearTimeout(timer);
    }, time || 1000);
};

xchat.componentAdd = function (content) {
    var html = '<div class="component-info"><b>' + content + '</b></div>';
    $('#msgContainer').prepend(html);
};