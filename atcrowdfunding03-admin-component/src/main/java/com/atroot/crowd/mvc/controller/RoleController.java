package com.atroot.crowd.mvc.controller;

import com.atroot.crowd.entity.Role;
import com.atroot.crowd.service.api.RoleService;
import com.atroot.crowd.util.ResultEntity;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * Description: RoleController
 *
 * @author atroot@126.com  @ZYD
 * @create 2021.6.8 20:32
 */
@Controller
public class RoleController {
    @Autowired
    RoleService roleService;


    @RequestMapping("/role/get/page/info.json")
    @ResponseBody
    public ResultEntity<PageInfo<Role>> getPageInfo(@RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
                                                    @RequestParam(value = "pageSize", defaultValue = "5") Integer pageSize,
                                                    @RequestParam(value = "keyword", defaultValue = "") String keyword) {
        // 获取分页信息
        PageInfo<Role> pageInfo = roleService.getPageInfo(pageNum, pageSize, keyword);
        // 封装到ResultEntity对象中返回，由框架异常处理机制处理异常
        return ResultEntity.successWithData(pageInfo);
    }

    @ResponseBody
    @RequestMapping("/role/save.json")
    public ResultEntity<String> saveRole(Role role) {
        roleService.save(role);
        return ResultEntity.successWithoutData();
    }

    @ResponseBody
    @RequestMapping("/role/update.json")
    public ResultEntity<String> updateRole(Role role){
        roleService.update(role);
        return ResultEntity.successWithoutData();
    }

    @ResponseBody
    @RequestMapping("/role/remove/by/role/id/array.json")
    public ResultEntity<String> removeRole(@RequestBody List<Integer> roleIdList){
        for (Integer integer : roleIdList) {
            System.out.println("=================>" + integer);
        }
        roleService.removeRole(roleIdList);
        return ResultEntity.successWithoutData();
    }
}
