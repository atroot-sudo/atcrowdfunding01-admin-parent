package com.atroot.crowd.service.impl;

import com.atroot.crowd.entity.Auth;
import com.atroot.crowd.entity.AuthExample;
import com.atroot.crowd.mapper.AuthMapper;
import com.atroot.crowd.service.api.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * Description:
 *
 * @author atroot@126.com  @ZYD
 * @create 2021.6.25 16:46
 */
@Service
public class AuthServiceImpl implements AuthService {
    @Autowired
    AuthMapper authMapper;

    @Override
    public List<Auth> getAll() {
        return authMapper.selectByExample(new AuthExample());
    }

    @Override
    public List<Integer> getAssignedAuthByRoleId(Integer roleId) {
        return authMapper.selectAssignedAuthIdByRoleId(roleId);
    }

    @Override
    public void saveRelationship(Map<String, List<Integer>> map) {
        List<Integer> roleIdList = map.get("roleId");
        for (Integer integer : roleIdList) {
            System.out.println("roleIdList数据为：" + integer);
        }
        Integer roleId = roleIdList.get(0);
        // 删除旧数据
        authMapper.deleteOldRelationship(roleId);

        // 获取authIdList
        List<Integer> authIdList = map.get("authIdArray");
        for (Integer integer : authIdList) {
            System.out.println("authIdList数据为：" + integer);
        }

        // 判断authIdList是否有效
        if(authIdList != null && authIdList.size() > 0){
            authMapper.insertNewRelationship(roleId,authIdList);
        }

    }
}
