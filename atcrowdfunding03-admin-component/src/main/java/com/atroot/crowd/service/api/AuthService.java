package com.atroot.crowd.service.api;

import com.atroot.crowd.entity.Auth;

import java.util.List;
import java.util.Map;

/**
 * Description:
 *
 * @author atroot@126.com  @ZYD
 * @create 2021.6.25 16:46
 */
public interface AuthService {
    List<Auth> getAll();

    List<Integer> getAssignedAuthByRoleId(Integer roleId);

    void saveRelationship(Map<String, List<Integer>> map);
}
