package com.baodanyun.admin.controller;

import com.baodanyun.admin.dto.CustomerDto;
import com.baodanyun.admin.extend.ExcelCallbackFunction;
import com.baodanyun.admin.service.CustomerService;
import com.wzg.xls.tools.exception.ExcelErrorLogBean;
import com.wzg.xls.tools.tools.ExcelAndCsvUtils;
import com.wzg.xls.tools.tools.ExcelFileHelper;
import com.wzg.xls.tools.tools.ICallbackFunction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@RestController
@RequestMapping("/api/customer")
public class CustomerController {

    @Autowired
    private CustomerService customerService;

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
}
