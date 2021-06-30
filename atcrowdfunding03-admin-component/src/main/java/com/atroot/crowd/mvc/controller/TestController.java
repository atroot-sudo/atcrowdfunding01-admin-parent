package com.atroot.crowd.mvc.controller;

import com.atroot.crowd.entity.Admin;
import com.atroot.crowd.service.api.AdminService;
import com.atroot.crowd.util.CrowdUtil;
import org.slf4j.ILoggerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * Description:
 *
 * @author atroot@126.com  @ZYD
 * @create 2021.5.24 10:34
 */
@Controller
public class TestController {
    @Autowired
    AdminService adminService;

    Logger log = LoggerFactory.getLogger(this.getClass());

//    @ResponseBody
    @RequestMapping("/test/hello.html")
    public String testMVC(ModelMap map, HttpServletRequest request) {
        boolean b = CrowdUtil.judgeRequestType(request);
        log.info("此请求应该为：" + b);

        List<Admin> adminList = adminService.getAll();
        map.addAttribute("adminList", adminList);
        System.out.println(10/0);
        return "target";
    }

    @RequestMapping("/test/ajax.html")
    public String testAjax(@RequestBody Integer [] list,HttpServletRequest request){
        boolean b = CrowdUtil.judgeRequestType(request);
        log.info("此请求应该为：" + b);
        for (Integer list1 : list) {
            System.out.println(list1);
        }
        return "target";
    }
}
