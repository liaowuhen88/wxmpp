!function (w) {
    'use strict';
    var DOUBAO_CHAT = {
        DESKTOP: {
            ID: 'DOUBAO_CHAT_IFRAME_DESKTOP',
            NAME: 'DOUBAO_CHAT_DESKTOP',
            URL: '/desktop-chat.html',
            IFRAME: {
                STYLES: {
                    BASE: {
                        position: 'fixed',
                        bottom: '0',
                        right: '0',
                        border: 'none',
                        overflow: 'hidden',
                        'z-index': '1000'
                    },
                    NORMAL: {
                        bottom: '0',
                        right: '0',
                        width: '130px',
                        height: '60px',
                        'box-shadow': 'none'
                    },
                    ACTIVE: {
                        bottom: '10px',
                        right: '10px',
                        width: '380px',
                        height: '500px',
                        'box-shadow': '0 0 10px rgba(0,0,0,0.15)'
                    }
                }
            }
        },
        MOBILE: {
            ID: 'DOUBAO_CHAT_IFRAME_MOBILE',
            NAME: 'DOUBAO_CHAT_MOBILE',
            URL: '/mobile-chat.html',
            IFRAME: {
                STYLES: {
                    BASE: {
                        position: 'fixed',
                        bottom: '0',
                        right: '0',
                        border: 'none',
                        overflow: 'hidden',
                        'z-index': '1000'
                    },
                    NORMAL: {
                        width: '60px',
                        height: '60px'
                    },
                    ACTIVE: {
                        width: '100%',
                        height: '100%'
                    }
                }
            }
        }
    }

    function doubao_chat() {
        return new Doubao_chat();
    }

    function Doubao_chat() {
        this.BASE_URL = '.';
        this.DESKTOP = DOUBAO_CHAT.DESKTOP;
        this.MOBILE = DOUBAO_CHAT.MOBILE;
        this.SCRIPT_NAME = 'doubaochat.js';
        this.frame = document.createElement('iframe');
        this.init();
    }

    Doubao_chat.prototype.init = function () {
        this.initFrame(); // 初始化frame
    }
    Doubao_chat.prototype.initFrame = function () {
        var self = this;
        /* get Data */
        var url = self.getUrl(self.SCRIPT_NAME);
        var param = self.getParameter(url);
        var paramStr = self.obj2Str(param);
        if (self.isMobile()) {
            self.initFrameMobile(paramStr);
        } else {
            self.initFrameDesktop(paramStr);
        }
        document.body.appendChild(self.frame);
    }
    Doubao_chat.prototype.initFrameDesktop = function (paramStr) {
        var self = this;
        self.frame.id = self.DESKTOP.ID;
        self.frame.src = self.BASE_URL + self.DESKTOP.URL + paramStr;
        self.addClass(self.frame, self.extend(self.DESKTOP.IFRAME.STYLES.BASE, self.DESKTOP.IFRAME.STYLES.NORMAL));
    }
    Doubao_chat.prototype.initFrameMobile = function (paramStr) {
        var self = this;
        self.frame.id = self.MOBILE.ID;
        self.frame.src = self.BASE_URL + self.MOBILE.URL + paramStr;
        self.addClass(self.frame, self.extend(self.MOBILE.IFRAME.STYLES.BASE, self.MOBILE.IFRAME.STYLES.NORMAL));
    }
    Doubao_chat.prototype.isMobile = function () {
        // 是否移动设备
        var ua = w.navigator.userAgent;
        return /mobile/ig.test(ua);
    }
    Doubao_chat.prototype.obj2Str = function (param) {
        // 对象转参数地址
        var arr = [],
            k;
        for (k in param) {
            if (param.hasOwnProperty(k)) {
                arr.push(k + '=' + param[k]);
            }
        }
        return '?' + arr.join('&');
    }
    Doubao_chat.prototype.getUrl = function (match) {
        // 获取参数地址
        var scripts = document.getElementsByTagName('script'),
            i = 0,
            len = scripts.length;
        for (; i < len; i++) {
            if (scripts[i].src.indexOf(match) > 0) {
                return scripts[i].src;
            }
        }
    }
    Doubao_chat.prototype.getParameter = function (url) {
        // 获取参数
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
    }
    Doubao_chat.prototype.addClass = function (el, styles) {
        // 添加样式
        var style;
        for (style in styles) {
            el.style[style] = styles[style]
        }
        return el;
    }
    Doubao_chat.prototype.extend = function (o, n) {
        var k;
        for (k in n) {
            if (n.hasOwnProperty(k) && (!o.hasOwnProperty(k))) {
                o[k] = n[k];
            }
        }
        return o;
    }
    w.DOUBAO_CHAT = DOUBAO_CHAT;
    doubao_chat();
}(window);
