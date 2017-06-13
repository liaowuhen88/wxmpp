<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<jsp:include page="/tpl/weChatStatisticsTpl.jsp"/>
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
<table>
    <tr>
        <td>客服Jid</td>
        <td><input type="text" value="@126xmpp" id="jid"></td>
        <td>查询开始时间</td>
        <td><input type="text" id="datetimepickerStart"></td>
        <td>查询结束时间</td>
        <td><input type="text" id="datetimepickerEnd"></td>

        <td><input type="button" onclick="statistics()" value="查询"></td>
    </tr>
</table>

<table width="60%" border="1" cellspacing="1" cellpadding="1">
    <thead>
    <tr>
        <td>编号</td>
        <td>用户openID</td>
        <td>客服首次发送消息时间</td>
        <td>客服发送消息成功条数</td>
        <td>客服发送消息失败条数</td>
        <td>用户发送消息条数</td>
    </tr>

    </thead>

    <tbody id="dataView">

    </tbody>

</table>

<script>
    window.base = '<%=path%>';

    $('#datetimepickerStart').datetimepicker({
        //yearOffset:222,
        lang: 'ch',
        timepicker: false,
        format: 'Y-m-d',
        formatDate: 'Y-m-d'
        //minDate:'-1970/01/02', // yesterday is minimum date
        //maxDate:'+1970/01/02' // and tommorow is maximum date calendar
    });
    $('#datetimepickerEnd').datetimepicker({
        lang: 'ch',
        timepicker: false,
        format: 'Y-m-d',
        formatDate: 'Y-m-d'
    });

    function statistics() {
        var jid = $("#jid").val();
        var startDate = $("#datetimepickerStart").val();
        var endDate = $("#datetimepickerEnd").val();

        $("#dataView").html("");
        $.ajax({
            url: base + '/api/weChatMsg/statistics' + '?jid=' + jid + '&startDate=' + startDate + '&endDate=' + endDate,
            type: 'POST',
            success: function (res) {
                if (res.success) {
                    for (var index in  res.data) {
                        // console.log(res.data[json]);
                        var json = res.data[index];
                        json.id = index;
                        json.sendTime = myUtils.formatDate(json.sendTime);

                        if (json.msgStatus == -1) {
                            json.msgStatus = "发送失败";
                            json.toCount = null;
                        } else if (json.msgStatus == 1) {
                            json.msgStatus = "发送成功";
                        }

                        myUtils.renderDivAdd('weChatStatisticsTpl', json, 'dataView');
                    }

                }
            },
            error: function (res) {
                alert('查询失败');
                console.log(res);
            }
        });
    }

</script>

</html>
