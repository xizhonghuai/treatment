package com.config;

import com.common.Context;
import com.model.UserDO;
import org.springframework.lang.Nullable;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @ClassName MvcInterceptor
 * @Description: TODO
 * @Author xizhonghuai
 * @Date 2020/4/9
 * @Version V1.0
 **/

public class MvcInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
//        UserDO userDO = (UserDO) request.getSession().getAttribute("token");
        UserDO userDO = new UserDO();
        userDO.setUsername("root");
        userDO.setAuthCode("@");
        Context.getInstance().setObj(userDO);
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, @Nullable Exception ex) throws Exception {
        Context.getInstance().clear();
    }


}
