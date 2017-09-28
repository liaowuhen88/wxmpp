package com.baodanyun.admin.controller;

import com.baodanyun.admin.service.CustomerService;
import com.baodanyun.websocket.controller.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import java.io.InputStream;
import java.util.concurrent.Semaphore;

@RestController
public class CustomerController extends BaseController {

    @Autowired
    private CustomerService customerService;

    /**
     * 信号量
     */
    private Semaphore semaphore = new Semaphore(3);

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
            //semaphore.acquire();
            InputStream inputStream = file.getInputStream();
            customerService.uploadExcel(inputStream);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            //semaphore.release();
        }

        return true;
    }
}
