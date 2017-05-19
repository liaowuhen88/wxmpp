/**
 * nav control
 * 导航控制
 */
$(function() {
  (function() {
    var $nav = $('#nav');
    var $content = $('#content');

    $nav.on('click', 'li.bar', function() {
      var index = $(this).index();
      $(this).addClass('active').siblings().removeClass('active');
      $content.find('li.page').eq(index).addClass('active').siblings().removeClass('active');
      $("body").scrollTop(0);
    });
  })();
});
