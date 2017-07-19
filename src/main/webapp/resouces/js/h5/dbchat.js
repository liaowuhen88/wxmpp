!function (w) {
    'use strict';
    /**
     * [chat description]
     * @param  {[object]} options [客户端配置]
     * [必填配置描述]
     * @DOUBAO_CHAT {[object]} [由上层提供,若无,则为空对象]
     * @ID {[String]} [最外层包裹默认上层提供,目前有两个值 DOUBAO_CHAT_DESKTOP or DOUBAO_CHAT_MOBILE]
     * @sockConfig {[object]} [sock服务配置]
     * [非必填配置描述]
     * @bar {[String]} [按钮 ID]
     * @im {[String]} [聊天窗口id]
     * @win {[String]} [聊天显示窗口id]
     * @mini {[String]} [最小化按钮id]
     * @textarea {[String]} [输入框id]
     * @send {[String]} [发送按钮ID]
     * @win {[String]} [对话框ID]
     * [sock服务配置描述]
     * @url {[String]} [socket服务地址]
     * @options {[object]}
     * @options.transports [白名单]
     */
    function chat(options) {
        options = options || {};
        return new Chat(options);
    }

    function Chat(options) {
        this.MSG = { // 各种提示信息
            ONOPEN: '连接已建立',
            ONMESSAGE: '消息已接收',
            ONCLOSE: '连接关闭',
            HISTORY: '历史消息',
            TIMEOUT: '连接超时',
            CONNECT_ERROR: '连接出错，请刷新重试'
        };
        this.DOUBAO_CHAT = options.DOUBAO_CHAT || {}; // 顶级常量缓存
        this.FRAME_NAME = this.DOUBAO_CHAT.ID || ''; // iframeID缓存
        this.frame = w.top.document.getElementById(this.FRAME_NAME); // frame缓存若无则为false
        this.ID = options.ID; //包裹层ID
        this.bar = options.bar || 'bar'; //按钮
        this.im = options.im || 'webim'; // 对话窗口
        this.mini = options.mini || 'mini'; // 最小化按钮
        this.textarea = options.textarea || 'textarea'; // 输入框
        this.img = options.img || 'img';
        this.send = options.type || 'send'; //  发送按钮
        this.isType = false; // 是否输入
        this.win = options.win || 'win'; // 对话显示窗口
        this.noType = options.noType || 'noType'; //禁止输入
        this.reconnect = options.reconnect || 'reconnect';
        // this.sockConfig = options.sockConfig; // socket服务配置
        this.sock = null; // sock 缓存
        this.first = true; // 是否首次进入
        this.SOCKET_URL = ''; // socket服务地址
        this.UPLOAD = '';
        this.USER = {};
        this.SOURCE = 'web';
        this.OPENID = undefined;
        this.CHECK_URL = 'http://localhost:8080/appKeyCheck';
        this.MSG_TYPE = {
            TXT: 'txt',
            IMG: 'img',
            FILE: 'file',
            AUDIO: 'audio',
            VIDEO: 'video'
        };
        this.history = undefined;
        this.SEPARATOR = '|';
        this.initConfig(); //初始化配置
        this.bindEvent();
    }

    /**
     * [init description]
     * 初始化
     * initconfig 配置初始化
     * sockConnect 创建连接
     */
    Chat.prototype.init = function () {
        this.toggle('show');
        this.frameToggle('show');
        if (this.first) {
            this.readCache();
            if (this.SOCKET_URL) {
                this.sockConnect();
            }
            this.first = false;
        }
    };
    Chat.prototype.check = function (config) {
        var self = this,
            url = this.CHECK_URL + '?appKey=' + config.appkey + '&id=' + config.id + '&t=' + (+new Date());
        this.get(url, function (res, status) {
            if (res.success) {
                self.SOCKET_URL = res.data.socketUrl;
                //self.SOCKET_URL = 'http://127.0.0.1:9999/echo';
            } else {
                self.appendWin(self.alertRender(res.msg, 'normal'));
            }
        });
    };
    /**
     * [sockConnect description]
     * 建立连接 ！！！ 这里用到了sockjs-client 请先引入
     * @param  {[type]} config [sock配置]
     * @return {[object]} sock [返回sock对象]
     */
    Chat.prototype.sockConnect = function () {
        var self = this,
            sock = new SockJS(self.SOCKET_URL, undefined, {});
        sock.onopen = function () { // 建立连接时触发
            self.appendWin(self.alertRender(self.formatDate(+new Date()) + ' ' + self.MSG.ONOPEN, 'normal'));
            self.typing(true);
            self.autoScroll();
        };

        sock.onmessage = function (e) { // 接收消息时触发
            var data = JSON.parse(e.data);
            if (data.type === 'alert') {
                self.appendWin(self.alertRender(data.nickname + '正在与您通话', 'normal'));
            } else if (data.type) {
                self.appendWin(self.msgRender(data));
                self.localcache(data);
                self.autoScroll();
            } else {
                console.log(data);
            }
        };
        sock.onerror = function (e) {
            console.log(e);
        };
        sock.onclose = function () { // 关闭连接时触发
            self.appendWin(self.alertRender(self.formatDate(+new Date()) + ' ' + self.MSG.ONCLOSE, 'normal'));
            self.typing(false);
            self.connectting(true);
        };
        return this.sock = sock;
    };
    Chat.prototype.sockReconnect = function () {
        this.sock = null;
        this.sockConnect();
        this.connectting(false);
    };
    /**
     * [sockRouter description]
     * sock 路由
     * @param  {[object]} data [消息对象]
     */
    /* Chat.prototype.sockRouter = function(data) {
     if (data) {
     this.appendWin(this.msgRender(data));
     this.eventRouter(event, data.type);
     this.autoScroll();
     }
     };
     Chat.prototype.eventRouter = function(event, type) {
     var self = this;
     return eventType[event][type];
     };*/

    /**
     * [getConfig description]
     * 获取配置
     * @return {[String]} [返回当前URL]
     */
    Chat.prototype.getConfig = function () {
        return this.getParameter(w.location.href);
    };
    /**
     * [initConfig description]
     * 初始化配置
     */
    Chat.prototype.initConfig = function () {
        var self = this,
            config = this.getConfig();
        self.setUserInfo();
        self.setSource();
        self.chatConfig(config);
    };
    /**
     * [chatConfig description]
     * 聊天窗口初始化
     * @param  {[object]} config [配置信息]
     * [配置信息描述]
     * @chat {[boolean]} [true:显示聊天窗口,false:不显示]
     * @frame {[object || boolean]} [存在即为iframe,不存在即为false]
     */
    Chat.prototype.chatConfig = function (config) {
        if (config.chat === 'true' && this.frame) {
            this.init();
        }
        if (!this.frame) {
            this.toggle('show');
            document.getElementById(this.mini).style.display = 'none';
            if (!this.isMobile()) {
                this.addClass(document.getElementById(this.ID), {
                    'margin-top': '100px'
                });
            }
        }
        config.id = this.USER.ID;
        config.nickname = this.USER.NICKNAME;
        this.check(config);
    };
    /**
     * [bindEvent description]
     * 各种事件绑定
     */
    Chat.prototype.bindEvent = function () {
        var self = this;
        if (this.frame) {
            self.attchEvent(self.bar, 'click', function () {
                self.init();
            });
            self.attchEvent(self.mini, 'click', function () {
                self.toggle('hide');
                self.frameToggle('hide');
            });
        }
        // textarea 事件
        self.attchEvent(self.textarea, 'keyup', function (e) {
            if (this.value.length > 0) {
                self.isType = true;
            } else {
                self.isType = false;
            }
            self.typein(self.isType);

            e.stopPropagation();
            e.preventDefault();
        });
        // 回车事件
        self.attchEvent(self.textarea, 'keydown', function (e) {
            if (e.keyCode === 13) {
                if (this.value) {
                    self.sendMsg(this.value);
                }
                event.returnValue = false;
            }
        });
        // 发送按钮事件
        self.attchEvent(self.send, 'click', function () {
            var msg = document.getElementById(self.textarea).value;
            if (msg) {
                self.sendMsg(msg);
            }
            return false;
        });
        // 发送图片事件
        self.attchEvent(self.img, 'change', function (e) {
            self.upload(e);
            // var msg = self.createThumb(e);
            // self.sendMsg(msg);
            // self.upload(e);
        });
        // 重连
        self.attchEvent(self.reconnect, 'click', function (e) {
            self.sockReconnect();
            // self.sockReconnect();
        });
    };
    /**
     * [attchEvent description]
     * 事件绑定
     * @param  {[String]}   id    [id]
     * @param  {[String]}   event [事件类型]
     * @param  {Function} fn    [回调函数]
     */
    Chat.prototype.attchEvent = function (id, event, fn) {
        document.getElementById(id).addEventListener(event, fn, false);
    };
    /**
     * [localcache description]
     * 本地消息缓存
     * @param  {[type]} data [description]
     * @return {[type]}      [description]
     */
    Chat.prototype.localcache = function (data) {
        var ls;
        if (w.localStorage) {
            ls = w.localStorage;
            if (data.type !== 'alert') {
                data = JSON.stringify(data);
                if (ls.history) {
                    ls.history += this.SEPARATOR + data;
                } else {
                    ls.history = data;
                }
            }
            return this.history = ls.history;
        }
    };
    Chat.prototype.readCache = function () {
        var i = 0,
            msglist,
            len,
            domCache = document.createElement('div');
        if (w.localStorage && w.localStorage.history) {
            domCache.append(this.alertRender(this.MSG.HISTORY, 'normal'));
            this.history = w.localStorage.history;
            msglist = this.history.split(this.SEPARATOR);
            len = msglist.length;
            for (; i < len; i++) {
                domCache.append(this.msgRender(JSON.parse(msglist[i])));
            }
            // console.log(domCache.childNodes);
            this.appendWin(domCache);
        }
        this.autoScroll();
    };
    /**
     * [typein description]
     * 发送按钮状态切换
     * @param  {Boolean} isType [true 为可以发送, false 为不可发送]
     * @return {[type]}         [description]
     */
    Chat.prototype.typein = function (isType) {
        if (isType) {
            document.getElementById(this.send).removeAttribute('disabled');
        } else {
            document.getElementById(this.send).setAttribute('disabled', 'true');
        }
    };
    /**
     * [typing description]
     * 是否可以输入
     * @param {[boolean]} [是否可以输入]
     */
    Chat.prototype.typing = function (status) {
        if (status) {
            document.getElementById(this.noType).style.display = 'none';
        } else {
            document.getElementById(this.noType).style.display = 'block';
            document.getElementById(this.textarea).blur();
        }
    };
    Chat.prototype.connectting = function (status) {
        if (status) {
            document.getElementById(this.reconnect).removeAttribute('disabled');
        } else {
            document.getElementById(this.reconnect).setAttribute('disabled');
        }
    };
    Chat.prototype.getUserInfo = function () {
    };
    Chat.prototype.setUserInfo = function () {
        if (w.localStorage) {
            if (w.localStorage.USER) {
                this.USER = JSON.parse(w.localStorage.USER);
            } else if (this.OPENID) {
                this.USER = {
                    ID: +new Date(),
                    NICKNAME: this.OPENID
                }
            } else {
                this.USER = {
                    ID: +new Date(),
                    NICKNAME: '游客_' + (+new Date())
                }
            }
            w.localStorage.USER = JSON.stringify(this.USER);
        }
        return this.USER;
    };
    Chat.prototype.setSource = function () {
        if (this.OPENID) {
            this.SOURCE = 'wechat';
        }
        return this.SOURCE;
    };
    /**
     * [msgPackage description]
     * 消息包装
     * @param  {[String]} content [消息体]
     * @return {[object]}     [返回包装后的消息]
     */
    Chat.prototype.msgPackage = function (content, type) {
        return {
            id: this.USER.ID,
            nickname: this.USER.NICKNAME,
            type: type || this.MSG_TYPE.TXT,
            content: content,
            time: +new Date(),
            source: this.SOURCE,
            isRead: false
        };
    };
    /**
     * [sendMsg description]
     * 消息发送
     * @param  {[String]} msg [消息体]
     */
    Chat.prototype.sendMsg = function (msg, type) {
        var data = this.msgPackage(msg, type);
        this.localcache(data);
        this.appendWin(this.msgRender(data));
        this.sendDone();
        this.sock.send(JSON.stringify(data));
        this.autoScroll();
    };
    /**
     * [appendWin description]
     * 聊天显示窗口消息填充
     * @param  {[object]} data [description]
     */
    Chat.prototype.appendWin = function (el) {
        document.getElementById(this.win).appendChild(el);
    };
    /**
     * [各种render函数]
     * @param  {[type]} data [description]
     * @return {[type]}      [description]
     */
    /**
     * [msgRender description]
     * 消息渲染
     * @param  {[object]} data [消息对象]
     * @return {[object]}      [返回DOM对象]
     */
    Chat.prototype.msgRender = function (data) {
        // 消息渲染
        var bubble = document.createElement('div'),
            inner = '<div class="time">' + this.formatDate(data.time) + '</div>';
        if (data.id === this.USER.ID) {
            bubble.className = 'bubble ' + 'me';
        } else {
            bubble.className = 'bubble ' + 'her';
        }
        switch (data.type) {
            case 'txt':
                inner += '<div class="message">' + data.content + '</div>';
                break;
            case 'img':
                inner += '<div class="message"><img class="thumb" src="http://kf.17doubao.com/' + data.content + '" alt="" /></div>';
                break;
            default:
                inner += '<div class="message">' + data.content + '</div>';
        }
        bubble.innerHTML = inner;
        return bubble;
    };
    /**
     * [alert description]
     * 提示
     * @param  {[type]} str [需要显示的信息]
     */
    Chat.prototype.alertRender = function (str, status) {
        var container = document.createElement('div');
        container.className = 'alert ' + status;
        container.innerHTML = str;
        return container;
    };
    /**
     * [sendDone description]
     * 消息发送完毕后的各种重置
     */
    Chat.prototype.sendDone = function () {
        // 发送消息完成
        document.getElementById(this.textarea).value = '';
        this.isType = false;
        this.typein(this.isType);
        this.autoScroll();
    };
    Chat.prototype.autoScroll = function () {
        var win = document.getElementById(this.win);
        win.scrollTop = win.scrollHeight + 10000;
    };
    /**
     * [toggle description]
     * 切换聊天窗口或者按钮
     * @param  {[String]} status [show 为显示聊天窗口，hide 为隐藏聊天窗口]
     */
    Chat.prototype.toggle = function (status) {
        var im = document.getElementById(this.im),
            bar = document.getElementById(this.bar);
        switch (status) {
            case 'show':
                im.style.visibility = 'visible';
                bar.style.visibility = 'hidden';
                break;
            case 'hide':
                bar.style.visibility = 'visible';
                im.style.visibility = 'hidden';
                break;
            default:
                bar.style.visibility = 'visible';
                im.style.visibility = 'hidden';
        }
    };
    /**
     * [frameToggle description]
     * iframe的展示状态切换
     * @param  {[String]} status [show 为显示聊天窗口，iframe变大，hide 为显示按钮，iframe变小]
     */
    Chat.prototype.frameToggle = function (status) {
        switch (status) {
            case 'show':
                this.addClass(this.frame, this.DOUBAO_CHAT.IFRAME.STYLES.ACTIVE);
                break;
            case 'hide':
                this.addClass(this.frame, this.DOUBAO_CHAT.IFRAME.STYLES.NORMAL);
                break;
            default:
                this.addClass(this.frame, this.DOUBAO_CHAT.IFRAME.STYLES.NORMAL);
        }
    };
    /**
     * [工具类函数]
     */
    /**
     * [addClass description]
     * 增加样式
     * @param {[object]} el     [DOM对象]
     * @param {[object]} styles [样式对象]
     */
    Chat.prototype.addClass = function (el, styles) {
        var style;
        for (style in styles) {
            el.style[style] = styles[style]
        }
        return el;
    };
    /**
     * [getParameter description]
     * 获取URL参数
     * @param  {[String]} url [URL对象]
     * @return {[object]}     [参数对象]
     */
    Chat.prototype.getParameter = function (url) {
        var tmp = {},
            match = url.match(/\?[^#]*/);
        if (!match || match[0].length === 1) {
            return tmp;
        }
        var paramArr = match[0].slice(1).split('&');
        paramArr.forEach(function (item) {
            var match = item.split('='),
                key = match[0],
                value = decodeURIComponent(match[1] || '');
            tmp[key] = value;
        });
        return tmp;
    };
    /**
     * [formatDate description]
     * 格式化日期
     * @param  {[String]} str [时间对象，将自动转为字符串]
     * @param  {[String]} fmt [日期格式]
     * @return {[String]}     [返回格式化后的日期]
     */
    Chat.prototype.formatDate = function (str, fmt) {
        fmt = fmt || 'yyyy-MM-dd hh:mm:ss';
        var date = new Date(str),
            rules = {
                'M+': date.getMonth() + 1, //月份
                'd+': date.getDate(), //日
                'h+': date.getHours(), //  小时
                'm+': date.getMinutes(), //分
                's+': date.getSeconds(), //秒
                'q+': Math.floor((date.getMonth() + 3) / 3), //季度
                'S': date.getMilliseconds(), //毫秒
                'T': 'T'
            };
        if (/(y+)/i.test(fmt)) {
            fmt = fmt.replace(RegExp.$1, (date.getFullYear() + "").substr(4 - RegExp.$1.length));
        }
        for (var key in rules) {
            if (new RegExp("(" + key + ")").test(fmt)) {
                fmt = fmt.replace(RegExp.$1, (RegExp.$1.length === 1) ? (rules[key]) : (("00" + rules[key]).substr(("" + rules[key]).length)));
            }
        }
        return fmt;
    };
    /**
     * [isMobile description]
     * 判断是否为移动端
     * @return {Boolean} [返回true or false]
     */
    Chat.prototype.isMobile = function () {
        // 是否移动设备
        var ua = w.navigator.userAgent;
        return /mobile/ig.test(ua);
    };
    Chat.prototype.upload = function (e) {
        var self = this,
            formData = new FormData(),
            blob = e.target.files[0];
        formData.append('img', blob);
        this.post(this.UPLOAD.URL, formData, function (res) {
            // console.log(res);
            self.sendMsg(res.path, 'img');
        });
    };
    Chat.prototype.createThumb = function (e) {
        return '<img src="data:image/png;base64,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" alt="" />';
    };
    Chat.prototype.readFile = function (e) {
        /*var self = this;
         if (w.File && w.FileReader && w.FileList && w.Blob) {
         var files = e.target.files,
         i = 0,
         len = files.length,
         f;
         for (; i < len; i++) {
         f = files[i];
         if (!f.type.match('image.*')) {
         continue;
         }
         var reader = new FileReader();
         reader.onload = (function(theFile) {
         return function(e) {
         var msg = ['<img class="thumb" src="', e.target.result,
         '" title="', escape(theFile.name), '"/>'
         ].join('');
         // self.sendMsg(msg);
         var xhr = new XMLHttpRequest();
         xhr.open('POST', self.UPLOAD.URL, true);
         xhr.send(e.target.result);
         xhr.onreadystatechange = function() {
         if (xhr.readyState === 4 && xhr.status === 200) {
         console.log(xhr.responseText);
         }
         }
         };
         })(f);
         reader.readAsDataURL(f);
         }
         } else {
         }*/
    };
    Chat.prototype.post = function (url, data, success, error) {
        var xhr = new XMLHttpRequest();
        xhr.open('POST', url, true);
        xhr.send(data);
        xhr.onreadystatechange = function () {
            if (xhr.readyState === 4 && xhr.status === 200) {
                if (fn) {
                    fn(JSON.parse(xhr.response))
                }
            }
        }
    };
    Chat.prototype.get = function (url, fn) {
        var xhr = new XMLHttpRequest();
        xhr.open('GET', url, true);
        xhr.send(null);
        xhr.onreadystatechange = function () {
            if (xhr.readyState === 4 && xhr.status === 200) {
                if (fn) {
                    fn(JSON.parse(xhr.response), xhr.status)
                }
            }
        }
    };
    w.chat = chat;
}(window);
