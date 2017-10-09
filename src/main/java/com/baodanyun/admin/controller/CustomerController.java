package com.baodanyun.admin.controller;

import com.baodanyun.admin.dto.CustomerSearchDto;
import com.baodanyun.admin.service.CustomerService;
import com.baodanyun.websocket.bean.Response;
import com.baodanyun.websocket.controller.BaseController;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import java.io.InputStream;
import java.util.concurrent.Semaphore;

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
    public Response searchCustomer(CustomerSearchDto searchDto) {

        return null;
    }

    /**
     * 根据批次查询上传成功的记录
     *
     * @param serialNo
     * @return
     */
    @RequestMapping(value = "/getSuccessCustomer")
    public Response getSuccessCustomer(String serialNo) {

        return null;
    }

    /**
     * 根据批次查询上传失败的记录
     *
     * @param serialNo
     * @return
     */
    @RequestMapping(value = "/getFailCustomer")
    public Response getFailCustomer(String serialNo) {

        return null;
    }
}
