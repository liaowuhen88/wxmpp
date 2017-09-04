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
                <select style="height: 30px;width: 100px;" name="customerName" id="customerName"
                        onchange="showDetail();">
                    <option value="">--请选择--</option>
                    <option value="maqiumeng">马秋萌</option>
                    <option value="wangjing">汪婧</option>
                    <option value="hushuanyue">胡双月</option>
                    <option value="liuya">刘雅</option>
                    <option value="zhangchi">张弛</option>
                </select>
                <input type="hidden" name="userName" id="userName">
            </td>
            <td>查询开始时间</td>
            <td><input type="text" style="height: 30px;" name="beginDate" id="datetimepickerStart"></td>
            <td>查询结束时间</td>
            <td><input type="text" style="height: 30px;" name="endDate" id="datetimepickerEnd"></td>
            <td style="display: ${isCustomerLeader ? 'block' : 'none'}">
                <input type="button" onclick="showDetail()" value="查询">
            </td>
        </tr>
    </table>
</form>

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
                if (res.success) {
                    $('#userListDiv').empty();
                    var arr = res.data;
                    var html = '';
                    for (var i = 0, len = arr.length; i < len; i++) {
                        html += '<li><input type="radio" name="userList" id="' + arr[i] + '">' +
                            '<label for="' + arr[i] + '">' + arr[i] + '</a></label></li>';
                    }
                    $('#userListDiv').append(html);
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


</script>

</html>
