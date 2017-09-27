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
        .total {
            color: red;
        }
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

        #mask {
            width: 100%;
            height: 100%;
            position: fixed;
            z-index: 999999;
            display: none;
            background: url("<%=request.getContextPath()%>/resouces/images/loading.gif") no-repeat center;
            background-color: rgba(255, 255, 255, .6);
        }
    </style>
</head>

<body>
<div id="mask"></div>


<form action="<%=request.getContextPath()%>/api/customer/uploadExcel" method="post" enctype="multipart/form-data"
      onsubmit="return check();">
    <div style="margin: 30px;">
        <input id="excel_file" type="file" name="file" size="80"/>
        <input id="excel_button" type="submit" value="导入Excel"/></div>
</form>

<div id="calendar" class="calendar"></div>
<div><h3>&nbsp;&nbsp;质检</h3></div>
<form id="qualityForm">
    <table>
        <tr>
            <td>客服</td>
            <td>
                <select style="height: 30px;width: 100px;" name="customerName" id="customerName"
                        onchange="showDetail()">
                    <option value="">全部</option>
                    <option value="maqiumeng@126xmpp">马秋萌</option>
                    <option value="wangjing@126xmpp">汪婧</option>
                    <option value="hushuangyue@126xmpp">胡双月</option>
                    <option value="liuya@126xmpp">刘雅</option>
                    <option value="zhangchi@126xmpp">张弛</option>
                    <option value="yutao@126xmpp">yutao</option>
                </select>
                <input type="hidden" name="userName" id="userName">
            </td>
            <td>查询开始时间</td>
            <td><input type="text" style="height: 30px;" name="beginDate" id="datetimepickerStart" value="${startTime}">
            </td>
            <td>查询结束时间</td>
            <td><input type="text" style="height: 30px;" name="endDate" id="datetimepickerEnd" value="${endTime}"></td>
            <td>
                <input type="button" onclick="showDetail()" value="查询">
            </td>
        </tr>
    </table>
</form>

<div class="select">
    <ul class="select-list">
        <li>
            <input type="radio" id="1" name="selectDate" checked onclick="selectByDate(1)">
            <label for="1">今天</label>
        </li>
        <li>
            <input type="radio" id="2" name="selectDate" onclick="selectByDate(2)">
            <label for="2">昨天</label>
        </li>
        <li>
            <input type="radio" id="3" name="selectDate" onclick="selectByDate(3)">
            <label for="3">前天</label>
        </li>
        <li>
            <input type="radio" id="4" name="selectDate" onclick="selectByDate(4)">
            <label for="4">一周以内</label>
        </li>
    </ul>


    <ul class="select-list">
        <li>
            <input type="radio" id="600" name="select" checked onclick="loadEvtData(5)">
            <label for="600">进入客服(<span class="total" id="enterCount">${totalMap.enterCount}</span>)</label>
        </li>
        <li>
            <input type="radio" id="200" name="select" onclick="loadEvtData(1)">
            <label for="200">用户留言(<span class="total" id="leaveCount">${totalMap.leaveCount}</span>)</label>
        </li>
        <li>
            <input type="radio" id="300" name="select" onclick="loadEvtData(2)">
            <label for="300">微信主动咨询(<span class="total" id="wxActiveCount">${totalMap.wxActiveCount}</span>)</label>
        </li>
        <%-- <li>
             <input type="radio" id="400" name="select" onclick="loadEvtData(3)">
             <label for="400">H5主动咨询(<span class="total" id="h5Count">${totalMap.h5Count}</span>)</label>
         </li>--%>
        <li>
            <input type="radio" id="500" name="select" onclick="loadEvtData(4)">
            <label for="500">客服主动接入(<span class="total" id="wxPassiveCount">${totalMap.wxPassiveCount}</span>)</label>
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
        var startDate = $("#datetimepickerStart").val();
        var endDate = $("#datetimepickerEnd").val();
        if (!startDate || !endDate) {
            alert('请选择日期');
            return false;
        }
        statisticCount(); //更新数量
        loadEvtData($('#code').val());
    };


    /**
     * 点击用户加载聊天记录
     * @param userName
     */
    function loadChatMsgList(userName) {
        if (!userName) {
            alert('用户电话为空');
            return false;
        }

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
        var date, start, end = "";
        var start = " 00:00:00";
        var end = " 23:59:59";
        var now = myUtils.formatDate(new Date(), "yyyy-MM-dd");
        switch (type) {
            case 1:
                date = getthedate(now, 0); //今天
                break;
            case 2:
                date = getthedate(now, -1); //昨天
                break;
            case 3:
                date = getthedate(now, -2); //前天
                break;
            case 4:
                date = getthedate(now, -7); //一周
                break;
            case 5:
                date = getthedate(now, -30);//一个月
                break;
            default:
                break;
        }
        start = date + start;
        if (type == 2) {
            end = date + end;
        } else {
            end = now + end;
        }
        $('#datetimepickerStart').val(start);
        $('#datetimepickerEnd').val(end);

        showDetail();
    }

    function getthedate(dd, dadd) {
        var a = new Date(dd);
        a = a.valueOf();
        a = a + dadd * 24 * 60 * 60 * 1000;
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
    };

    function statisticCount() {
        $.ajax({
            url: base + '/api/statisticCount',
            type: 'POST',
            data: $('#qualityForm').serialize(),
            success: function (res) {
                if (res.success) {
                    var data = res.data;
                    for (var key in data) {
                        $('#' + key).text(data[key]);
                    }
                }
            },
            error: function (res) {
                alert('查询失败');
                console.log(res);
            }
        });
    };

    function loadEvtData(code) {
        $('#mask').show();

        $('#code').val(code);
        $('#userListDiv').empty();
        $('#serviceUserDiv').show();
        $('#allMsgHistoryContainer').empty();
        $("#dataView").html("");
        $.ajax({
            url: base + '/api/loadEvtData/' + code,
            type: 'POST',
            data: $('#qualityForm').serialize(),
            success: function (res) {
                $('#mask').hide();

                res.success = true;
                if (res.success) {
                    $('#userListDiv').empty();
                    var arr = res.data;
                    if (!arr) {
                        return;
                    }
                    var htmlArr = [];

                    for (var i = 0, len = arr.length; i < len; i++) {
                        var user = arr[i];
                        if (!user) {
                            continue;
                        }
                        if (user.pname && user.pname == 'undefined') {
                            continue;
                        }
                        var templateHtml = '<li><input type="radio" name="userList" id="@mobile" onclick=loadChatMsgList("@mobile")>' +
                            '<label for="@mobile" class="item">' +
                            '@index)<span style="color: #007fff">@uid</span>' +
                            '<span class="blue">@name</span>' +
                            '<span class="eucalyptus">@age</span>' +
                            '<span class="blue">@mobile</span><br>' +
                            '<span class="red">@company</span>' +
                            '</label><hr></li>';

                        if (user.useraccountid == 0) {//openid
                            templateHtml = '<li><input type="radio" name="userList" id="@mobile"  onclick=loadChatMsgList("@mobile")>' +
                                '<label for="@mobile" class="item">' +
                                '@index)&nbsp;&nbsp;<span class="blue">@name</span></br>' +
                                '</label><hr></li>';
                        }
                        var age = jsGetAge(user.birthday);
                        templateHtml = templateHtml.replace(/@mobile/g, user.mobile || '').replace('@index', i + 1)
                            .replace('@uid', user.useraccountid || '').replace('@name', user.pname || '')
                            .replace('@age', age == -1 ? '' : age + '岁').replace('@company', user.attr || '');

                        htmlArr.push(templateHtml);
                    }
                    $('#userListDiv').append(htmlArr.join(""));
                }
            },
            error: function (res) {
                alert('查询失败');
                console.log(res);
                $('#mask').hide();
            }
        });
    };


    function check() {
        var excel_file = $("#excel_file").val();
        alert(excel_file);
        if (excel_file == "" || excel_file.length == 0) {
            return false;
        } else {
            return true;
        }
    }

</script>

</html>
