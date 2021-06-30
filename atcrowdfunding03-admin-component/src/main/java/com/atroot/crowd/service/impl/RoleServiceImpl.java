package com.atroot.crowd.service.impl;

import com.atroot.crowd.entity.Role;
import com.atroot.crowd.entity.RoleExample;
import com.atroot.crowd.mapper.RoleMapper;
import com.atroot.crowd.service.api.RoleService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Description:
 *
 * @author atroot@126.com  @ZYD
 * @create 2021.6.8 20:30
 */
@Service
public class RoleServiceImpl implements RoleService {
    @Autowired
    RoleMapper roleMapper;

    @Override
    public PageInfo<Role> getPageInfo(Integer pageNum, Integer pageSize, String keyword) {
        // 开启分页功能
        PageHelper.startPage(pageNum, pageSize);
        // 调用Service查询分页数据
        List<Role> roles = roleMapper.selectRoleByKeyword(keyword);
        // 封装为PageInfo返回
        return new PageInfo<>(roles);
    }

    @Override
    public void save(Role role) {
        roleMapper.insert(role);
    }

    @Override
    public void update(Role role) {
        roleMapper.updateByPrimaryKey(role);
    }

    @Override
    public void removeRole(List<Integer> roleIdList) {
        RoleExample roleExample = new RoleExample();
        RoleExample.Criteria criteria = roleExample.createCriteria();
        criteria.andIdIn(roleIdList);
        roleMapper.deleteByExample(roleExample);
    }

    @Override
    public List<Role> getAssignRole(Integer adminId) {
        return roleMapper.selectAssignRole(adminId);
    }

    @Override
    public List<Role> getUnAssignRoleList(Integer adminId) {
        return roleMapper.selectUnAssignRole(adminId);
    }
}
