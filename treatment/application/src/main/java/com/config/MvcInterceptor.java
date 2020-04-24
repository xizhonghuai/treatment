package com.config;

import org.springframework.lang.Nullable;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @ClassName MvcInterceptor
 * @Description: TODO
 * @Author xizhonghuai
 * @Date 2020/4/15
 * @Version V1.0
 **/

public class MvcInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String url = request.getRequestURI();
        System.out.println(url);
        if (url.indexOf("/api/account/reg")>0){
            return true;
        }

        if (url.indexOf("/api/account/login")>0){
            return true;
        }






        //        UserDO userDO = (UserDO) request.getSession().getAttribute("token");
//        UserDO userDO = new UserDO();
//        userDO.setUsername("root");
//        userDO.setAuthCode("@");
//        AuthContext.getInstance().setObj(userDO);
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, @Nullable Exception ex) throws Exception {
//        AuthContext.getInstance().clear();
    }


}
