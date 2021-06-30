package com.atroot.crowd.mvc.controller;

import com.atroot.crowd.entity.Auth;
import com.atroot.crowd.entity.Role;
import com.atroot.crowd.service.api.AdminService;
import com.atroot.crowd.service.api.AuthService;
import com.atroot.crowd.service.api.RoleService;
import com.atroot.crowd.util.ResultEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.Map;

/**
 * Description:
 *
 * @author atroot@126.com  @ZYD
 * @create 2021.6.19 16:42
 */
@Controller
public class AssignHandler {
    @Autowired
    RoleService roleService;

    @Autowired
    AdminService adminService;

    @Autowired
    AuthService authService;

    @RequestMapping("/assign/to/assign/role/page.html")
    public String toAssignRolePage(@RequestParam("adminId") Integer adminId,
                                   ModelMap modelMap) {
        // 查询已经分配角色
        List<Role> assignRoleList = roleService.getAssignRole(adminId);

        // 查询未分配角色
        List<Role> unAssignRoleList = roleService.getUnAssignRoleList(adminId);

        // 存入模型
        modelMap.addAttribute("assignRoleList", assignRoleList);
        modelMap.addAttribute("unAssignRoleList", unAssignRoleList);
        return "assign-role";
    }

    @RequestMapping("/assign/do/role/assign.html")
    public String saveAdminRoleRelationship(@RequestParam("adminId") Integer adminId,
                                            @RequestParam("pageNum") Integer pageNum,
                                            @RequestParam("keyword") String keyword,
                                            // 我们允许用户在页面上取消原有admin的所有role，所以我们允许roleIdList为空
                                            @RequestParam(value = "roleIdList", required = false) List<Integer> roleIdList) {
        adminService.saveAdminRoleRelationship(adminId, roleIdList);
        return "redirect:/admin/get/page.html?pageNum=" + pageNum + "&keyword=" + keyword;
    }

    @ResponseBody
    @RequestMapping("/assign/get/all/auth.json")
    public ResultEntity<List<Auth>>  getAllAuth(){
        List<Auth> authList = authService.getAll();
        return ResultEntity.successWithData(authList);
    }

    @ResponseBody
    @RequestMapping("/assign/get/assigned/auth/id/by/role/id.json")
    public ResultEntity<List<Integer>> getAssignedAuthIdByRoleId(@RequestParam("roleId") Integer roleId){
        List<Integer> authIdList = authService.getAssignedAuthByRoleId(roleId);
        for (Integer integer : authIdList) {
            System.out.println("测试： " + integer);
        }
        return ResultEntity.successWithData(authIdList);
    }

    @ResponseBody
    @RequestMapping("/assign/do/role/assign/auth.json")
    public ResultEntity<String> saveRoleAuthRelationship(@RequestBody Map<String,List<Integer>> map){
        authService.saveRelationship(map);
        return ResultEntity.successWithoutData();
    }

}
