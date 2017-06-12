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
    <script src="<%=request.getContextPath()%>/resouces/js/common.js"></script>
    <script src="<%=request.getContextPath()%>/resouces/js/plugins/jquery.form.js"></script>
    <script src="<%=request.getContextPath()%>/resouces/js/plugins/jquery-migrate-1.1.1.js"></script>
    <script src="<%=request.getContextPath()%>/resouces/js/util.js"></script>
    <script src="<%=request.getContextPath()%>/resouces/js/plugins/mustache.js"></script>
    <script>
        window.base = '<%=path%>';
        function statistics() {
            var jid = $("#jid").val();
            var date = $("#date").val();
            $("#dataView").html("");
            $.ajax({
                url: base + '/api/weChatMsg/statistics' + '?jid=' + jid + '&date=' + date,
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

</head>

<body>

<table>
    <tr>
        <td>客服Jid</td>
        <td><input type="text" value="maqiumeng@126xmpp" id="jid"></td>
        <td>查询时间</td>
        <td><input type="text" value="2017-06-09" id="date"></td>

        <td><input type="button" onclick="statistics()"></td>
    </tr>
</table>

<table width="60%" border="1" cellspacing="1" cellpadding="1">
    <thead>
    <tr>
        <td>编号</td>
        <td>用户openID</td>
        <td>发送状态</td>
        <td>时间</td>
        <td>发送消息条数</td>
        <td>接收消息条数</td>
    </tr>

    </thead>

    <tbody id="dataView">

    </tbody>

</table>
</html>
