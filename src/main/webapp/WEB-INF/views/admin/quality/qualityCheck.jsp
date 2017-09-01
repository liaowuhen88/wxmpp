<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<jsp:include page="/tpl/alarmStatisticsTpl.jsp"/>
<!DOCTYPE html>
<html lang="zh-CN">
<%
    String path = request.getContextPath();
%>

<head>
    <meta charset="UTF-8"/>
    <title>豆包客服</title>
    <link rel="stylesheet" type="text/css"
          href="<%=request.getContextPath()%>/resouces/css/datetimepicker/jquery.datetimepicker.css"/>
    <script src="<%=request.getContextPath()%>/resouces/js/common.js"></script>
    <script src="<%=request.getContextPath()%>/resouces/js/plugins/jquery.form.js"></script>
    <script src="<%=request.getContextPath()%>/resouces/js/plugins/jquery-migrate-1.1.1.js"></script>
    <script src="<%=request.getContextPath()%>/resouces/js/util.js"></script>
    <script src="<%=request.getContextPath()%>/resouces/js/plugins/mustache.js"></script>
    <script src="<%=request.getContextPath()%>/resouces/js/datetimepicker/jquery.datetimepicker.full.js"></script>
</head>

<body>
<div id="calendar" class="calendar"></div>
<form id="qualityForm">
    <table>
        <tr>
            <td>客服</td>
            <td>
                <select name="customerName" id="customerName">
                    <option value="">--请选择--</option>
                    <option value="maqiumeng">马秋萌</option>
                    <option value="wangjing">汪婧</option>
                    <option value="hushuanyue">胡双月</option>
                    <option value="liuya">刘雅</option>
                    <option value="zhangchi" selected>张弛</option>
                </select>
            </td>
            <td>查询开始时间</td>
            <td><input type="text" name="beginDate" id="datetimepickerStart"></td>
            <td>查询结束时间</td>
            <td><input type="text" name="endDate" id="datetimepickerEnd"></td>
            <td><input type="button" onclick="showDetail()" value="查询"></td>
        </tr>
    </table>
</form>

<div>
    <div style="width: 30%;float: left;">
        <div>用户列表</div>
        <div id="userListDiv" style="border: solid 1px gray;"></div>
    </div>
    <div style="width: 70%;float: left;">
        <div>消息记录</div>
    </div>
</div>

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

        $("#dataView").html("");
        $.ajax({
            url: base + '/api/findAllGuestName',
            type: 'POST',
            data: $('#qualityForm').serialize(),
            success: function (res) {
                if (res.success) {
                    var arr = res.data;
                    for (var i = 0, len = arr.length; i < len; i++) {
                        $('#userListDiv').append('<span><a href="javascript:void(0);" onclick=loadChatMsgList("' + arr[i] + '")>' + arr[i] + '</a></span><br>');
                    }
                }
            },
            error: function (res) {
                alert('查询失败');
                console.log(res);
            }
        });
    };


    function loadChatMsgList(userName) {
        alert(userName);
    };


</script>

</html>
