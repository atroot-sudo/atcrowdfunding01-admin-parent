package com.atroot.crowd.service.api;

import com.atroot.crowd.entity.Role;
import com.github.pagehelper.PageInfo;

import java.util.List;

/**
 * Description:
 *
 * @author atroot@126.com  @ZYD
 * @create 2021.6.8 20:30
 */
public interface RoleService {
    /**
     * 获取分页信息
     * @param pageNum  当前页
     * @param pageSize 每页的数量
     * @param keyword 关键字
     * @return 返回一个pageInfo实体
     */
    PageInfo<Role> getPageInfo(Integer pageNum, Integer pageSize, String keyword);

    /**
     * 保存Role
     * @param role 传入Role对象
     */
    void save(Role role);

    void update(Role role);

    void removeRole(List<Integer> roleIdList);

    List<Role> getAssignRole(Integer adminId);

    List<Role> getUnAssignRoleList(Integer adminId);
}
