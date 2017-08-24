/*****************************************************************
 * chat核心
 * 不许随便改动
 * v0.1
 * @param options
 * ***************************************************************
 */
var xChat = function (options) {
    options = options || {};
    //定义接口
    //接收到信息
    this.recvMsgEvent = function (json) {
    };
    //接收到文本信息
    this.recvTextMsgHandelEvent = function (json) {
    };
    //接收到视频信息
    this.recvVideoMsgHandelEvent = function (json) {
    };
    //接收到文件信息
    this.recvFileMsgHandelEvent = function (json) {
    };
    //接收到图片信息
    this.recvImageMsgHandelEvent = function () {
    };
    //接收到音频信息
    this.recvAudioMsgHandelEvent = function (json) {
    };
    //发送消息的处理
    this.sendMsgHandelEvent = function (json) {
    };
    //登录失败处理
    this.loginErrorStatusHandelEvent = function (json) {
    };
    //登录成功处理
    this.loginSuccessStatusHandelEvent = function (json) {
    };
    //排队到online队列
    this.onlineQueueSuccessStatusHandelEvent = function (json) {
    };
    //被服务器踢出
    this.kickOffStatusHandelEvent = function (json) {
    };
    //下线
    this.offlineStatusHandelEvent = function (json) {
    };

    //转接下线
    this.changeOfflineStatusHandelEvent = function (json) {
    };

    //初始化失败
    this.initErrorStatusHandelEvent = function (json) {
    };
    //在等待队列中
    this.waitQueueSuccessStatusHandelEvent = function () {
    };
    //进入到等待队列失败
    this.waitQueueErrorStatusHandelEvent = function () {
    };
    //客服在线
    this.customerOnlineStatusHandelEvent = function () {
    };
    //客服离线
    this.customerOfflineStatusHandelEvent = function () {
    };
    //离开等待队列
    this.offlineWaitQueueStatusHandelEvent = function () {
    };
    //初始化成功
    this.initSuccessQueueStatusHandelEvent = function () {
    };
    //在临时队列中
    this.backUpStatusHandelEvent = function () {
    };
    // 消息发送失败
    this.msgFailEvent = function () {
    };
    //离开临时队列
    this.offlineBackUpStatusHandelEvent = function () {
    };
    //ws关闭
    this.wsClose = function () {
        console.log("wsClose")
        window.location.href = window.base + "/api/customer_chat";
        //disConnect();
    };
    //接收到服务器对消息的回执
    this.sendACKStatusHandelEvent = function () {
    };
    //初次建立连接成功
    this.handsUp = function () {
    };
    //客服发出了移动指令 从一个客服移动到另一个客服
    this.moveMsgHandelEvent = function (json) {
    };
    //发送消息管理器
    var SenderFactory = function (o) {
        var _this = o;
        var _timeOutTime = 3000;
        var _senderTimeOut = [];
        var _interval;
        this.stop = function () {
            if (_interval) {
                _interval.clear();
            }
        };
        var _init = function () {
            var it = function () {
                for (var i = 0; i < _senderTimeOut.length; i++) {
                    var sto = _senderTimeOut[i];
                    var ct = sto.ct;
                    var to = sto.timeOutTime;
                    var callback = sto.timeoutCall;
                    var id = sto.id;
                    var et = new Date().getTime();
                    //id里边包含特殊符号 需要用document获取
                    var checkTarget = $(document.getElementById(id)).find('.content');
                    var claStr = checkTarget.attr("class");
                    if (claStr && claStr != undefined && claStr.indexOf("send-done") != -1) {
                        _senderTimeOut.splice(i, 1);
                        break;
                    }

                    if (ct && et - ct > to) {
                        _senderTimeOut.splice(i, 1);
                        if (callback) {
                            callback(checkTarget);
                        }
                        break;
                    }
                }
            };
            _interval = setInterval(it, _timeOutTime);
        };

        //用于创建消息检查对象
        var _timeOutCheck = function (msg) {
            var cto = {
                "id": msg.id,
                "ct": new Date().getTime(),
                "timeOutTime": msg.timeOutTime || _timeOutTime,
                "timeoutCall": msg.timeOutCall
            };
            //是否开启消息延迟检查
            if (_sto) {
                _senderTimeOut.push(cto);
            }
        };

        /**
         * 消息预处理，发送前事件，异步处理事件，处理后事件
         * 用于统一的消息发送，延迟判断
         * 判断的原理是 发送消息后 如果在规定延迟时间内接收到服务器回执 则判断为非延迟
         * 如果超过规定的延迟时间 则判定为 发送超时
         * @param preHandelMsg
         * @param before
         * @param asyncMethod
         * @param after
         * @private
         */
        var _Sender = function (preHandelMsg, before, asyncMethod, after) {
            //console.log(_Sender);
            this.send = function (msg) {
                if (_ws) {
                    if (preHandelMsg) {
                        msg = preHandelMsg(msg);
                    }
                    if (before) {
                        before(msg);
                    }

                    //异步情况下 等待异步执行完成后 调用发送方法
                    if (asyncMethod) {
                        asyncMethod(msg, function () {
                            sending(msg)
                        });
                    } else {
                        //同步情况下 创建cto
                        _timeOutCheck(msg);
                        sending(msg);
                    }
                    if (after) {
                        after(msg);
                    }
                } else {
                    this.wsClose();
                }
            };
            var sending = function (msg) {
                _ws.send(JSON.stringify(msg));
            }
        };

        this.getSender = function (plugins, type, _options) {
            //普通消息的
            switch (type) {
                case "normal":
                    return new _Sender(
                        function (msg) {
                            return _msgFactory.buildNormalMsg(msg);
                        },
                        function (msg) {
                            _this.sendMsgHandelEvent(msg);
                        }
                    );
                case "receipt":
                    return new _Sender(
                        function (msg) {
                            return _msgFactory.buildReceiptMsg(msg);
                        }
                    );
                case "image":
                    //异步的sender
                    return new _Sender(
                        function (msg) {
                            return _msgFactory.buildImgMsg(msg);
                        },
                        null,
                        function (msg, send) {
                            plugins.uploader(
                                _options,
                                //网络异常不会执行此方法，正常情况下 上传成功后 赋值消息内容为图片地址 并且调用发送回调
                                function (file, response) {
                                    console.log(file);
                                    msg.content = _pluginsConfig.upload.downUrl + "?key=" + response.src;
                                    //更新之前填好的预览图
                                    if (window.user) {
                                        msg.icon = window.user.icon;
                                    }
                                    msg.to = destJid;
                                    msg.from = window.currentId;
                                    send();
                                    msg.time = myUtils.formatDate(new Date(msg.ct));
                                    msg.src = msg.to;
                                    myUtils.storage(msg);
                                    myUtils.updateImageSrc(msg.id, msg.content);
                                },
                                function () {

                                },
                                function (file, src) {
                                    msg.id = createMsgId();
                                    //选择文件的动作之后开始计时，一定会执行
                                    _timeOutCheck(msg);
                                    //生成一个预览图 并且渲染界面
                                    msg.content = src;
                                    msg.to = _options.destJid;
                                    _this.sendMsgHandelEvent(msg);
                                }
                            );
                        }
                    );
            }
        };
        //执行interval循环
        _init();
    };

    //创建id规则
    var createMsgId = function () {
        return _ut + "_" + new Date().getTime();
    };

    //消息工厂
    var MsgFactory = function () {

        //构造发送基础消息
        var _baseSendMsg = function () {
            return {
                "id": createMsgId(),
                "from": _lu,
                "username": _nk,
                "icon": _ic,
                "ct": new Date().getTime()
            }
        };

        //回执类消息
        this.buildReceiptMsg = function (msg) {
            var receipt = {
                "id": msg.id,//被回执的消息id
                "contentType": "receiptMsg",
                "type": "msg"
            };
            return $.extend({}, _baseSendMsg(), receipt);
        };

        //发送图片类消息
        this.buildImgMsg = function (msg) {
            msg.contentType = "image";
            msg.type = "msg";

            return $.extend({}, _baseSendMsg(), msg);
        };

        //发送音频消息
        this.buildAudio = function (msg) {
            msg.contentType = "audio";
            msg.type = "msg";
            return $.extend({}, _baseSendMsg(), msg);
        };

        //发送普通消息消息
        this.buildNormalMsg = function (msg) {
            msg.contentType = "text";
            msg.type = "msg";
            return $.extend({}, _baseSendMsg(), msg);
        };
    };

    //插件
    var Plugins = function () {

        this.uploader = function (options, success, startUpload, thumb) {
            $.webUploader({
                "uploaderId": options.uploaderId,
                "isMultiple": false,
                "uploadPath": options.uploadPath,
                "uploadServer": _pluginsConfig.upload.uploadUrl,
                startUpload: function () {
                    startUpload();
                },
                callback: function (file, src) {
                    console.log("callback");
                    if (thumb) {
                        thumb(file, src);
                    }
                },
                success: function (file, response) {
                    success(file, response);
                }
            })
        };

    };

    //当前filter用于控制事件
    var FilterEvent = function () {
        var block = false;
        //比如 接受到waitQueueSuccess后 只能接受到onlineQueueSuccess 才能正常通信,其他的事件一概忽略
        var filterEventDef = {
            //key 本身是放行的，但是后边开始拦截，直到有对应的 value 后才放行
            "waitQueueSuccess": ["backUpQueueSuccess", "onlineQueueSuccess", "customerOffline"]
        };

        this.blockIt = function (key) {
            var isKey = false;
            if (block) {
                for (var pk in filterEventDef) {
                    //找到了要对应放行的事件
                    if ($.inArray(key, filterEventDef[pk]) != -1) {
                        block = false;
                        break;
                    }
                }
            } else {
                for (var pk in filterEventDef) {
                    if (key == pk) {
                        block = true;
                        isKey = true;
                        break;
                    }
                }
            }
            if (isKey) {
                //不拦截
                return false;
            }
            return block;
        };

    };

    var _ws = null;
    var _url = options.url;
    var _transports = [];
    var _ut = options.ut || '';//记录当前登录的用户类型
    var _sr = options.sr || true;//是否开启消息回执
    var _lu = options.lu;//当前登录的账号id
    var _ic = options.ic || '';//当前登录的icon
    var _nk = options.nk || '';//昵称
    var _sto = options.sto || true;//是否开启消息超时检查

    var _msgFactory = new MsgFactory();
    var _senderFactory = new SenderFactory(this);
    var _filterEvent = new FilterEvent();

    //上传配置
    var _pluginsConfig = {
        "upload": {
            "downUrl": "http://upload.17doubao.com/xmppDownLoad/downLoad",
            "uploadUrl": "http://upload.17doubao.com/upload"
        }
    };

    //初始化插件配置
    var _plugins = new Plugins();


    //任何的消息来之前触发
    var _events = function () {
        var _this = this;
        return {
            msg: [
                {"text": _this.recvTextMsgHandelEvent},
                {"video": _this.recvVideoMsgHandelEvent},
                {"file": _this.recvFileMsgHandelEvent},
                {"audio": _this.recvAudioMsgHandelEvent},
                {"image": _this.recvImageMsgHandelEvent}
            ],
            active: [
                {"move": _this.moveMsgHandelEvent}
            ],
            status: [
                {"initSuccess": _this.initSuccessQueueStatusHandelEvent},
                {"initError": _this.initErrorStatusHandelEvent},
                {"loginError": _this.loginErrorStatusHandelEvent},
                {"loginSuccess": _this.loginSuccessStatusHandelEvent},
                {"offline": _this.offlineStatusHandelEvent},
                {"changeOffline": _this.changeOfflineStatusHandelEvent},
                {"offlineWaitQueue": _this.offlineWaitQueueStatusHandelEvent},
                {"offlineBackUpQueue": _this.offlineBackQueueStatusHandelEvent},
                {"kickOff": _this.kickOffStatusHandelEvent},
                {"onlineQueueSuccess": _this.onlineQueueSuccessStatusHandelEvent},
                {"waitQueueSuccess": _this.waitQueueSuccessStatusHandelEvent},
                {"waitQueueError": _this.waitQueueErrorStatusHandelEvent},
                {"onlineQueueError": _this.onlineQueueErrorStatusHandelEvent},
                {"customerOnline": _this.customerOnlineStatusHandelEvent},
                {"backUpQueueSuccess": _this.backUpStatusHandelEvent},
                {"offlineBackUpQueueSuccess": _this.offlineBackUpStatusHandelEvent},
                {"serverACK": _this.sendACKStatusHandelEvent},
                {"customerOffline": _this.customerOfflineStatusHandelEvent},
                {"msgFail": _this.msgFailEvent}

            ]
        }
    };


    //路由
    var _routeProtocolEvents = function (json) {
        console.log(json);
        var _this = this;
        var data = eval("(" + json + ")");
        if (data) {
            var topType = data.type;
            if (topType) {
                var type = data.contentType || data.actionType || data.status;
                var objList = _events.call(_this)[topType];
                if(topType == "msg"){
                    _this.recvMsgEvent(data);
                }

                for (var i = 0; i < objList.length; i++) {
                    var obj = objList[i];
                    if (obj && obj[type]) {
                        //过滤器 事件过滤
                        if (_ut == 'visitor') {
                            if (_filterEvent.blockIt(type)) {
                                return;
                            }
                        }
                        obj[type].call(_this, data);
                        //只有消息类 才开启消息回执 状态类消息不会发送回执
                        //if (('text' == type || 'image' == type) && _sr && 'visitor' == _ut) {
                        //    _senderFactory.getSender('receipt').send(data);
                        //}
                        break;
                    }
                }
            }
        }
    };

    //开始创建连接
    this.connect = function () {
        var _this = this;
        if (!_url) {
            alert('请求地址错误');
            return;
        }
        _ws = new SockJS(_url, undefined, {protocols_whitelist: _transports});

        _ws.onopen = function () {
            _this.handsUp();
        };
        _ws.onmessage = function (_event) {
            _routeProtocolEvents.call(_this, _event.data);
        };
        _ws.onclose = function (_event) {
            _this.wsClose();
        };
    };

    var disConnect = function(){
        setTimeout(function(){
            this.connect();
        },5000);
    }

    //销毁ws
    this.destroy = function () {
        if (_ws) {
            _ws.close();
            _senderFactory.stop();
        }
    };

    //发送普通的消息
    this.normalSend = function (msg) {
        _senderFactory.getSender(null, 'normal').send(msg);
    };

    //发送文件消息 30秒如果还没发送成功 则超时
    this.imageSendInit = function (uploaderId, uploadPath, timeout) {
        _senderFactory.getSender(_plugins, 'image', {
            "uploaderId": uploaderId,
            "uploadPath": uploadPath
        }).send({
            "timeOutCall": timeout,
            "timeOutTime": 30000
        });
    };

//
    this.uploadAvatar = function (id, uploadPath,fn) {
        $.avatarUploader({
            "uploaderId": id,
            "isMultiple": false,
            "uploadPath": uploadPath,
            "uploadServer": _pluginsConfig.upload.uploadUrl,
            startUpload: function () {
                console.log('正在上传');
            },
            callback: function (file, src) {
                console.log(file);
                console.log(src);

            },
            success: function (file, response) {
                console.log(file);
                console.log(response);
                if(fn){
                    fn.call(this,response);
                }
            }
        });
    };
};