/**
 * 基于jquery的图片上传控件
 */
!function ($) {

    "use strict";
    //定义上传事件
    var upImgEvent = {
        fileQueued: 'fileQueued',//文件加载的时候触发
        //statusChange: 'statuschange',
        uploadSuccess: 'uploadSuccess',//文件上传成功触发默认路径应该是固定的
        uploadError: 'uploadError',//文件上传报错触发
        error: 'error',//文件上传验证报错的时候触发,比如大小,上传重复图片，
        uploadFinished: 'uploadFinished',
        startUpload: 'startUpload'
    };

    //定义内部使用函数
    var _util = {
        _isSupportImage: function () {
            var data = new Image();
            var support = true;
            data.onload = data.onError = function () {
                if (this.width != 1 || this.height != 1) {
                    support = false;
                }
            };
            data.src = "data:image/gif;base64,R0lGODlhAQABAIAAAAAAAP///ywAAAAAAQABAAACAUwAOw==";
            return support;
        }(),
        _renderHtml: function (file) {
            return $('<div class="file_img" id="' + file.id + '">' +
            '<div class="delete"><a href="javascript:deleteImg(\'' + file.id + '\',1)">删除</a></div>' +
            '<span style="vertical-align: middle; display: inline-block; height: 100%;"></span>' +
            '<img id="img' + file.id + '"  title="' + file.name + '" src="' + window.base + '/resources/images/loading_.gif" /> ' +
            '<div id="wart" style="position:absolute;z-index:99;left:5px;top:10px;"><span style="color: blue">图片正在上传.请等待...</span><div>' +
            '</div>')
        },
        _showError: function (code) {
            //$('body').hideLoading();
            $("img").next().remove();
            var text = null;
            switch (code) {
                case 'exceed_size':
                    text = '文件太大了..请重新上传';
                    break;
                case 'interrupt':
                    text = '上传暂停..';
                    break;
                default:
                    text = '上传失败，请重试...';
                    break;
            }
            alert(text);
        }
    };

    var Img = function (element, options) {

        var imgRadio = {
            //图片属性
            _ratio: function () {
                if (window.devicePixelRatio) {
                    return window.devicePixelRatio;
                } else {
                    return 1;
                }
            },  //像素比例
            thumbWidth: 140 * this._ratio,
            thumbHeight: 140 * this._ratio
        };
        //构造默认options
        var defaultOption = {
            packId: "#" + options.packId,
            swf: window.base + '/resources/js/webuploader/Uploader.swf',//ie6,7,8 需要flash支持，默认优先Html5
            server: options.uploadServer + options.uploadPath,//文件上传的服务器路径
            pick: {
                id: "#" + options.uploaderId,
                multiple: options.isMultiple//是否单选
            },
            duplicate: true,//可以重复
            sendAsBinary: true,//android4 有些机型必须开启 为了兼容 都启用这种模式
            auto: true,//开启选择图片自动上传
            fileVal: 'bin',//文件上传字段名称
            fileSingleSizeLimit: 10 * 1024 * 1024,  //10M 单个图片只能最大10M
            disableGlobalDnd: true,//不能拖拽
            accept: {
                //上传图片所支持的类型
                title: "上传图片",
                extensions: 'gif,jpg,jpeg,bmp,png',
                mimeTypes: 'image/*'
            },
            resize: false,//不压缩image, 默认如果是jpeg，文件上传前会压缩一把再上传！
            //压缩
            compress: {
                width: 1600,
                height: 1600,
                // 图片质量，只有type为`image/jpeg`的时候才有效。
                quality: 90,
                // 是否允许放大，如果想要生成小图的时候不失真，此选项应该设置为false.
                allowMagnify: false,
                // 是否允许裁剪。
                crop: false,
                // 是否保留头部meta信息。
                preserveHeaders: true,
                // 如果发现压缩后文件大小比原来还大，则使用原来图片
                // 此属性可能会影响图片自动纠正功能
                noCompressIfLarger: false,
                // 单位字节，如果图片大小小于此值，不会采用压缩。
                compressSize: 1024 * 1024 * 1
            },
            callback: undefined || options.callback,
            success: undefined || options.success,
            uploadFinished: undefined || options.uploadFinished,
            startUpload:undefined||options.startUpload
        };
        this.init(defaultOption, imgRadio);
    };
    Img.prototype.init = function (options, radio) {
        var uploader = WebUploader.create(options);
        //构造监听事件
        uploader.on(upImgEvent.fileQueued, function (file) {
            //$('body').showLoading();
            if (file.getStatus() === 'invalid') {
                _util._showError(file.statusText);
            }
            if (options.callback != undefined && options.callback instanceof Function) {
                this.makeThumb(file, function (error, src) {
                    if (error) {
                        console.log("can not preview :" + error);
                        return;
                    }
                    if (_util._isSupportImage) {
                        options.callback.apply(this, [file, src]);
                    }
                }, radio.thumbWidth, radio.thumbHeight);
            } else {
                var html = _util._renderHtml(file);
                //构造预览图
                this.makeThumb(file, function (error, src) {
                    var img = $("#img" + file.id);
                    if (error) {
                        console.log("can not preview :" + error);
                        return;
                    }
                    if (_util._isSupportImage) {
                        img.empty().attr('src', src);
                    }
                }, radio.thumbWidth, radio.thumbHeight);
                html.appendTo($(options.packId));
            }
        });
        uploader.on('uploadProgress', function (file, percentage) {

        });
        uploader.on(upImgEvent.uploadSuccess, function (file, response) {
            if (response.success) {
                var imgResponse = response.data;
                if (options.success != undefined && options.success instanceof Function) {
                    options.success.apply(this, [file, imgResponse]);
                }
            } else {
                alert("warning:" + response.msg);
            }
        });
        uploader.on(upImgEvent.uploadFinished, function () {
            if (options.uploadFinished != undefined && options.uploadFinished instanceof Function) {
                options.uploadFinished.apply();
            }
        });
        uploader.on(upImgEvent.uploadError, function (file, code) {
            //$('body').hideLoading();
            $("img").next().remove();
            alert("文件上传失败" + file.name + "code:" + code);
        });
        uploader.on(upImgEvent.startUpload, function (file, code) {
            options.startUpload();
        });

        uploader.on(upImgEvent.error, function (code) {
            //$('body').hideLoading();
            $("img").next().remove();
            if (code === 'F_DUPLICATE')
                alert("文件已经存在.请换个图片重新上传...");
            else if (code === 'F_EXCEED_SIZE')
                alert("文件不能超过10M,请重新上传");
            else if (code === 'Q_TYPE_DENIED')
                alert("文件类型只能是图片类型,请重新上传");
            else
                alert("error type :" + code);
        });
    };
    $.webUploader = function (options) {
        return new Img(this, options);
    };
    $.avatarUploader = function (options) {
        return new Img(this, options);
    };
    return window.Img = Img;
}(window.jQuery);
function deleteImg(id, type) {
    $("#" + id).remove();
    //如果是sybd则显示上传插件
    if (type == 1) {
        $("#sybd").parent().show();
    }

    //快速理赔
    if (type == 666) {
        $("#fastSybd").show();
    }
}
