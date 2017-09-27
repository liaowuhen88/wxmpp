/**
 * JS统一工具类
 */
Utils = (function () {
    var instance;
    //构造
    function Utils() {
        instance = this;
        return this;
    }

    /**
     * ajax send
     * @param link
     * @param async
     * @param callback
     * @param data
     * @returns {null|*|string}
     */
    Utils.prototype.load = function (link, type, callback, data) {
        return $.ajax({
            url: link,
            success: function (json) {
                if (json.success) {
                    callback(json);
                } else {
                    myUtils.visitorShowTip("获取数据出错");
                }
            },
            data: data,
            type: type,
            timeout: 30000,
            error: function () {
                myUtils.visitorShowTip("获取数据超时");
            }
        }).responseText;
    };

    Utils.prototype.ajaxSubmitNoBtn = function (formId, url, data, successCallback, errorCallback) {
        var ajaxOptions = {
            url: url,
            type: 'post',
            data: data,
            dataType: 'json',
            timeout: 100000,
            cache: false,
            success: function (responseJson) {
                if (responseJson.success) {
                    successCallback(responseJson);
                } else {
                    errorCallback(responseJson);
                }
            },
            error: function () {
            }
        };
        $("#" + formId).ajaxSubmit(ajaxOptions);
    }

    Utils.prototype.ajaxSubmit = function (formId, buttonId, url, data, successCallback, errorCallback) {
        $("#" + buttonId).attr("disabled", "disabled");
        var ajaxOptions = {
            url: url,
            type: 'post',
            data: data,
            dataType: 'json',
            timeout: 100000,
            cache: false,
            success: function (responseJson) {
                $("#" + buttonId).removeAttr("disabled");
                if (responseJson.success) {
                    successCallback(responseJson);
                } else {
                    errorCallback(responseJson);
                }
            },
            error: function () {
                $("#" + buttonId).removeAttr("disabled");
            }
        };
        $("#" + formId).ajaxSubmit(ajaxOptions);
    };

    Utils.prototype.renderDiv = function renderDiv(tplId, datas, renderDiv) {
        if (datas) {
            var commentTpl = $('#' + tplId).html();
            $("#" + renderDiv).empty();
            $.each(datas, function (i, item) {
                $(Mustache.to_html(commentTpl, item)).appendTo("#" + renderDiv);
            });
        }
    };

    Utils.prototype.historyRenderDiv = function renderDiv(currentId, datas, renderDiv) {
        if (datas) {
            $.each(datas, function (i, item) {
                var commentTpl;
                console.log(item);
                $(document.getElementById(item.id)).removeClass("sending").addClass("sendDone");
                if (item.from == currentId) {
                    if (item.contentType == 'image') {
                        commentTpl = $('#imgRight').html();

                    } else {
                        commentTpl = $('#mright').html();
                    }

                } else if (item.to == currentId) {
                    if (item.contentType == 'image') {
                        commentTpl = $('#imgLeft').html();
                    }
                    if (item.contentType == 'audio') {
                        commentTpl = $('#audioLeft').html();
                    } else {
                        commentTpl = $('#mleft').html();
                    }

                }
                //TODO 历史记录的发送状态应该全为发送成功
                $(Mustache.to_html(commentTpl, item)).prependTo("#" + renderDiv);
            });
        }
    };

    // 历史纪录展示  本地缓存
    Utils.prototype.cacheRenderDiv = function renderDiv(currentId, datas, renderDiv, fn) {
        if (datas) {
            $.each(datas, function (i, item) {
                var commentTpl;

                if (item.ct) {
                    item.time = myUtils.formatDate(new Date(item.ct));
                }
                if (item.from == currentId) {
                    if (item.contentType == 'image') {
                        commentTpl = $('#imgRight').html();
                    } else {
                        commentTpl = $('#mright').html();
                    }
                } else if (item.to == currentId) {
                    if (item.contentType == 'image') {
                        commentTpl = $('#imgLeft').html();
                    }else if (item.contentType == 'audio') {
                        commentTpl = $('#audioLeft').html();
                    } else {
                        commentTpl = $('#mleft').html();
                    }
                }
                //TODO 历史记录的发送状态应该全为发送成功
                $(Mustache.to_html(commentTpl, item)).appendTo("#" + renderDiv);

            });
            if (fn) {
                fn.call(this);
            }
        }
    };


    // 历史纪录展示  数据库
    Utils.prototype.DBRenderDiv = function renderDiv(currentId, datas, renderDiv, fn) {
        debugger;
        if (datas) {
            $.each(datas, function (i, item) {
                var commentTpl;

                if (item.ct) {
                    item.time = myUtils.formatDate(item.ct);
                }
                if (item.from == currentId) {
                    if (item.contentType == 'image') {
                        commentTpl = $('#imgRight').html();

                    } else {
                        commentTpl = $('#mright').html();
                    }

                } else if (item.to == currentId) {
                    if (item.contentType == 'image') {
                        commentTpl = $('#imgLeft').html();
                    }
                    else if (item.contentType == 'audio') {
                        commentTpl = $('#audioLeft').html();
                    } else {
                        commentTpl = $('#mleft').html();
                    }

                }
                //TODO 历史记录的发送状态应该全为发送成功
                $(Mustache.to_html(commentTpl, item)).prependTo("#" + renderDiv);

            });
            if (fn) {
                fn.call(this);
            }
        }
    };


    // 历史纪录展示  数据库
    Utils.prototype.DBRenderDivAll = function renderDiv(currentId, datas, renderDiv, fn) {
        if (datas) {
            $.each(datas, function (i, item) {
                var commentTpl;
                var direction = item.direction;
                item.direction = item.direction + ":" + item.to;

                var words = ["豆包管家", "您好，我是豆包网的专属客服,不知道您有什么疑问可以帮到您^_^", "您好，我是豆包网的专属客服，您刚浏览了"];
                var flag = false;
                if (direction == 'to') {
                    for (var i = 0, len = words.length; i < len; i++) {
                        if (item.content == words[i] || item.content.indexOf(words[i]) != -1) {
                            flag = true;
                            continue;
                        }
                    }
                }

                if (item.ct) {
                    item.time = myUtils.formatDate(item.ct);
                }
                if (direction == 'from') {
                    if (item.contentType == 'image') {
                        commentTpl = $('#imgRight').html();

                    } else {
                        commentTpl = $('#mright').html();
                    }

                } else {
                    if (item.contentType == 'image') {
                        commentTpl = $('#imgLeft').html();
                    }
                    else if (item.contentType == 'audio') {
                        commentTpl = $('#audioLeft').html();
                    } else {
                        commentTpl = $('#mleft').html();
                    }

                }

                if (flag) {
                    //TODO 历史记录的发送状态应该全为发送成功
                    $(Mustache.to_html(commentTpl, item)).appendTo("#" + renderDiv);
                }

            });
            if (fn) {
                fn.call(this);
            }
        }
    };


    Utils.prototype.renderDivAdd = function renderDiv_(tplId, data, renderDiv) {
        if (data) {
            var commentTpl = $('#' + tplId).html();
            $(Mustache.to_html(commentTpl, data)).appendTo("#" + renderDiv);
        }
    };

    Utils.prototype.renderDivPrepend= function renderDiv_(tplId, data, renderDiv) {
        if (data) {
            var commentTpl = $('#' + tplId).html();
            $(Mustache.to_html(commentTpl, data)).prependTo("#" + renderDiv);
        }
    };

    Utils.prototype.updateImageSrc = function updateImageSrc(id, value) {
        $(document.getElementById(id)).find(".content").find("img").attr("src", value);
        $(document.getElementById(id)).find(".content").find("a").attr("href", value);
    };

    Utils.prototype.findTpl = function findTpl(tplId, data) {
        if (data) {
            var commentTpl = $('#' + tplId).html();
            var currHtml = '';
            $.each(data, function (i, item) {
                currHtml += $(Mustache.to_html(commentTpl, item)).html();
            });
            return currHtml;
        }
    };

    Utils.prototype.findTpl_ = function findTpl_(tplId, data) {
        if (data) {
            var commentTpl = $('#' + tplId).html();
            var currHtml = $(Mustache.to_html(commentTpl, data)).html();
            return currHtml;
        }
    };

    /**
     * 将形如"2015-11-25 17:35:00"的字符串转换为Date对象
     */
    Utils.prototype.parseDate = function (s) {
        var re = /^(\d{4})-(\d\d)-(\d\d) (\d\d):(\d\d):(\d\d)$/;
        var m = re.exec(s);
        return m ? new Date(m[1], m[2] - 1, m[3], m[4], m[5], m[6]) : null;
    };

    /**
     * 格式化日期
     * @param date
     * @param fmt
     * @returns {*|string}
     */
    Utils.prototype.formatDate = function (millisec, fmt) {
        var date = new Date(millisec);
        // console.log(date);
        //console.log(date.getHours());
        fmt = fmt || 'yyyy-MM-dd hh:mm:ss';
        var o = {
            "M+": date.getMonth() + 1,
            "d+": date.getDate(),
            "h+": date.getHours(),
            "m+": date.getMinutes(),
            "s+": date.getSeconds(),
            "q+": Math.floor((date.getMonth() + 3) / 3),
            "S": date.getMilliseconds()
        };
        if (/(y+)/.test(fmt)) {
            fmt = fmt.replace(RegExp.$1, (date.getFullYear() + "").substr(4 - RegExp.$1.length));
        }
        for (var k in o) {
            if (new RegExp("(" + k + ")").test(fmt)) {
                fmt = fmt.replace(RegExp.$1, (RegExp.$1.length == 1) ? (o[k]) : (("00" + o[k]).substr(("" + o[k]).length)));

            }

        }
        return fmt;

    };

    //创建发送消息体
    Utils.prototype.buildSendMsg = function (data) {
        return {
            src: data.to,
            flag: "go",
            time: myUtils.formatDate(new Date(data.ct)),
            message: data.content
        };
    };

    //创建接受消息体
    Utils.prototype.buildRecvMsg = function (data) {
        return {
            src: myUtils.id2name(data.from),
            flag: "from",
            time: myUtils.formatDate(new Date(data.ct)),
            message: data.content
        };
    };

    Utils.prototype.isPc = function () {
        var isPc = "电脑";
        var sUserAgent = navigator.userAgent.toLowerCase();
        var bIsIpad = sUserAgent.match(/ipad/i) == "ipad";
        var bIsIphoneOs = sUserAgent.match(/iphone os/i) == "iphone os";
        var bIsMidp = sUserAgent.match(/midp/i) == "midp";
        var bIsUc7 = sUserAgent.match(/rv:1.2.3.4/i) == "rv:1.2.3.4";
        var bIsUc = sUserAgent.match(/ucweb/i) == "ucweb";
        var bIsAndroid = sUserAgent.match(/android/i) == "android";
        var bIsCE = sUserAgent.match(/windows ce/i) == "windows ce";
        var bIsWM = sUserAgent.match(/windows mobile/i) == "windows mobile";
        if (bIsIpad || bIsIphoneOs || bIsMidp || bIsUc7 || bIsUc || bIsAndroid || bIsCE || bIsWM) {
            isPc = "移动端";
        }
        return isPc;
    };

    //渲染好友列表
    Utils.prototype.renderQueue = function (fromId, queueId, status, addFriend) {
        var fromName = myUtils.id2name(fromId);
        if (fromName == 'sys') {
            return;
        }
        var hasFind = false, hasFindId;
        $("#" + queueId).find("li").each(function () {
            var this_ = $(this);
            var fid = this_.attr("id");
            if (fid == fromId) {
                hasFind = true;
                hasFindId = fid;
            }
        });

        if (hasFind && 'down' == status) {
            //用户下线可以接受消息
            $(document.getElementById(hasFindId)).removeClass("online").addClass("offline");
            //$('#chatInput').addClass('chat-input-disabled');
        } else if (hasFind && 'up' == status) {
            $(document.getElementById(hasFindId)).removeClass("offline").addClass("online");
            $('#chatInput').removeClass('chat-input-disabled');
        }  else if (hasFind && 'remove' == status) {
            $(document.getElementById(hasFindId)).remove();
            $('#chatInput').removeClass('chat-input-disabled');
        }else if (!hasFind && 'up' == status) {
            $('#chatInput').removeClass('chat-input-disabled');
            addFriend.call(this);
        } else if (!hasFind && 'down' == status) {
            $('#chatInput').addClass('chat-input-disabled');
            addFriend.call(this);
        }
    };


    Utils.prototype.friendListRender = function () {

    };
 
    //本地存储
    Utils.prototype.storage = function (data) {
        if (localStorage) {
            //存储to的id
            var storeList = eval("(" + this.getStorage(data.src) + ")");
            var msgList;
            if (storeList == null || storeList == "") {
                msgList = [];
                msgList.push(data);
            } else {
                if (storeList.length >= 300) {
                    storeList.shift();
                }
                storeList.push(data);
                msgList = storeList;
            }
            localStorage.setItem("msg:" + data.src, JSON.stringify(msgList));
        } else {
            console.log("浏览器不支持本地存储");
        }
    };
// 统计一个用户有多少消息未读
    var recvMsgOne = {};
    //本地存储
    Utils.prototype.storage_unread = function (src, count) {
        if (localStorage) {
            //存储to的id
            localStorage.setItem("count:" + src, count);
        } else {
            recvMsgOne[src] = count;
            console.log("浏览器不支持本地存储");
        }
    };

    //本地存储
    Utils.prototype.get_unread = function (src) {
        if (localStorage) {
            //存储to的id
            return localStorage.getItem("count:" + src);
        } else {
            console.log("浏览器不支持本地存储");
            return recvMsgOne[src];

        }
    };

    Utils.prototype.visitorShowTip = function (content, container) {
        var tip = $(".component-info").find("b");
        tip.empty().append(content).show();

        /*var html = '<div class="component-info"><b>'+content+'</b></div>';
         $(container || '#msgContainer').appendTo(html);*/
    };

    //跟进to的id获取本地存储
    Utils.prototype.getStorage = function (id) {
        if (localStorage) {
            return localStorage.getItem("msg:" + id);
        }
        return null;
    };

    //根据id获取本地存储的最后一条信息
    Utils.prototype.getStorageLast = function (id) {

        var dataList = localStorage.getItem("msg:" + id);
        if (dataList) {
            dataList = eval("(" + dataList + ")");
            return dataList[0];
        }

        return null;
    };

    Utils.prototype.id2name = function (id) {
        if (id && id.indexOf('@')) {
            return id.substring(0, id.indexOf("@"));
        } else {
            return id;
        }
    };

    Utils.prototype.Page = function (options) {
        var page = options.pageNumber || 0;
        var pageSize = options.pageSize || 10;
        this.getNextPage = function () {
            return page++;
        };
        this.getPageSize = function () {
            return pageSize;
        }
    };


    Utils.prototype.visitorHistory = function (cacheLast, page) {
        myUtils.load(
            window.base + "/messageHistory/query",
            "get",
            function (responseJson) {
                myUtils.historyRenderDiv(window.currentId, responseJson.data, 'msgContainer');
            },
            {
                from: window.currentId,
                to: window.destJid,
                mLastId: cacheLast,
                page: page.getNextPage(),
                count: page.getPageSize()
            }
        );
    };

    Utils.prototype.customerHistory = function (page, cacheLast, callback) {
        console.log('已请求');
        myUtils.load(
            window.base + "/messageHistory/query",
            "get",
            function (responseJson) {
                myUtils.historyRenderDiv(window.currentId, responseJson.data, 'chatMsgContainer');
                if (callback !== null && callback !== undefined) {
                    callback.bind(this);
                }
            },
            {
                from: window.currentId,
                to: window.destJid,
                page: page.getNextPage(),
                mLastId: cacheLast,
                count: page.getPageSize()
            }
        );
    };

    Utils.prototype.upload = function (options, callback, success) {
        $.webUploader({
            "uploaderId": options.uploaderId,
            "isMultiple": false,
            "uploadPath": options.uploadPath,
            "uploadServer": options.uploadUrl,
            callback: function (file, src) {
                callback(file, src);
            },
            success: function (file, response) {
                success(file, response);
            }
        });
    };

    return Utils;
})();

window.myUtils = new Utils();

/**
 * 判断当前对象是否为空
 *
 * @method isEmpty
 * @param {Object}
 *            obj
 * @return {Boolean} empty 当为 null,undefined,"" 将返回true
 */
window.isEmpty = function (obj) {
    return (obj == null || typeof obj == "undefined" || obj.length == 0)
};


/**
 * 判断当前对象是否非空
 *
 * @method isNotEmpty
 * @param {Object}
 *            obj
 * @return {Boolean}
 */
window.isNotEmpty = function (obj) {
    return !isEmpty(obj);
};

/**
 * 判断是否为函数
 *
 * @method isFunc
 * @param {Object}
 *            fun
 * @return {Boolean}
 */
window.isFunc = function (fun) {
    return (fun != null && typeof fun == "function");
};

/**
 * 判断不是函数
 *
 * @method isNotFunc
 * @param {Object}
 *            fun
 * @return {Boolean}
 */
window.isNotFunc = function (fun) {
    return !isFunc(fun);
};

/**
 * 判断 cur 是否为 type 类型
 *
 * @method typeOf
 * @param {Object}
 *            cur
 * @param {String}
 *            type
 * @example typeOf("Hello","string");//将返回true
 * @return {Boolean}
 */
window.typeOf = function (cur, type) {
    if (typeof type != "string")
        return false;
    return typeof cur == type;
};

/**
 * 判断是否为数组
 *
 * @method isArray
 * @param {Object}
 *            array
 * @return {Boolean}
 */
window.isArray = function (array) {
    return isNotEmpty(array) && className(array) == "Array"
};

/**
 * 判断不是数组
 *
 * @method isNotArray
 * @param {Object}
 *            arr
 * @return {Boolean}
 */
window.isNotArray = function (arr) {
    return !isArray(arr);
};

window.console = window.console || {};
ConsoleUtils = (function () {
    var open = false;

    function ConsoleUtils(op) {
        open = op;
    }

    ConsoleUtils.prototype.toggle = function () {
        open = !open;
    };
    ConsoleUtils.prototype.open = function () {
        open = true;
    };
    ConsoleUtils.prototype.close = function () {
        open = false;
    };
    ConsoleUtils.prototype.log = function (msg) {
        if (open)
            console.log(msg);
    };
    //打印结构
    ConsoleUtils.prototype.dir = function (obj) {
        if (open)
            console.dir(obj);
    };
    return ConsoleUtils;
})();

Console = new ConsoleUtils(false);