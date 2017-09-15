<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<jsp:include page="/tpl/chatContainerTpl.jsp"/>
<!DOCTYPE html>
<html lang="zh-CN">
<%
    String path = request.getContextPath();
%>

<head>
    <meta charset="UTF-8"/>
    <title>豆包客服</title>
    <link rel="stylesheet" href="<%=request.getContextPath()%>/resouces/styles/admin.css">
    <jsp:include page="/tpl/common/commonjs.jsp"/>
    <link rel="stylesheet" type="text/css"
          href="<%=request.getContextPath()%>/resouces/css/datetimepicker/jquery.datetimepicker.css"/>
    <script src="<%=request.getContextPath()%>/resouces/js/common.js"></script>
    <script src="<%=request.getContextPath()%>/resouces/js/plugins/jquery.form.js"></script>
    <script src="<%=request.getContextPath()%>/resouces/js/plugins/jquery-migrate-1.1.1.js"></script>
    <script src="<%=request.getContextPath()%>/resouces/js/util.js"></script>
    <script src="<%=request.getContextPath()%>/resouces/js/plugins/mustache.js"></script>
    <script src="<%=request.getContextPath()%>/resouces/js/datetimepicker/jquery.datetimepicker.full.js"></script>


    <script src="<%=request.getContextPath()%>/resouces/js/webuploader/webuploader.js"></script>
    <script src="<%=request.getContextPath()%>/resouces/js/webuploader/uploadimg.js"></script>
    <script src="<%=request.getContextPath()%>/resouces/ajax/common.js"></script>
    <script src="<%=request.getContextPath()%>/resouces/scripts/laypage/laypage.js"></script>
    <script src="<%=request.getContextPath()%>/resouces/scripts/chat.js"></script>
    <script src="<%=request.getContextPath()%>/resouces/scripts/laypage/laypage.js"></script>
    <script src="<%=request.getContextPath()%>/resouces/scripts/history.js"></script>
    <script src="<%=request.getContextPath()%>/resouces/scripts/set.js"></script>
    <script src="<%=request.getContextPath()%>/resouces/scripts/leaveMessage.js"></script>
    <script src="<%=request.getContextPath()%>/resouces/js/wechatface.js"></script>

    <style>
        #allMsgHistoryContainer .avatar {
            display: none;
        }

        #userListDiv {
            width: 300px;
        }

        #userListDiv .item {
        }

        #userListDiv .item span {
            display: inline-block;
        / / background-color: #eee;
            margin-top: 5px;
            padding: 0 5px;
        }

        #userListDiv .item .blue {
            color: #fff;
            background-color: #188FC1;
        }

        #userListDiv .item .eucalyptus {
            color: #fff;
            background-color: #21A85B;
        }

        #userListDiv .item .cinnabar {
            color: #fff;
            background-color: #EB3E33;
        }

        #userListDiv .item .red {
            color: #fff;
            background-color: #E74235;
        }

        .select-list {
            margin: 5px 0;
            padding: 0;
        }

        .select-list li {
            display: inline-block;
        }

        .select-list li span {
            color: #EB3E33;
        }

        .select-list li input {
            display: none;
        }

        .select-list li input:checked + label {
            background-color: #188FC1;
            color: #fff;
        }

        .select-list li input:checked + label span {
            color: inherit;
        }

        .select-list li label {
            display: inline-block;
            padding: 0 5px;
        }
    </style>
</head>

<body>
<div id="calendar" class="calendar"></div>
<div><h3>&nbsp;&nbsp;质检</h3></div>
<form id="qualityForm">
    <table>
        <tr>
            <td>客服</td>
            <td>
                <select style="height: 30px;width: 100px;" name="customerName" id="customerName">
                    <option value="">--请选择--</option>
                    <option value="maqiumeng">马秋萌</option>
                    <option value="wangjing">汪婧</option>
                    <option value="hushuangyue">胡双月</option>
                    <option value="liuya">刘雅</option>
                    <option value="zhangchi">张弛</option>
                    <option value="yutao">yutao</option>
                </select>
                <input type="hidden" name="userName" id="userName">
            </td>
            <td>查询开始时间</td>
            <td><input type="text" style="height: 30px;" name="beginDate" id="datetimepickerStart"></td>
            <td>查询结束时间</td>
            <td><input type="text" style="height: 30px;" name="endDate" id="datetimepickerEnd"></td>
            <%-- <td style="display: ${isCustomerLeader ? 'block' : 'none'}">--%>
            <td>
                <input type="button" onclick="showDetail()" value="查询">
            </td>
        </tr>
    </table>
</form>

<div class="select">
    <ul class="select-list">
        <li>
            <input type="radio" id="1" name="select" checked>
            <label for="1">全部</label>
        </li>
        <li>
            <input type="radio" id="2" name="select" onclick="selectByDate(2)">
            <label for="2">今天</label>
        </li>
        <li>
            <input type="radio" id="3" name="select" onclick="selectByDate(3)">
            <label for="3">昨天</label>
        </li>
        <li>
            <input type="radio" id="4" name="select" onclick="selectByDate(4)">
            <label for="4">前天</label>
        </li>
        <li>
            <input type="radio" id="5" name="select" onclick="selectByDate(5)">
            <label for="5">一周以内</label>
        </li>
        <li>
            <input type="radio" id="6" name="select" onclick="selectByDate(6)">
            <label for="6">一个月以内</label>
        </li>
    </ul>

    <ul class="select-list">
        <li>
            <input type="radio" id="100" name="select" checked>
            <label for="100">全部</label>
        </li>
        <li>
            <input type="radio" id="200" name="select">
            <label for="200">用户留言</label>
        </li>
        <li>
            <input type="radio" id="300" name="select">
            <label for="300">微信主动咨询</label>
        </li>
        <li>
            <input type="radio" id="400" name="select">
            <label for="400">H5主动咨询</label>
        </li>
        <li>
            <input type="radio" id="500" name="select">
            <label for="500">客服主动接入</label>
        </li>
        <li>
            <input type="radio" id="600" name="select">
            <label for="600">进入客服</label>
        </li>
    </ul>
</div>

<div>
    <div id="serviceUserDiv">
        <div>用户列表</div>
        <ul id="userListDiv"></ul>
    </div>
    <div style="margin-left: 300px;">
        <div id="showHistory" data-id="" style="display: none;">
            <div class="chat-window">
                <div class="chat-screen">
                    <div class="chat-title">
                        客服<span id="msgFrom"></span>和游客【<span id="msgTo"></span>】 的聊天记录
                    </div>
                    <div class="chat-show" id="showHistoryChatShow">
                        <div class="chat-timeline" id="showHistoryChatTimeline">
                            <div id="allMsgHistoryContainer"></div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>
<style>
    #serviceUserDiv {
        width: 300px;
        float: left;
        display: none;
    }

    #userListDiv {
        background-color: #fff;
        margin: 0;
        padding: 0;
        border: solid 1px #ddd;
        height: 450px;
        overflow-y: auto;
        list-style: none;
    }

    input[name="userList"] {
        display: none;
    }

    input[name="userList"] + label {
        display: block;
        line-height: 30px;
        padding-left: 10px;
        cursor: pointer;
    }

    input[name="userList"]:checked + label {
        background-color: #ddd;
    }
</style>
<script>
    window.base = '<%=path%>';

    $('#datetimepickerStart').datetimepicker({
        //yearOffset:222,
        lang: 'ch',
        timepicker: false,
        format: 'Y-m-d 00:00:00',
        formatDate: 'Y-m-d 00:00:00'
        //minDate:'-1970/01/02', // yesterday is minimum date
        //maxDate:'+1970/01/02' // and tommorow is maximum date calendar
    });
    $('#datetimepickerEnd').datetimepicker({
        lang: 'ch',
        timepicker: false,
        format: 'Y-m-d 23:59:59',
        formatDate: 'Y-m-d 23:59:59'
    });

    /**
     * 查询
     * @returns {boolean}
     */
    function showDetail() {
        if (!$('#customerName').val()) {
            alert('请选择客服');
            return false;
        }
        var startDate = $("#datetimepickerStart").val();
        var endDate = $("#datetimepickerEnd").val();
        if (!startDate || !endDate) {
            alert('请选择日期');
            return false;
        }
        $('#serviceUserDiv').show();
        $("#dataView").html("");
        $.ajax({
            url: base + '/api/findAllGuestName',
            type: 'POST',
            data: $('#qualityForm').serialize(),
            success: function (res) {
                res.success = true;
                if (res.success) {
                    $('#userListDiv').empty();
                    debugger;

                    var res = {
                        "success": true,
                        "code": 0,
                        "data": [{
                            "id": 562,
                            "useraccountid": 306,
                            "pname": "于涛",
                            "mobile": "18300012133",
                            "email": "yutao133@163.com",
                            "sex": 1,
                            "birthday": 520614000000,
                            "homeaddress": "河北保定22",
                            "identitytype": 1,
                            "ct": 1443513406000,
                            "ut": 1465207445000,
                            "attr": "豆包网",
                            "idcardtype": 1,
                            "idcard": "130603198605020918",
                            "prov": "河北",
                            "city": "保定",
                            "dist": "北市区"
                        }, {"pname": "ort5yw2yvgrs98-rg97ttfw08mjw"}, {
                            "id": 92411,
                            "useraccountid": 38907,
                            "pname": "李成",
                            "mobile": "13718013880",
                            "email": "1398752165@qq.com",
                            "sex": 2,
                            "birthday": 642006000000,
                            "identitytype": 1,
                            "ct": 1476171612000,
                            "ut": 1479719688000,
                            "attr": "北京众信易保科技有限公司",
                            "idcardtype": 1,
                            "idcard": "130633199005072143",
                            "relationship": "本人"
                        }, {
                            "id": 584,
                            "useraccountid": 317,
                            "pname": "付佳",
                            "mobile": "15311230121",
                            "email": "",
                            "sex": 2,
                            "birthday": 621270000000,
                            "homeaddress": "",
                            "identitytype": 1,
                            "ct": 1443513407000,
                            "ut": 1450681938000,
                            "attr": "豆包网",
                            "idcardtype": 1,
                            "idcard": "232101198909092043"
                        }, {
                            "id": 1530,
                            "useraccountid": 733,
                            "pname": "李晓萌",
                            "mobile": "15727384671",
                            "email": "xiaomeng.li@baodanyun-inc.com",
                            "sex": 2,
                            "birthday": 555001200000,
                            "homeaddress": "北京市朝阳区天创世缘315栋401",
                            "healthdegree": 0,
                            "identitytype": 1,
                            "ct": 1450077579000,
                            "ut": 1505447564000,
                            "attr": "北京众信易保科技有限公司",
                            "msalary": "0.0",
                            "pamount": 0.0,
                            "pcontractid": 0,
                            "idcardtype": 1,
                            "idcard": "130922198707102848"
                        }, {
                            "id": 168113,
                            "useraccountid": 73849,
                            "pname": "胡波",
                            "mobile": "18511072313",
                            "sex": 3,
                            "birthday": 601660800000,
                            "identitytype": 1,
                            "ct": 1491040435000,
                            "ut": 1491040435000,
                            "attr": "北京众信易保科技有限公司",
                            "idcardtype": 1,
                            "idcard": "421222198901256017",
                            "relationship": "本人"
                        }, {
                            "id": 92409,
                            "useraccountid": 38906,
                            "pname": "张文超",
                            "mobile": "18649041578",
                            "email": "296558063@qq.com",
                            "sex": 1,
                            "birthday": 626716800000,
                            "identitytype": 1,
                            "ct": 1476171612000,
                            "ut": 1479720577000,
                            "attr": "北京众信易保科技有限公司",
                            "idcardtype": 1,
                            "idcard": "14233019891111621X",
                            "relationship": "本人"
                        }, {
                            "id": 92401,
                            "useraccountid": 38904,
                            "pname": "周汉涛",
                            "mobile": "15801178892",
                            "sex": 1,
                            "birthday": 569347200000,
                            "identitytype": 1,
                            "ct": 1476171612000,
                            "ut": 1476171612000,
                            "attr": "北京众信易保科技有限公司",
                            "idcardtype": 1,
                            "idcard": "130982198801177316",
                            "relationship": "本人"
                        }, {"pname": "oah_qsi4ca81eg9_xhdq-2vdvu1q"}, {"pname": "oah_qsorlouio2ut-zm8jnp3uupm"}]
                    };
                    var arr = res.data;

                    var htmlArr = [];

                    for (var i = 0, len = arr.length; i < len; i++) {
                        var user = arr[i];
                        if (user.pname && user.pname == 'undefined') {
                            continue;
                        }
                        var templateHtml = '<li><input type="radio" name="userList" id="@mobile">' +
                            '<label for="@mobile" class="item">' +
                            '@index<span>@uid</span>' +
                            '<span class="blue">@name</span>' +
                            '<span class="eucalyptus">@age</span>' +
                            '<span class="cinnabar">@mobile</span><br>' +
                            '<span class="red">@company</span>' +
                            '</label><hr></li>';
                        if (!$.isNumeric(user.mobile)) {
                            templateHtml = '<li><input type="radio" name="userList" id="@mobile">' +
                                '<label for="@mobile" class="item">' +
                                '@index<span class="blue">@name</span></br>' +
                                '</label><hr></li>';
                        }
                        templateHtml = templateHtml.replace(/@mobile/g, user.mobile).replace('@index', i + 1)
                            .replace('@uid', user.useraccountid).replace('@name', user.pname)
                            .replace('@age', jsGetAge(user.birthday)).replace('@company', user.attr);

                        htmlArr.push(templateHtml);
                    }
                    $('#userListDiv').append(htmlArr.join(""));
                    $('#userListDiv').on('click', '[name="userList"]', function () {
                        loadChatMsgList(this.id)
                    })
                }
            },
            error: function (res) {
                alert('查询失败');
                console.log(res);
            }
        });
    };


    /**
     * 点击用户加载聊天记录
     * @param userName
     */
    function loadChatMsgList(userName) {
        if (!$('#customerName').val()) {
            alert('请选择客服');
            return false;
        }

        $('#showHistory').show();
        var startDate = $("#datetimepickerStart").val();
        var endDate = $("#datetimepickerEnd").val();
        if (!startDate || !endDate) {
            alert('请选择日期');
            return false;
        }
        $('#allMsgHistoryContainer').empty();

        var customername = $('#customerName').val();
        $('#userName').val(userName); //用户名
        $('#msgFrom').text(customername);
        $('#msgTo').text($('#userName').val());

        $.ajax({
            url: base + '/api/loadChatMsgFromUser',
            type: 'POST',
            data: $('#qualityForm').serialize(),
            success: function (res) {
                if (res.success) {
                    var data = res.data;
                    myUtils.DBRenderDivAll(customername, data, 'allMsgHistoryContainer', function () {
                        console.log("DBRenderDivAll");
                    });
                }
            },
            error: function (res) {
                alert('查询失败');
                console.log(res);
            }
        });
    };

    /*根据出生日期算出年龄*/
    function jsGetAge(timestamp) {
        var returnAge;
        var birthDate = new Date(timestamp);
        var birthYear = birthDate.getFullYear();
        var birthMonth = birthDate.getMonth() + 1;
        var birthDay = birthDate.getDate();

        d = new Date();
        var nowYear = d.getFullYear();
        var nowMonth = d.getMonth() + 1;
        var nowDay = d.getDate();

        if (nowYear == birthYear) {
            returnAge = 0;//同年 则为0岁
        }
        else {
            var ageDiff = nowYear - birthYear; //年之差
            if (ageDiff > 0) {
                if (nowMonth == birthMonth) {
                    var dayDiff = nowDay - birthDay;//日之差
                    if (dayDiff < 0) {
                        returnAge = ageDiff - 1;
                    }
                    else {
                        returnAge = ageDiff;
                    }
                }
                else {
                    var monthDiff = nowMonth - birthMonth;//月之差
                    if (monthDiff < 0) {
                        returnAge = ageDiff - 1;
                    }
                    else {
                        returnAge = ageDiff;
                    }
                }
            }
            else {
                returnAge = -1;//返回-1 表示出生日期输入错误 晚于今天
            }
        }

        return returnAge;//返回周岁年龄
    }

    function selectByDate(type) {
        var start, end = "";
        var now = myUtils.formatDate(new Date(), "yyyy-MM-dd");
        switch (type) {
            case 1:
                start = end = getthedate(now, 0); //今天
                break;
            case 2:
                start = end = getthedate(now, -1); //昨天
                break;
            case 3:
                start = end = getthedate(now, -2); //前天
                break;
            case 4:
                start = end = getthedate(now, -7); //一周
                break;
            case 5:
                start = end = getthedate(now, -30);//一个月
                break;
            default:
                break;
        }

        $('#datetimepickerStart').val(start);
        $('#datetimepickerEnd').val(end);
    }

    function getthedate(dd, dadd) {
        var a = new Date(dd)
        a = a.valueOf()
        a = a + dadd * 24 * 60 * 60 * 1000
        a = new Date(a);
        var m = a.getMonth() + 1;
        if (m.toString().length == 1) {
            m = '0' + m;
        }
        var d = a.getDate();
        if (d.toString().length == 1) {
            d = '0' + d;
        }
        return a.getFullYear() + "-" + m + "-" + d;
    }

</script>

</html>
