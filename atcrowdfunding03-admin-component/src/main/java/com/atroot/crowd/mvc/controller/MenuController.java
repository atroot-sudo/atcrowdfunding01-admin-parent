package com.atroot.crowd.mvc.controller;

import com.atroot.crowd.entity.Menu;
import com.atroot.crowd.service.api.MenuService;
import com.atroot.crowd.util.ResultEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Description:
 *
 * @author atroot@126.com  @ZYD
 * @create 2021.6.17 20:25
 */
@Controller
public class MenuController {
    @Autowired
    private MenuService menuService;


    @ResponseBody
    @RequestMapping("/menu/get/whole/tree.json")
    public ResultEntity<Menu> getWholeTreeNew() {
        // 查询全部的Menu对象
        List<Menu> menuList = menuService.getAll();

        // 声明一个变量来存储根节点
        Menu root = null;

        // 声明一个Map,用于存储id与节点之间的对应关系
        Map<Integer, Menu> menuMap = new HashMap<>();

        // 遍历所有的menuList，填充Map
        for (Menu menu : menuList) {
            Integer id = menu.getId();
            menuMap.put(id, menu);
        }

        // 再次遍历menuList查找根节点、组装父子节点
        for (Menu menu : menuList) {
            // 获取根节点id
            Integer pid = menu.getPid();

            // 判定如果pid为null，则说明此节点为根节点
            if (pid == null) {
                root = menu;

                // 父节点没有父节点直接continue返回
                continue;
            }
            // 如果pid不为空，那么就可以找到他的父节点
            Menu father = menuMap.get(pid);
            // 那么将当前节点存入父节点的children结合
            father.getChildren().add(menu);
        }
        // 根据上面的运算，已经算出了整个树，只要返回根节点就是返回整个树
        return ResultEntity.successWithData(root);
    }

    @ResponseBody
    @RequestMapping("/menu/save.json")
    public ResultEntity<String> saveMenu(Menu menu){

        menuService.saveMenu(menu);
        return ResultEntity.successWithoutData();
    }

    @ResponseBody
    @RequestMapping("/menu/update.json")
    public ResultEntity<String> updateMenu(Menu menu){

        menuService.updateMenu(menu);
        return ResultEntity.successWithoutData();
    }

    @ResponseBody
    @RequestMapping("/menu/remove.json")
    public ResultEntity<String> removeMenu(@RequestParam("id") Integer id){
        menuService.removeMenu(id);
        return ResultEntity.successWithoutData();

    }


}
