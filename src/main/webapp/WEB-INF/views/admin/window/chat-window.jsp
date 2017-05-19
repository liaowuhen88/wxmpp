<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%--模态框--%>
<div class="modal" data-target="turnBtn">
    <div class="modal_backdrop"></div>
    <div class="modal_dialog">
        <div class="modal_content">
            <div class="modal_header">
                <span class="modal_close">X</span>客服列表
            </div>
            <div class="modal_body">
                <div class="turn-list">
                    <ul id="turn-list">
                    </ul>
                </div>
            </div>
        </div>
    </div>
</div>
<!-- 无会话 -->
<div class="chat-window" id="noChat">
    <div class="nothing">
        <img src="<%=request.getContextPath()%>/resouces/images/tabpage.png" alt="">
        <p>从左侧列表打开对话</p>
    </div>
</div>
<!-- 有会话 -->
<div class="chat-window" style="display: none" id="hasChat">
    <div class="chat-screen">
        <div class="chat-title"><span class="close" id="offSession">结束对话</span><span class="turn" id="turnBtn">转接</span>
            <div id="currentChatId" data-id=""></div>
        </div>
        <!-- chat-source -->
        <%--<jsp:include page="chat-source.jsp"/>--%>
        <!-- chat-show -->
        <jsp:include page="chat-show.jsp"/>
        <!-- chat-input -->
        <jsp:include page="chat-input.jsp"/>
    </div>
</div>
