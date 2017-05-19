'use strict';
(function($) {
    $.fn.extend({
        modal: function(options) {
            var selector = this.selector;
            var $target = $('[data-target=' + selector.replace('#', '') + ']');
            var _this = this;
            options = options || {};
            var fn = {
                init: function() {
                    this.showBind();
                    this.dismissBind();
                    this.resize();
                },
                show: function() {
                    $target.addClass('fade').show();
                    if (options.ajax) {
                        options.ajax.call(_this, $target);
                    }
                },
                dismiss: function() {
                    $target.removeClass('fade').hide();
                    if (options.missed) {
                        options.missed.call(_this);
                    }
                },
                showBind: function() {
                    $(selector).on('click', function(e) {
                        fn.show();
                        fn.position();
                    });
                },
                dismissBind: function() {
                    $target.on('click', '.modal_close', function(e) {
                        fn.dismiss();
                        e.stopPropagation();
                    });
                    $('[data-close="modal"]').on('click', function(e) {
                        fn.dismiss();
                        e.stopPropagation();
                    });
                    $target.on('click','.modal_backdrop,.modal_dialog', function(e) {
                        fn.dismiss();
                        e.stopPropagation();
                    });
                    $target.on('click','.modal_content', function(e) {
                        e.stopPropagation();
                    });
                },
                position: function() {
                    var wWidth = $target.width();
                    var wHeight = $target.height();
                    var $content = $target.find('.modal_content');
                    /*$content.css({
                        //(wHeight - $content.height()) / 2 + 'px'
                        top: '200px',
                        left: (wWidth - $content.width()) / 2 + 'px'
                    })*/
                },
                resize: function() {
                    $(window).on('resize', function() {
                        fn.position();
                    });
                }
            };
            fn.init();
        }
    });
})(jQuery);
