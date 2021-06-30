package com.atroot.crowd.mvc.config;

import com.atroot.crowd.constant.CrowdConstant;
import com.atroot.crowd.exception.LoginAcctAlreadyInUseException;
import com.atroot.crowd.exception.LoginAcctAlreadyInUseForUpdateException;
import com.atroot.crowd.exception.LoginFailedException;
import com.atroot.crowd.util.CrowdUtil;
import com.atroot.crowd.util.ResultEntity;
import com.google.gson.Gson;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Description: 异常处理类
 *
 * @author atroot@126.com  @ZYD
 * @create 2021.5.25 10:33
 */
@ControllerAdvice
public class CrowdExceptionResolver {

    @ExceptionHandler(value = LoginAcctAlreadyInUseForUpdateException.class)
    public ModelAndView resolveLoginAcctAlreadyInUseForUpdateException(Exception exception,
                                                                       HttpServletRequest request,
                                                                       HttpServletResponse response) throws IOException {
//        String viewName = "admin-edit";
        String viewName = "system-error";
        ModelAndView modelAndView = commonExceptionResolve(exception, request, response, viewName);
        return modelAndView;
    }

    @ExceptionHandler(value = LoginFailedException.class)
    public ModelAndView resolveLoginFailedException(Exception exception,
                                                    HttpServletRequest request,
                                                    HttpServletResponse response) throws IOException {
        String viewName = "admin-login";
        ModelAndView modelAndView = commonExceptionResolve(exception, request, response, viewName);
        return modelAndView;
    }

    @ExceptionHandler(value = LoginAcctAlreadyInUseException.class)
    public ModelAndView resolveLoginAcctAlreadyInUseException(Exception exception,
                                                              HttpServletRequest request,
                                                              HttpServletResponse response) throws IOException {
        String viewName = "admin-add";
        ModelAndView modelAndView = commonExceptionResolve(exception, request, response, viewName);
        return modelAndView;
    }

    @ExceptionHandler(value = NullPointerException.class)
    public ModelAndView resolveNullPointerException(Exception exception,
                                                    HttpServletRequest request,
                                                    HttpServletResponse response) throws IOException {
        String viewName = "system-error";
        ModelAndView modelAndView = commonExceptionResolve(exception, request, response, viewName);
        return modelAndView;
    }

    @ExceptionHandler(value = ArithmeticException.class)
    public ModelAndView resolverArithmeticException(Exception exception,
                                                    HttpServletRequest request,
                                                    HttpServletResponse response) throws IOException {
        String viewName = "system-error";
        ModelAndView modelAndView = commonExceptionResolve(exception, request, response, viewName);
        return modelAndView;
    }


    private ModelAndView commonExceptionResolve(Exception exception,
                                                HttpServletRequest request,
                                                HttpServletResponse response,
                                                String viewName) throws IOException {
        String message = exception.getMessage();
        boolean requestType = CrowdUtil.judgeRequestType(request);
//        if = ajax
        if (requestType) {
            ResultEntity<Object> resultEntity = ResultEntity.failed(message);
            Gson gson = new Gson();
            String json = gson.toJson(resultEntity);
            response.getWriter().write(json);
//            已响应 不返回
            return null;
        }
        //        不是Ajax请求
//        将Exception放入ModelAndView返回
        ModelAndView mav = new ModelAndView();
        mav.addObject(CrowdConstant.ATTR_NAME_EXCEPTION, exception);
        mav.setViewName(viewName);
        return mav;
    }
}
