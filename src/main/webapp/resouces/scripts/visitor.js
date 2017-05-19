/**
 * 历史记录
 * @constructor
 */
'use strict';
var Visitor = function (options) {
    options = options || {};
    //基础配置
    this.currentId = options.currentId || window.currentId;
    this.base = options.base || window.base;
    this.destJid = options.destJid || window.destJid;

    this.controls = {
        leaveMessageBtn: '#leaveMessageBtn',
        username: '#username',
        phone: '#phone',
        content: '#problem'
    };

    this.interface = {
        addMessage: 'api/addMessage',
        keepOnlineApi: window.base + '/api/keepOnline',
    };
};
Visitor.prototype = {
    init: function () {
        this.postLeaveMessageEventBind();
        this.keepOnline();

    },
    postLeaveMessage: function (username, phone, content) {
        var _this = this;
        $.ajax({
            url: _this.interface.addMessage + '?cid=' + _this.destJid + '&username=' + username + '&phone=' + phone + '&content=' + content,
            type: 'POST',
            success: function (res) {
                if (res.success) {
                    alert('提交成功');
                    history.back();
                } else {
                    alert('提交失败');
                }
            },
            error: function () {
                alert('提交失败');
            }
        })
    },
    postLeaveMessageEventBind: function () {
        var _this = this;
        $(this.controls.leaveMessageBtn).on('click', function () {
            var username = $(_this.controls.username).val();
            var phone = $(_this.controls.phone).val();
            var content = $(_this.controls.content).val();
            console.log(username, phone, content);
            if (username.length === 0 || username.length > 6) {
                alert('请输入正确的名字');
            } else if (phone.length === 0 || phone.length > 11) {
                alert('请输入正确的手机号');
            } else if (content.length === 0) {
                alert('问题不能为空');
            } else if (content.length > 250) {
                alert('问题不超过250个字');
            } else {
                _this.postLeaveMessage(username, phone, content);
            }

        });
    },

    // 定时刷新，防止客服掉线
    keepOnline: function () {
        var _this = this;

        function keep() {
            $.ajax({
                url: _this.interface.keepOnlineApi,
                type: 'POST',
                success: function (res) {
                    if (res.success) {
                        console.log("keepOnline success")
                    }
                },
                error: function () {
                    console.log("keepOnline error")
                }
            });
        }

        // 十分钟调用一次接口   防止掉线
        setInterval(keep, 1000 * 60 * 3);// 注意函数名没有引号和括弧！
    }

};