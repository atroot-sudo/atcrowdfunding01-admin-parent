package com.atroot.crowd.mvc.interceptor;

import com.atroot.crowd.constant.CrowdConstant;
import com.atroot.crowd.entity.Admin;
import com.atroot.crowd.exception.AccessForbiddenException;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Description: 定义拦截器，实行登录拦截
 *
 * @author atroot@126.com  @ZYD
 * @create 2021.5.30 14:07
 */
public class LoginInterceptor extends HandlerInterceptorAdapter {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 检查登录状态
        // 获取session对象
        HttpSession session = request.getSession();
        // 尝试从session域中获取admin对象
        Admin admin = (Admin) session.getAttribute(CrowdConstant.ATTR_NAME_LOGIN_ADMIN);

        // 判断admin对象是否为空
        if (admin == null) {
            // 若为空则抛出异常，不放行
            throw new AccessForbiddenException(CrowdConstant.MESSAGE_ACCESS_FORBIDDEN);
        }
        // 如果admin对象不为空，则返回true放行
        return true;
    }
}
