package com.atroot.crowd.mvc.controller;

import com.atroot.crowd.constant.CrowdConstant;
import com.atroot.crowd.entity.Admin;
import com.atroot.crowd.service.api.AdminService;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpSession;

/**
 * Description:
 *
 * @author atroot@126.com  @ZYD
 * @create 2021.5.28 18:00
 */
@Controller
public class AdminController {
    @Autowired
    AdminService adminService;


    /**
     * 处理登录请求
     *
     * @param loginAcct 传入用户名
     * @param userPswd  传入密码
     * @param session   获取session对象
     * @return 重定向到主页面 //防止刷新重复提交表单
     */
    @RequestMapping("/admin/do/login.html")
    public String doLogin(@RequestParam("loginAcct") String loginAcct,
                          @RequestParam("userPswd") String userPswd,
                          HttpSession session) {
        // 调用Service方法进行账户登录检查
        // 若查询成功则返回一个admin对象，否则为空
        Admin admin = adminService.getAdminByLoginAcct(loginAcct, userPswd);

        // 如果登陆成功,那么将返回的admin对象存入Session域
        session.setAttribute(CrowdConstant.ATTR_NAME_LOGIN_ADMIN, admin);
        return "redirect:/admin/to/main/page.html";
    }

    /**
     * 处理登出请求
     *
     * @param session 拿到session对象
     * @return 重定向到登录页
     */
    @RequestMapping("/admin/do/logout.html")
    public String doLogout(HttpSession session) {
        // 使session强制失效
        session.invalidate();
        return "redirect:/admin/to/login/page.html";
    }

    /**
     * 分页功能实现
     *
     * @return 视图admin-page
     */
    @RequestMapping("/admin/get/page.html")
    public String getAdminPage(@RequestParam(value = "keyword", defaultValue = "") String keyword,
                               @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
                               @RequestParam(value = "pageSize", defaultValue = "5") Integer pageSize,
                               ModelMap modelMap) {
        PageInfo<Admin> pageInfo = adminService.getPageInfo(keyword, pageNum, pageSize);
        modelMap.addAttribute(CrowdConstant.ATTR_NAME_PAGE_INFO, pageInfo);
        return "admin-page";
    }

    @RequestMapping("/admin/remove/{adminId}/{pageNum}/{keyword}.html")
    public String removeAdmin(@PathVariable("adminId") Integer id,
                              @PathVariable() Integer pageNum,
                              @PathVariable() String keyword) {
        adminService.remove(id);
        // 直接转发到admin-page会导致没有分页数据显示 所以我们应该通过请求
        // 直接转发到请求get/page 会导致刷新的时候不断的发送删除请求
        // 这里使用重定向到上边的请求并带上路径参数
        return "redirect:/admin/get/page.html?pageNum=" + pageNum + "&keyword=" + keyword;
    }

    @RequestMapping("/admin/save.html")
    public String save(Admin admin) {
        // 执行保存操作
        adminService.saveAdmin(admin);

        return "redirect:/admin/get/page.html?pageNum=" + Integer.MAX_VALUE;
    }

    @RequestMapping("/admin/to/edit/page.html")
    public String toEditPage(@RequestParam("adminId") Integer id,
                             ModelMap modelMap,
                             HttpSession session) {
        Admin admin = adminService.getAdminById(id);
        modelMap.addAttribute("admin", admin);
//        session.setAttribute("adminLoginAcct",admin.getLoginAcct());
//        session.setAttribute("adminUserName",admin.getUserName());
//        session.setAttribute("adminLoginEmail",admin.getEmail());
        return "admin-edit";
    }

    @RequestMapping("/admin/update/page.html")
    public String update(Admin admin,
                         @RequestParam("pageNum") Integer pageNum,
                         @RequestParam("keyword") String keyword) {
        adminService.updateAdmin(admin);
        System.out.println("==========================>" + pageNum + "===>" + keyword);
        return "redirect:/admin/get/page.html?pageNum=" + pageNum + "&keyword=" + keyword;
    }
}