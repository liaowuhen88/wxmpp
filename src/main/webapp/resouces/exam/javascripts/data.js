$(function() {
  'use strict';
  /*常规定义*/
  var $overview = $('#overview');
  var $abnormal = $('#abnormal');
  var $suggest = $('#suggest');
  var $overall = $('#overall');
  var $menuDepartment = $('#menuDepartment');
  var $menuProject = $('#menuProject');
  /*数据获取*/
  $.ajax({
    type: 'GET',
    url: './data/missq.json',
    data: 'json',
    timeout: 3000,
    success: function(jsondata, status, xhr) {
      if (jsondata.success) {
        // 数据获取成功
        // 得到获取的数据
        var data = jsondata.data;
        // 定义异常项
        var abnormalNumber = 0;
        var abnormalNum = 0;
        // 页面拼接 异常指标 报告详情
        var abnormalHtml = '';
        var overallHtml = '';
        var menuDepHtml = '';
        var menuProHtml = '';
        // 体检概览脏器归属
        var belong = '';
        // 临时变量 表格 科室 部门
        var tableHtml = '';
        var dHtml = '';
        var pHtml = '';
        var tableHtmlAB = '';
        var dHtmlAB = '';
        var pHtmlAB = '';
        var abNumber = 0;
        /**
         * 体检概览
         * 异常指标
         * 医生建议
         * 报告详情
         */
        // 健康状况
        switch (data.healthLevel) {
          case -1:
            $overview.find('.state').text('亚健康').addClass('normal');
            $abnormal.find('p.remind-info strong.state').text('亚健康');
            break;
          case 0:
            $overview.find('.state').text('不健康').addClass('bad');
            $abnormal.find('p.remind-info strong.state').text('不健康');
            break;
          case 1:
            $overview.find('.state').text('很健康').addClass('good');
            $abnormal.find('p.remind-info strong.state').text('很健康');
            break;
        }
        // 性别判断
        if (data.sex === 2) {
          $overview.find('.person').addClass('women');
          $overview.find('.organ li.ovary').show();
        } else {
          $overview.find('.person').addClass('men');
          $overview.find('.organ li.prostate').show();
        }
        // 医生建议
        $suggest.find('.content').append(data.suggest);
        // 报告详情 包括体检概览喝异常指标

        $.each(data.department, function(dIndex, dItem) {
          /*部门*/
          $.each(dItem.project, function(pIndex, pItem) {
            /*项目*/
            $.each(pItem.subdivide, function(sIndex, sItem) {
              if (sItem.status !== 0) {
                // 异常项
                abnormalNumber++;
                abNumber++;
                abnormalNum++;
                // 体检概览器官展示
                belong = sItem.belong;
                switch (belong) {
                  case '心脏':
                    $overview.find('ul.organ li.heart').addClass('active');
                    break;
                  case '肝胆胰脾':
                    $overview.find('ul.organ li.liver').addClass('active');
                    break;
                  case '甲状腺':
                    $overview.find('ul.organ li.thyroid').addClass('active');
                    break;
                  case '口腔':
                    $overview.find('ul.organ li.mouth').addClass('active');
                    break;
                  case '眼':
                    $overview.find('ul.organ li.eye').addClass('active');
                    break;
                  case '肾':
                    $overview.find('ul.organ li.kidney').addClass('active');
                    break;
                  case '脑血管':
                    $overview.find('ul.organ li.brain').addClass('active');
                    break;
                  case '耳鼻喉':
                    $overview.find('ul.organ li.ear').addClass('active');
                    break;
                  case '颈动脉':
                    $overview.find('ul.organ li.artery').addClass('active');
                    break;
                  case '脊柱':
                    $overview.find('ul.organ li.vertebral').addClass('active');
                    break;
                  case '肺':
                    $overview.find('ul.organ li.lung').addClass('active');
                    break;
                  case '肠':
                    $overview.find('ul.organ li.intestinal').addClass('active');
                    break;
                  case '骨骼':
                    $overview.find('ul.organ li.bone').addClass('active');
                    break;
                  case '胃':
                    $overview.find('ul.organ li.stomach').addClass('active');
                    break;
                }

                if (data.sex === "女") {
                  switch (belong) {
                    case '生殖器官':
                      $overview.find('ul.organ li.ovary').addClass('active');
                      break;
                  }
                } else {
                  switch (belong) {
                    case '生殖器官':
                      $overview.find('ul.organ li.prostate').addClass('active');
                      break;
                  }
                }

                // 报告详情页面拼接
                switch (sItem.status) {
                  case 1:
                    tableHtml += '<tr><th data-belong="' + sItem.belong + '"><i class="icon caution"></i>' + sItem.name + '</th><td><i class="icon high"></i>' + sItem.result + '<span class="unit">' + unitConversion(sItem.unit) + '</span><p class="remark">' + sItem.remark + '</p></td></tr>';
                    tableHtmlAB += '<tr><th data-belong="' + sItem.belong + '"><i class="icon caution"></i>' + sItem.name + '</th><td><i class="icon high"></i>' + sItem.result + '<span class="unit">' + unitConversion(sItem.unit) + '</span><p class="remark">' + sItem.remark + '</p></td></tr>';
                    break;
                  case -1:
                    tableHtml += '<tr><th data-belong="' + sItem.belong + '" data-belong="' + sItem.belong + '"><i class="icon caution"></i>' + sItem.name + '</th><td><i class="icon low"></i>' + sItem.result + '<span class="unit">' + unitConversion(sItem.unit) + '</span><p class="remark">' + sItem.remark + '</p></td></tr>';
                    tableHtmlAB += '<tr><th data-belong="' + sItem.belong + '" data-belong="' + sItem.belong + '"><i class="icon caution"></i>' + sItem.name + '</th><td><i class="icon low"></i>' + sItem.result + '<span class="unit">' + unitConversion(sItem.unit) + '</span><p class="remark">' + sItem.remark + '</p></td></tr>';
                    break;
                  case 2:
                    tableHtml += '<tr><th data-belong="' + sItem.belong + '"><i class="icon caution"></i>' + sItem.name + '</th><td>' + sItem.result + '<span class="unit">' + unitConversion(sItem.unit) + '</span><p class="remark">' + sItem.remark + '</p></td></tr>';
                    tableHtmlAB += '<tr><th data-belong="' + sItem.belong + '"><i class="icon caution"></i>' + sItem.name + '</th><td>' + sItem.result + '<span class="unit">' + unitConversion(sItem.unit) + '</span><p class="remark">' + sItem.remark + '</p></td></tr>';
                    break;
                }
              } else {
                // 非异常项
                if (sItem.result === '') {
                  tableHtml += '';
                } else {
                  tableHtml += '<tr><th>' + sItem.name + '</th><td>' + sItem.result + '<span class="unit">' + unitConversion(sItem.unit) + '</span><p class="remark">' + sItem.remark + '</p></td></tr>';
                }
              }
            });
            // table循环结束
            /*项目拼接*/
            if (abNumber === 0) {
              // 没有异常
              pHtml += '<h4 class="tag" id="pro' + dIndex + pIndex + '">' + pItem.name + '</h4><table class="over-tabel">' + tableHtml + '</table>';
              menuProHtml += '<li class="item" data-id="#pro' + dIndex + pIndex + '">' + pItem.name + '</li>';
            } else {
              // 出现异常
              pHtml += '<h4 class="tag" id="pro' + dIndex + pIndex + '">' + pItem.name + '<span class="note"><em>' + abNumber + '</em>项异常!</span></h4><table class="over-tabel">' + tableHtml + '</table>';
              menuProHtml += '<li class="item waring" data-id="#pro' + dIndex + pIndex + '">' + pItem.name + '</li>';
              pHtmlAB += '<h4 class="tag" id="pro' + dIndex + pIndex + '">' + pItem.name + '<span class="note"><em>' + abNumber + '</em>项异常!</span></h4><table class="over-tabel">' + tableHtmlAB + '</table>';
            }
            abNumber = 0;
            tableHtml = '';
            tableHtmlAB = '';
          });
          /*项目结束*/
          if (abnormalNum === 0) {
            menuDepHtml += '<li class="item" data-id="#dep' + dIndex + '">' + dItem.name + '</li>';
          } else {
            menuDepHtml += '<li class="item waring" data-id="#dep' + dIndex + '">' + dItem.name + '</li>';
          }
          /*科室小结*/
          if (dItem.opinion !== '' && dItem.opinion !== null) {
            // 有科室小结
            pHtml += '<table class="over-tabel over-conclusion"><tr><th><i class="icon conclusion"></i>科室小结</th><td>' + dItem.opinion + '</td></tr></table>';
          } else {
            // 无科室小结
            pHtml += '';
          }
          /*科室菜单*/
          dHtml += '<div class="over-items"><h3 class="title" id="dep' + dIndex + '">' + dItem.name + '</h3>' + pHtml + '</div>';
          if (pHtmlAB === '') {
            dHtmlAB += '';
          } else {
            dHtmlAB += '<div class="over-items over-abnormal">' + pHtmlAB + '</div>';
          }
          pHtml = ''; //科室循环结束清空赋值
          pHtmlAB = '';
          abnormalNum = 0;
        });
        /*内容填充*/
        // 异常项填充
        abnormalHtml = dHtmlAB;
        $abnormal.find('p.remind-info strong.abnormalNumber').text(abnormalNumber);
        $abnormal.find('.over').html(abnormalHtml);
        // 科室，项目填充
        $menuDepartment.html(menuDepHtml);
        $menuProject.html(menuProHtml);
        // 报告详情填充
        overallHtml = dHtml;
        $overall.find('.over').html(overallHtml);
        // 隔行换色
        interlacedChange();
        Menu($('#menu'));
        BorderClear($menuDepartment);
        BorderClear($menuProject);
      } else {
        // 请求成功，数据获取失败
        //$('body').html('没有找到数据，请刷新页面再试。');
        $abnormal.find('.over').empty().append("<div class='no-report'><img src='images/health/report_head.png' alt='' class='report-img'><p class='conts'>暂时没有数据</p> </div>");
        $abnormal.find('.over').css("margin", "0");
        $overall.find(".over").empty().append("<div class='no-report'><img src='images/health/report_head.png' alt='' class='report-img'><p class='conts'>暂时没有数据</p> </div>");
        $overall.find(".over").css("margin", "0");
      }
    },
    error: function(xhr, type) {
      // 请求失败
      //$('body').html(xhr.status + '请刷新页面再试。');
      if (console) {
        console.log(json.msg);
      }

      $abnormal.find('.over').empty().append("<div class='no-report'><img src='images/health/report_head.png' alt='' class='report-img'><p class='conts'>获取数据失败</p> </div>");
      $abnormal.find('.over').css("margin", "0");
      $overall.find(".over").empty().append("<div class='no-report'><img src='images/health/report_head.png' alt='' class='report-img'><p class='conts'>获取数据失败</p> </div>");
      $overall.find(".over").css("margin", "0");
    }
  });
});
//清除边框
function BorderClear(obj) {
  for (var i = 0; i < obj.length; i++) {
    var $item = obj.eq(i).find('.item');
    for (var j = 1; j <= $item.length; j++) {
      if (j % 3 === 2) {
        $item.eq(j).css('border-right', 'none');
      }
    }
  }
}
// 单位处理
function unitConversion(str) {
  if (str === 'null' || str === null) {
    return '';
  } else {
    return str;
  }
}
// 隔行换色
function interlacedChange() {
  var $over = $('.over').find('.tag');
  var overLength = $over.length;
  for (var i = 0; i <= overLength; i++) {
    if (i % 4 === 0) {
      $over.eq(i).css({
        'background-color': '#d2eff7'
      });
    } else if (i % 4 === 1) {
      $over.eq(i).css({
        'background-color': '#faedbe'
      });
    } else if (i % 4 === 2) {
      $over.eq(i).css({
        'background-color': '#deebbc'
      });
    } else if (i % 4 === 3) {
      $over.eq(i).css({
        'background-color': '#fcdcde'
      });
    }
  }
}
