/**
 * 历史记录
 * @constructor
 */
'use strict';

var History = function (options) {
    options = options || {};
    this.currentId = options.currentId || window.currentId;
    this.base = options.base || window.base;
    //控件
    this.searchHistoryInput = $('#searchHistoryInput');
    this.searchBtn = $('#searchBtn');

    this.controls = {
        pagesCont: 'pagesCont',
        historyBtn: '#historyBtn',
        showHistory: '#showHistory'
    };

    this.table = $('#table');

    this.interface = {
        searchHistory: window.base + '/messageHistory/getUserHistoryList' || options.interface.searchHistory,
        historyDetail: window.base + '/messageHistory/query' || options.interface.historyDetail
    }
};
History.prototype = {
    /*=====================================================================================初始化=====================================================================================*/
    init: function () {
        this.getHistoryListEventBind();     //获取历史记录事件绑定
        this.searchHistoryEventBind();       //搜索历史记录事件绑定
        this.showHistoryEventBind();    //显示聊天记录事件绑定
        this.hideHistoryEventBind();       //隐藏聊天记录窗口
        this.wheelGetHistoryEventBind();    //滚动获取更多历史记录事件绑定
    },
    /*=====================================================================================初始化=====================================================================================*/
    /*=====================================================================================历史记录=====================================================================================*/
    //显示历史记录
    showHistory: function (id, page, count) {
        console.log(this.currentId, id, page, count);
        var _this = this;
        page = page || 0;
        count = 20;
        $.ajax({
            url: _this.interface.historyDetail + '?from=' + _this.currentId + '&to=' + id + '&page=' + page + '&count=' + count,
            type: 'POST',
            timeout: 3000,
            success: function (res) {
                if (res.success) {
                    var data = res.data;
                    console.log(data);
                    myUtils.DBRenderDiv(_this.currentId, data, 'showHistoryContainer', function () {
                        console.log(1111);
                    });
                }
            },
            error: function () {
                alert('查询失败,请重试');
            }
        });
    },
    //隐藏历史记录窗口事件绑定
    hideHistoryEventBind: function () {
        var _this = this;
        $(this.controls.showHistory).on('click', 'span.close', function () {
            $(_this.controls.showHistory).hide();
        });
    },
    //显示历史记录窗口事件绑定
    showHistoryEventBind: function () {
        var _this = this;
        $('#table').on('click', 'tr', function () {
            var id = $(this).data('id');
            var formUsername = $(this).find('.nickname').text();
            if (id) {
                $(_this.controls.showHistory).data('id', id);
                $('#historyFromUsername').text(formUsername);
                _this.showHistory(id, 0, 2);
                $('#showHistoryContainer').empty();
                $(_this.controls.showHistory).show();
            } else {
                return false;
            }

        });
    },
    //获取历史记录
    getHistoryList: function (page, count, nickName) {
        var _this = this;
        nickName = nickName || '';
        page = page || 0;
        count = count || 10;
        $.ajax({
            url: _this.interface.searchHistory + '?id=' + _this.currentId + '&page=' + page + '&count=' + count + '&nickName=' + nickName,
            type: 'POST',
            success: function (res) {
                if (res.success && res.data.list !== null) {
                    var data = res.data;
                    _this.historyListComb(data.list);
                    _this.laypage(page, data.pages);
                } else {
                    var history = document.getElementById('history');
                    history.innerHTML = '没有数据';
                }
            },
            error: function () {
                alert('查询失败，请重试');
            }
        });
    },
    //获取历史记录事件绑定
    getHistoryListEventBind: function () {
        var _this = this;
        $(this.controls.historyBtn).on('click', function () {
            _this.getHistoryList();
        });
    },
    //搜索历史记录事件绑定
    searchHistoryEventBind: function () {
        var _this = this;
        this.searchBtn.on('click', function () {
            var nickName = _this.searchHistoryInput.val();
            _this.getHistoryList(nickName);
        });
    },
    //历史记录字符串拼接
    historyListComb: function (data) {
        var header = '<tr><th>顾客Id</th><th>登录名</th><th>顾客头像</th><th>顾客昵称</th><th>备注</th><th>访问时间</th></tr>';
        var body = '';
        $.each(data, function (index, item) {
            console.log(item.chattime);
            body += '<tr data-id="' + item.id + '">' +
                '<td>' + item.id + '</td>' +
                '<td>' + item.loginUsername + '</td>' +
                '<td><img class="avatar" src=' + item.icon + ' ></td>' +
                '<td class="nickname">' + item.nickName + '</span></td>' +
                '<td>' + (item.remark !== null ? item.remark : '') + '</span></td>' +
                '<td>' + item.chattime + '</td>' +
                '</tr>';
        });
        this.table.html(header + body);
    },
    //滚动获取更多历史记录事件绑定
    wheelGetHistoryEventBind: function () {
        var _this = this;
        var i = 0;
        var page = 0;
        var count = 10;
        $('#showHistoryChatShow').on('mousewheel', function (e) {
            var delta = -e.originalEvent.wheelDelta || e.originalEvent.detail;
            var id = $(_this.controls.showHistory).data('id');
            if (delta > 0) {
                i--;
            }
            //上滚
            if (delta < 0) {
                i++;
            }
            if (this.scrollTop === 0 && i >= 10) {
                var currentHeight = this.scrollHeight;

                var timer = setTimeout(function () {
                    i = 0;
                    clearTimeout(timer);
                }, 300);

                _this.showHistory(id, page, count);
                page++;
                i = 0;
            }
        });
    },
    laypage: function (curr, pages) {
        var _this = this;
        laypage({
            cont: _this.controls.pagesCont, //容器。值支持id名、原生dom对象，jquery对象。【如该容器为】：<div id="page1"></div>
            pages: pages, //通过后台拿到的总页数
            curr: curr + 1 || 0, //当前页
            jump: function (obj, first) { //触发分页后的回调
                if (!first) { //点击跳页触发函数自身，并传递当前页：obj.curr
                    _this.getHistoryList(obj.curr - 1);
                }
            }
        });
    }
    /*=====================================================================================历史记录=====================================================================================*/
};