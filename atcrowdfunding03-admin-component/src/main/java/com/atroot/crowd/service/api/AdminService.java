package com.atroot.crowd.service.api;

import com.atroot.crowd.entity.Admin;
import com.github.pagehelper.PageInfo;

import java.util.List;

/**
 * Description:
 *
 * @author atroot@126.com  @ZYD
 * @create 2021.5.20 10:58
 */
public interface AdminService {
    /**
     * 保存一个Admin
     *
     * @param admin 传入Admin对象
     */
    void saveAdmin(Admin admin);

    /**
     * 获取所有Admin
     *
     * @return 返回List<Admin>
     */
    List<Admin> getAll();

    /**
     * 通过用户名和密码来查询用户
     *
     * @param loginAcct 用户名
     * @param userPswd  密码
     * @return 返回一个Admin对象
     */
    Admin getAdminByLoginAcct(String loginAcct, String userPswd);

    /**
     * 获取分页信息
     *
     * @param keyword  关键字
     * @param pageNum  当前页
     * @param pageSize 每页的大小
     * @return 返回PageInfo<Admin>
     */
    PageInfo<Admin> getPageInfo(String keyword, Integer pageNum, Integer pageSize);

    /**
     * 通过id删除一个Admin
     *
     * @param id 传入参数id
     */
    void remove(Integer id);

    /**
     * 通过id查询一个Admin
     *
     * @param id 传入一个参数id
     * @return 返回Admin对象
     */
    Admin getAdminById(Integer id);

    /**
     * 更新一个Admin
     *
     * @param admin 传入一个admin(修改过)
     */
    void updateAdmin(Admin admin);

    void saveAdminRoleRelationship(Integer adminId, List<Integer> roleIdList);
}
