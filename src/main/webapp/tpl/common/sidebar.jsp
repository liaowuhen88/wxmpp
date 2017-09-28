<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<header class="sidebar">
    <div class="sidebar-user">
        <div class="user-photo" id="customerInfo"><img src="<%=request.getContextPath()%>/resouces/images/89hi.jpg" alt=""></div>
        <div class="status status-icon icon-online" data-toggle="dropdown"></div>
        <ul class="status-list dropdown-menu">
            <li><i class="status-icon icon-online"></i>在线</li>
            <li><i class="status-icon icon-offline"></i>离线</li>
            <li><i class="status-icon icon-leave"></i>离开</li>
            <li><i class="status-icon icon-invisible"></i>隐身</li>
        </ul>
    </div>
    <nav class="navbar">
        <ul class="nav">
            <%--<li data-toggle="tooltip" data-placement="right" title="首页">
              <a href="/customer/home"><i class="nav-icon icon-home"></i></a>
            </li>--%>
            <li data-toggle="tooltip" data-placement="right" title="对话">
                <span class="msg" id="waitReplyPerson">0</span>
                <a href="javascript:void(0);"><i class="nav-icon icon-chat"></i></a>
            </li>
            <li data-toggle="tooltip" data-placement="right" title="历史记录" id="historyBtn">
                <a href="javascript:void(0);"><i class="nav-icon icon-history"></i></a>
            </li>
            <li data-toggle="tooltip" data-placement="right" title="用户留言" id="leaveMessageBtn">
                <a href="javascript:void(0);"><i class="nav-icon icon-visitor"></i></a>
            </li>
                <li data-toggle="tooltip" data-placement="right" title="质检" id="qualityCheck" style="display: none;">
                    <a href="<%=request.getContextPath()%>/api/qualityCheck" target="_blank"><i
                            class="nav-icon icon-report"></i></a>
                </li>
                <li data-toggle="tooltip" data-placement="right" title="客户配置" id="leaveMessageBtn">
                    <a href="<%=request.getContextPath()%>/api/customerConfig" target="_blank"><i
                            class="nav-icon icon-visitor"></i></a>
                </li>
        </ul>
    </nav>
    <ul class="tools">
        <li data-toggle="tooltip" data-placement="top" title="设置" id="set">
            <a class="tools-icon icon-set" href="javascript:void(0);">设置</a>
        </li>
        <li data-toggle="tooltip" data-placement="top" title="退出">
            <a class="tools-icon icon-signout signOut" href="javascript:void(0);">退出</a>
        </li>
    </ul>
</header>