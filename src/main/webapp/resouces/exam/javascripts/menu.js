	'use strict';

	function Menu(obj) {
	  var $bar = obj.find('.menu-bar .item');
	  var $content = obj.find('.menu-content');
	  var state = true;
	  var $overall = $('#overall');

	  //终端判断
	  var u = navigator.userAgent;
	  var isAndroid = u.indexOf('Android') > -1 || u.indexOf('Adr') > -1; //android终端
	  var isiOS = !!u.match(/\(i[^;]+;( U;)? CPU.+Mac OS X/); //ios终端

	  //选用事件
	  if (isAndroid) {
	    obj.find('.menu-bar .item').on('click', function() {
	      if ($(this).attr('class') !== 'item active') {
	        $(this).addClass('active').siblings().removeClass('active');
	        var index = $(this).index();
	        $content.css('background', 'rgba(0,0,0,0.7)').show().find('.items').eq(index).addClass('active').siblings().removeClass('active');
	        $('html,body').css({
	          'width': '100%',
	          'height': '100%',
	          'overflow': 'hidden'
	        });
	      } else {
	        $(this).removeClass('active');
	        $content.css('background', 'white').hide();
	        $('html,body').css('overflow', 'visible');
	      }
	    });
	    obj.find('.menu-content .item').on('click', function() {
	      var id = $(this).attr('data-id');
	      var overItemsTop = $overall.find('.over-items').offset().top;
	      $content.css('background', 'white').hide();
	      $('html,body').css('overflow', 'visible');

	      var targetTop = $overall.find(id).offset().top - overItemsTop;
	      $('body').scrollTop(targetTop);
	      $bar.removeClass('active');
	      return false;
	    });
	    obj.find('.menu-content').click(function() {
	      obj.find('.menu-bar .item').removeClass('active');
	      $content.css('background', 'rgba(255,255,255,0)').hide();
	      $('html,body').css('overflow', 'visible');
	    });
	  } else {
	    obj.find('.menu-bar .item').click(function() {
	      if ($(this).attr('class') !== 'item active') {
	        $(this).addClass('active').siblings().removeClass('active');
	        var index = $(this).index();
	        $content.css('background', 'rgba(0,0,0,0.7)').show().find('.items').eq(index).addClass('active').siblings().removeClass('active');
	        $('html,body').css({
	          'width': '100%',
	          'height': '100%',
	          'overflow': 'hidden'
	        });
	      } else {
	        $(this).removeClass('active');
	        $content.css('background', 'white').hide();
	        $('html,body').css('overflow', 'visible');
	      }
	    });
	    obj.find('.menu-content .item').click(function() {
	      var id = $(this).attr('data-id');
	      var overItemsTop = $overall.find('.over-items').offset().top;
	      $content.css('background', 'white').hide();
	      $('html,body').css('overflow', 'visible');

	      var targetTop = $overall.find(id).offset().top - overItemsTop;
	      $('body').scrollTop(targetTop);
	      $bar.removeClass('active');
	      return false;
	    });
	    obj.find('.menu-content').click(function() {
	      obj.find('.menu-bar .item').removeClass('active');
	      $content.css('background', 'rgba(255,255,255,0)').hide();
	      $('html,body').css('overflow', 'visible');
	    });
	  }
	}
