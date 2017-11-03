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
<form id="alarmForm">
    <table>
        <tr>
            <td>客服</td>
            <td>
                <select name="customerName" id="customerName">
                    <option value="">全部</option>
                    <option value="maqiumeng">马秋萌</option>
                    <option value="wangjing">汪婧</option>
                    <option value="hushuanyue">胡双月</option>
                    <option value="liuya">刘雅</option>
                    <option value="zhangchi">张弛</option>
                    <option value="zhangfuliang">张福亮</option>
                </select>
            </td>
            <td>查询开始时间</td>
            <td><input type="text" name="beginDate" id="datetimepickerStart"></td>
            <td>查询结束时间</td>
            <td><input type="text" name="endDate" id="datetimepickerEnd"></td>

            <td><input type="button" onclick="statistics()" value="查询"></td>
        </tr>
    </table>
</form>

<table width="100%" border="1" cellspacing="1" cellpadding="1">
    <thead>
    <tr align="center">
        <td>客服名</td>
        <td>5分钟未回复消息条数</td>
        <td>15分钟未回复消息条数</td>
        <td>30分钟未回复消息条数</td>
    </tr>
    </thead>
    <tbody id="dataView" align="center">

    </tbody>
</table>

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

    function statistics() {
        var startDate = $("#datetimepickerStart").val();
        var endDate = $("#datetimepickerEnd").val();
        if (!startDate || !endDate) {
            alert('请选择日期');
            return false;
        }

        $("#dataView").html("");
        $.ajax({
            url: base + '/api/alarmStastistics',
            type: 'POST',
            data: $('#alarmForm').serialize(),
            success: function (res) {
                debugger;
                if (res.success) {
                    var list = res.data;
                    for (var i in  list) {
                        myUtils.renderDivAdd('alarmStatisticsTpl', list[i], 'dataView');
                    }
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
