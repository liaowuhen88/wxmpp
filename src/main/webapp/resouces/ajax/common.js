
function ajaxSubmitCommon(formId, buttonId, url, data, successCallback, errorCallback, timeOut) {
    var ajaxOptions = {
        url: url,
        type: 'post',
        data: data,
        dataType: 'json',
        timeout: 100000,
        cache: false,
        success: function (responseJson) {
            $("#" + buttonId).removeAttr("disabled").removeAttr("style");
            if (responseJson.success) {
                successCallback(responseJson);
            } else {
                errorCallback(responseJson);
            }
        },
        error: function () {
            $("#" + buttonId).removeAttr("disabled").removeAttr("style");
            timeOut();
        }
    };

    $("#" + formId).ajaxSubmit(ajaxOptions);
    $("#" + buttonId).attr("disabled", "disabled").attr("style", "background: #B0B5B9;");

}

function confirmIt(title, content, url, data, table,success,error) {
    $.messager.confirm(title, content, function () {
        $.ajax({
            type: 'post',
            url: url,
            data: JSON.stringify(data),
            contentType: "application/json",
            dataType: 'json',

            success: function (responseJson) {
                if (responseJson.success) {
                    if(null != table){
                        table.fnDraw();
                    }
                    success(responseJson);
                } else {
                    //$.messager.popup(responseJson.msg);
                    error(responseJson);
                    $.messager.popup(responseJson.msg);
                }
            },
            error: function () {
                $.messager.popup("error");
            }
        });
    });
}

function ajaxCommon(url, data, success, error) {
    $.ajax({
        url: url,
        type: 'post',
        data: JSON.stringify(data),
        contentType: "application/json",
        dataType: 'json',
        success: function (responseJson) {
            if (responseJson.success) {
                success(responseJson)
            } else {
                //$.messager.popup(responseJson.msg);
                error(responseJson);
            }
        },
        error: function () {
            //$.messager.popup("error");
            $.messager.popup("发生错误");
        }
    });
}

/**
 * 因事件冲突请迁移到自用JS页面中
 */
//$(document).keydown(function (e) {
//    var e = e || event,
//        keycode = e.which || e.keyCode;
//    if (keycode == 13) {
//        $("#searchButton").trigger("click");
//        return false;
//    }
//});

$(document).keyup(function(e){
    var key = e.which;
    if(key == 27){
        $(".close").trigger("click");
    }
});