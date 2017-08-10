/**
 *  发起客服对话事件
 * @param username  当前登陆用户名
 * @param to   等待接入用户id
 */
function startCustomerTalk(username, to) {
    if (!username || !to) {
        return false;
    }
    var url = 'http://kfnew.17doubao.com/kf/api/customerLogin?username=' + username + '&to=' + to;
    $.ajax({
        url: url,
        type: 'POST',
        success: function (data) {
            if (data.code == 0 && !data.success && data.msg == '客服未登录') {
                url = 'http://kfnew.17doubao.com/kf/api/customer_chat?platform=uec&username=' + username + '&to' + to;
                window.open(url, "_blank");
            } else if (data.success) {
                alert('已接入用户，请到客服后台系统服务用户');
            }
            console.log(data);
        }
    });
}