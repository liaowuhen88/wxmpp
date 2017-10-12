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
        <td>
            <select id="jid">
                <option value="">全部</option>
                <option value="yutao@126xmpp">于涛</option>
                <option value="maqiumeng@126xmpp">马秋萌</option>
                <option value="wangjing@126xmpp">汪婧</option>
                <option value="hushuangyue@126xmpp">胡双月</option>
                <option value="liuya@126xmpp">刘雅</option>
                <option value="zhangchi@126xmpp">张弛</option>
            </select>
        </td>
        <td>查询开始时间</td>
        <td><input type="text" id="datetimepickerStart"></td>
        <td>查询结束时间</td>
        <td><input type="text" id="datetimepickerEnd"></td>

        <td><input type="button" onclick="statistics()" value="查询"></td>
    </tr>
</table>

<table width="100%" border="1" cellspacing="1" cellpadding="1">
    <thead>
    <tr align="center">
        <td>编号</td>
        <td>用户openID</td>
        <td>客服首次发送消息时间</td>
        <td class="totalSuccessTd">客服发送消息成功<span id="totalSuccess" style="color:blue;"></span>条数</td>
        <td id="totalFailTd">客服发送消息失败<span id="totalFail" style="color:blue;"></span>条数</td>
        <td id="totalSendMsgTd">用户发送消息<span id="totalSendMsg" style="color:blue;"></span>条数</td>
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

        if (!startDate || !endDate) {
            alert('请选择日期');
            return false;
        }

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
                    claculateTotal();
                }
            },
            error: function (res) {
                alert('查询失败');
                console.log(res);
            }
        });
    }
    ;

    function claculateTotal() {//统计各列的各
        var successFromCount = 0;

        var fun = function (clzz) {
            var count = 0;
            $(clzz).each(function () {
                count += Number($.trim($(this).text() || 0));
            });
            return count;
        };

        $('#totalSuccess').html(fun('.successFromCount'));
        $('#totalFail').html(fun('.failFromCount'));
        $('#totalSendMsg').html(fun('.successToCount'));
    }

</script>

</html>
