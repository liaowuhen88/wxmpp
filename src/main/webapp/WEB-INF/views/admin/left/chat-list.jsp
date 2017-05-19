<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<jsp:include page="/tpl/onlinefriendListTpl.jsp"/>
<jsp:include page="/tpl/backupfriendListTpl.jsp"/>
<jsp:include page="/tpl/waitfriendListTpl.jsp"/>
<div class="chat-list">
    <div class="chat-list-item">
        <div class="chat-title">
            对话列表
            <%--<select name="access" id="access">
            <option value="1">1</option>
            <option value="2">2</option>
            <option value="3">3</option>
            <option value="4">4</option>
            <option value="5">5</option>
            <option value="6">6</option>
            <option value="7">7</option>
            <option value="8">8</option>
            <option value="9">9</option>
            <option value="10">10</option>
            </select>--%>
        </div>
        <ul class="chat-contact-list" id="friendList"></ul>
        <ul class="chat-contact-list" id="backupFriendList"></ul>
        <ul class="chat-contact-list" id="historyFriendList"></ul>
        <%-- 转接下线 --%>
        <ul class="chat-contact-list" id="changeOfflineFriendList"></ul>

    </div>
    <div class="holdListBtn" id="holdListWeiXInBtn">接入微信用户</div>
    <div class="holdListBtn" id="holdListBtn">接入对话</div>
</div>
