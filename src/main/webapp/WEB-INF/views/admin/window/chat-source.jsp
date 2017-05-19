<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<jsp:include page="/tpl/visitorProperties.jsp"/>
<div class="chat-source">
    <i class="source-icon icon-macos"></i>
    <i class="source-icon icon-chrome"></i>
    <span class="fangshi">直接访问</span>
  <%--<span class="ip">--%>
      <%--<i class="source-icon icon-ip"></i>--%>
      <%--192.168.1.24--%>
  <%--</span>--%>

    <div class="chat-source-detail-btn" data-toggle="dropdown"></div>
    <div class="chat-source-detail dropdown-menu">
        <dl class="type" id="visitorProperties">
        </dl>
    </div>
</div>
