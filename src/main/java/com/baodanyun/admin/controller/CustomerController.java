package com.baodanyun.admin.controller;

import com.baodanyun.admin.dto.CustomerSearchDto;
import com.baodanyun.admin.dto.PageDto;
import com.baodanyun.admin.service.CustomerService;
import com.baodanyun.websocket.bean.Response;
import com.baodanyun.websocket.controller.BaseController;
import com.baodanyun.websocket.model.AppCustomerFail;
import com.baodanyun.websocket.model.AppCustomerSerial;
import com.baodanyun.websocket.model.AppCustomerSuccess;
import com.github.pagehelper.PageInfo;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import java.io.InputStream;

/**
 * 客户配置
 */
@RestController
public class CustomerController extends BaseController {

    @Autowired
    private CustomerService customerService;

    /**
     * 客户配置首页
     *
     * @return
     */
    @RequestMapping("customerConfig")
    public ModelAndView customerConfig() {
        ModelAndView view = new ModelAndView();
        view.setViewName("/customerConfig");

        return view;
    }

    /**
     * 上传客服excel
     *
     * @param file 文件
     * @return
     */
    @RequestMapping(value = "/uploadExcel", method = RequestMethod.POST)
    public boolean upload(@RequestParam(value = "file") MultipartFile file) {
        try {
            InputStream inputStream = file.getInputStream();
            customerService.uploadExcel(inputStream);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    /**
     * 搜索上传状态记录
     *
     * @param searchDto
     * @return
     */
    @RequestMapping(value = "/searchCustomer")
    public Response searchCustomer(PageDto pageDto, CustomerSearchDto searchDto) {
        PageInfo<AppCustomerSerial> pageInfo = customerService.searchCustomer(pageDto, searchDto);
        return null;
    }

    /**
     * 根据批次查询上传成功的记录
     *
     * @param serialNo 批次
     * @return
     */
    @RequestMapping(value = "/getSuccessCustomer")
    public Response findSuccessCustomerPage(PageDto pageDto, String serialNo) {
        if (StringUtils.isBlank(serialNo)) {
            return null;
        }
        PageInfo<AppCustomerSuccess> pageInfo = customerService.findSuccessCustomerPage(pageDto, serialNo);
        return null;
    }

    /**
     * 根据批次查询上传失败的记录
     *
     * @param serialNo
     * @return
     */
    @RequestMapping(value = "/getFailCustomer")
    public Response finalFailCustomerPage(PageDto pageDto, String serialNo) {
        PageInfo<AppCustomerFail> pageInfo = customerService.finalFailCustomerPage(pageDto, serialNo);
        return null;
    }
}
