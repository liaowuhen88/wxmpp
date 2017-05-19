<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%--模态框 profile--%>
<div class="modal" data-target="set">
    <div class="modal_backdrop"></div>
    <div class="modal_dialog">
        <div class="modal_content">
            <div class="modal_header">
                <span class="modal_close">X</span>账号信息
            </div>
            <div class="modal_body">
                <div class="profile">
                    <div class="content">
                        <div class="item avatar" id="avatar">
                            <img src="<%=request.getContextPath()%>/resouces/images/default-avatar.jpg" alt="" id="icon">

                            <span id="avatarUpload">换一个新头像</span>
                            <input type="file" name="" id="">

                            <div class="remark">你可以选择 jpg, gif, png 格式的图片，最大 5Mb</div>
                        </div>
                        <div class="item nickname">
                            <span class="tag">昵称</span>
                            <input type="text" name="nickname" id="nickName" placeholder="昵称">
                        </div>
                        <div class="item username">
                            <span class="tag">真实姓名</span>
                            <input type="text" name="username" id="userName" placeholder="真实姓名">
                        </div>
                        <div class="item password">
                            <span class="tag">密码</span>
                            <span class="text" id="changePWD">修改密码</span>
                        </div>
                        <div class="item loginUsername">
                            <span class="tag">登录名</span>
                            <span class="text" id="loginUsername"></span>
                        </div>
                        <div class="item workNumber">
                            <span class="tag">工号</span>
                            <input type="text" name="username" id="workNumber" placeholder="工号">
                        </div>
                        <div class="item desc">
                            <span class="tag">描述</span>
                            <textarea name="desc" id="desc" placeholder="请在此输入一端描述"></textarea>
                        </div>
                    </div>
                </div>
            </div>
            <div class="modal_footer">
                <button class="btn cancel-btn" data-close="modal">关闭</button>
                <button class="btn" id="changeProfileBtn">确认修改</button>
            </div>
        </div>
    </div>
</div>

<%--模态框 修改密码--%>
<div class="modal" data-target="changePWD">
    <div class="modal_backdrop"></div>
    <div class="modal_dialog">
        <div class="modal_content">
            <div class="modal_header">
                <span class="modal_close">X</span>修改密码
            </div>
            <div class="modal_body">
                <div class="changePWD-form">
                    <label for="oldPWD">
                        <input type="password" id="oldPWD" name="oldPWD" placeholder="旧密码">
                    </label>
                    <label for="newPWD">
                        <input type="password" id="newPWD" name="newPWD" placeholder="新密码">
                    </label>
                    <label for="confirmPWD">
                        <input type="password" id="confirmPWD" name="confirmPWD" placeholder="确认密码">
                    </label>
                </div>
            </div>
            <div class="modal_footer">
                <button class="btn cancel-btn" data-close="modal">关闭</button>
                <button class="btn" id="changePWDConfirm">确认修改</button>
            </div>
        </div>
    </div>
</div>