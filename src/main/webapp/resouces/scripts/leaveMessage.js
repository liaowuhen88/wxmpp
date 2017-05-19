/**
 * 设置页面
 * @constructor
 */
'use strict';
var LeaveMessage = function (options) {
    options = options || {};
    //基础配置
    this.currentId = options.currentId || window.currentId;
    this.base = options.base || window.base;

    this.controls = {
        leaveMessageBtn: '#leaveMessageBtn',
        leaveMessageCont: '#leaveMessageCont',
        leaveMessagePages: 'leaveMessagePages',
        archiveStatusBtn: '.message-status'
    };

    this.interface = {
        getMessageList: window.base + '/api/getMessageByCId',
        archiveMessage: window.base + '/api/updateMessage'
    };
};
LeaveMessage.prototype = {
    //初始化
    init: function () {
        this.getMessageListEventBind();  //获取留言列表
        this.archiveMessageEventBind();     //归档处理信息
        this.modalEventBind();  //模态框绑定
        this.getStatusMessageListEventBind();
        this.haveNewMsgEventBind();
    },
    //获取留言列表事件绑定
    getMessageListEventBind: function () {
        var _this = this;
        $(this.controls.leaveMessageBtn).on('click', function () {
            _this.getMessageList();
        });
    },
    //归档留言事件绑定
    archiveMessageEventBind: function () {
        var _this = this;
        $(this.controls.leaveMessageCont).on('click', 'li .process-btn', function () {
            var btn = $(this);
            var input = $(this).parent().find('textarea');
            var inputParent = input.parent();
            var id = $(this).data('id');
            var result = input.val();
            console.log(id, result);
            _this.archiveMessage(id, result, function (res) {
                if (res.success) {
                    btn.remove();
                    inputParent.text(result);
                }
            });
        });
    },
    //归档留言
    archiveMessage: function (id, result, fn) {
        var _this = this;
        $.ajax({
            url: this.interface.archiveMessage + '?id=' + id + '&result=' + result,
            type: 'POST',
            success: function (res) {
                if (fn) {
                    fn.call(this, res);
                }
            },
            error: function () {
                alert('归档失败，请重试');
            }
        });
    },
    //获取留言列表
    getMessageList: function (curr, status, fn) {
        var _this = this;
        curr = curr || 0;
        status = status || '';
        $.ajax({
            url: _this.interface.getMessageList + '?cid=' + _this.currentId + '&page=' + curr + '&status=' + status,
            type: 'POST',
            success: function (res) {
                if (res.success) {
                    var data = res.data;
                    _this.messageListComb(data);
                    _this.laypage(res.page, res.pages, _this.getStatus());
                    if (fn) {
                        fn.call(this, res);
                    }
                }
            },
            error: function () {
                $(_this.controls.leaveMessageCont).html('没有未处理的留言');
            }
        });
    },
    getStatus: function () {
        var status = $('.status-active').data('status');
        return status;
    },
    //获取不同状态留言事件绑定
    getStatusMessageListEventBind: function () {
        var _this = this;
        $(this.controls.archiveStatusBtn).on('click', function () {
            var status = $(this).data('status');
            $(this).addClass('status-active').siblings().removeClass('status-active');
            $(_this.controls.leaveMessageBtn).removeClass('haveNewMsg');
            _this.getMessageList(0, status);
        });
    },
    //留言列表拼接
    messageListComb: function (data) {
        var html = '';
        data.map(function (val) {
            if (val.status === 1) {
                html += '<li><dl class="type"><dt>联系姓名：</dt><dd>' + val.username + '</dd><dt>时间：</dt><dd>' + val.ut + '</dd><dt>联系电话：</dt><dd>' + val.phone + '</dd><dt>咨询问题：</dt><dd>' + val.content + '</dd><dt>处理备注：</dt><dd><textarea></textarea></dd></dl><div class="process-btn" data-id="' + val.id + '">处理</div></li>';
            } else {
                html += '<li><dl class="type"><dt>联系姓名：</dt><dd>' + val.username + '</dd><dt>时间：</dt><dd>' + val.ut + '</dd><dt>联系电话：</dt><dd>' + val.phone + '</dd><dt>咨询问题：</dt><dd>' + val.content + '</dd><dt>处理备注：</dt><dd>' + val.dealResult + '</dd></dl></li>';
            }
        });
        $(this.controls.leaveMessageCont).html(html);
    },
    //分页
    laypage: function (curr, pages, status) {
        var _this = this;
        laypage({
            cont: _this.controls.leaveMessagePages, //容器。值支持id名、原生dom对象，jquery对象。【如该容器为】：<div id="page1"></div>
            pages: pages, //通过后台拿到的总页数
            curr: curr + 1 || 0, //当前页
            jump: function (obj, first) { //触发分页后的回调
                if (!first) { //点击跳页触发函数自身，并传递当前页：obj.curr
                    _this.getMessageList(obj.curr - 1, status);
                }
            }
        });
    },
    //模态框事件绑定
    modalEventBind: function () {
        $(this.controls.leaveMessageBtn).modal();
    },
    haveNewMsgEventBind: function () {
        var _this = this;
        this.getMessageList(0, '1', function (res) {
            var data = res.data;
            if (data.length !== 0) {
                $(_this.controls.leaveMessageBtn).addClass('haveNewMsg');
            }else {
                $(_this.controls.leaveMessageBtn).removeClass('haveNewMsg');
            }
        });
    }
};