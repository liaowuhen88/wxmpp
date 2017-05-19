/**
 * 设置页面
 * @constructor
 */
'use strict';

var Set = function (options) {
    options = options || {};
    //基础配置
    this.currentId = options.currentId || window.currentId;
    this.base = options.base || window.base;

    this.cjid = options.cjid || window.cjid;

    //控件
    this.icon = $('#icon');
    this.nickName = $('#nickName');
    this.userName = $('#userName');
    this.loginUsername = $('#loginUsername');
    this.workNumber = $('#workNumber');
    this.desc = $('#desc');
    //修改密码控件
    this.oldPWD = $('#oldPWD');
    this.newPWD = $('#newPWD');
    this.confirmPWD = $('#confirmPWD');
    this.changeProfileBtn = $('#changeProfileBtn');
    this.changePWD = $('#changePWD');
    this.changePWDConfirm = $('#changePWDConfirm');
    this.setBtn = $('#set');
    this.interface = {
        changeProfile: window.base + '/api/upCustomerInfo' || options.interface.changeProfile,
        changePassword: window.base + '/api/upCustomerPwd' || options.interface.changePassword
    };
};

Set.prototype = {
    init: function () {
        //初始化用户信息
        this.getProfile();
        //this.setProfile(userInfo);
        //修改用户信息事件绑定
        this.changeProfile();
        //修改密码
        this.changePasswordBind();
        //模态框绑定
        this.modalBind();
        this.uploadAvatar();
    },
    //获取用户信息
    getProfile: function () {
        var data = {};
        var _this = this;
        $.ajax({
            url: _this.interface.changeProfile + '?cjid=' + _this.currentId,
            type: 'POST',
            success: function (res) {
                if(res.data){
                    data = res.data;
                    _this.setProfile(data);
                }else{
                    alert('数据加载失败，请重试');
                }
            },
            error: function () {
                alert('系统错误，请刷新重试');
            }
        });
    },
    //设置用户信息
    setProfile: function (data) {
        console.log();
        if (data.vCard) {
            if (data.vCard !== null && data.vCard.icon !== '' && data.vCard.icon !== 'ic') {
                this.icon.attr('src', data.vCard.icon);
            }
            if (data.vCard.nickName !== null && data.vCard.nickName !== '' && data.vCard.nickName !== 'null') {
                this.nickName.val(data.vCard.nickName);
            }
            if (data.vCard.userName !== null && data.vCard.userName !== '') {
                this.userName.val(data.vCard.userName);
            }
            if (data.vCard.workNumber !== null && data.vCard.workNumber !== '' && data.vCard.workNumber !== 'null') {
                this.workNumber.val(data.vCard.workNumber);
            }
            if (data.vCard.desc !== null && data.vCard.desc !== '' && data.vCard.desc !== 'null') {
                this.desc.text(data.vCard.desc);
            }

        }

        if (data.user) {
            if (data.user.loginUsername !== null && data.user.loginUsername !== '' && data.user.loginUsername !== 'null') {
                this.loginUsername.text(data.user.loginUsername);
            }

        }

    },
    //修改用户信息
    changeProfile: function () {
        var _this = this;
        this.changeProfileBtn.on('click', function () {
            var nickName = _this.nickName.val();
            var userName = _this.userName.val();
            var workNumber = _this.workNumber.val();
            var desc = _this.desc.val();
            $.ajax({
                url: _this.interface.changeProfile + '?cjid=' + _this.currentId + '&nickName=' + nickName + '&userName=' + userName + '&workNumber=' + workNumber + '&desc=' + desc,
                type: 'POST',
                success: function (res) {
                    alert('修改成功');
                },
                error: function () {
                    alert('修改信息失败，请重试');
                }
            });
        });
    },
    //修改密码
    changePassword: function (oldPWD, newPWD, confirmPWD) {
        var _this = this;
        oldPWD = this.oldPWD.val();
        newPWD = this.newPWD.val();
        confirmPWD = this.confirmPWD.val();
        $.ajax({
            url: _this.interface.changePassword + '?cjid=' + _this.currentId + '&oldPWD=' + oldPWD + '&newPWD=' + newPWD + '&confirmPWD=' + confirmPWD,
            type: 'POST',
            success: function (res) {
                if(res.success){
                    $('[data-target="changePWD"]').hide();
                    _this.oldPWD.val('');
                    _this.newPWD.val('');
                    _this.confirmPWD.val('');
                    alert('密码修改成功');
                }else {
                    alert(res.msg);
                }
            },
            error: function () {
                alert('修改信息失败，请重新再试');
            }
        });
    },
    //修改密码事件绑定
    changePasswordBind: function () {
        var _this = this;
        this.changePWDConfirm.on('click', function (e) {
            var oldPWD = _this.oldPWD.val();
            var newPWD = _this.newPWD.val();
            var confirmPWD = _this.confirmPWD.val();
            _this.changePassword(oldPWD, newPWD, confirmPWD);
            e.stopPropagation();
        });
    },
    //模态框绑定
    modalBind: function () {
        this.changePWD.modal();
        this.setBtn.modal();
    },
    //上传用户头像
    uploadAvatar: function () {
        var chat = new xChat();
        var _this = this;
        // 初始化上传头像
        chat.uploadAvatar('avatarUpload', "/up/customer/img/" + window.currentId + "?name=123123.png", function (response) {
            var src = "http://test.17doubao.cn/upload/xmppDownLoad/downLoad?key=" + response.src;
            $.ajax({
                url: _this.interface.changeProfile + '?cjid=' + window.currentId + '&icon=' + src,
                type: 'POST',
                success: function (res) {
                    $('#icon').attr('src', src);
                    alert('修改成功');
                },
                error: function () {
                    alert('修改信息失败，请重试');
                }
            });
        });
    }
};



