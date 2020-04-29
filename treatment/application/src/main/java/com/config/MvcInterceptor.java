package com.config;

import com.common.DBConstantUnit;
import com.model.AccountInfoDo;
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
        if ("/".equals(url)){
            response.sendRedirect("http://39.98.164.168:8080/treaweb/webmis/login.html");
            return false;
        }
        if (url.indexOf("/api/account/reg")>0){
            return true;
        }

        if (url.indexOf("/api/account/login")>0){
            return true;
        }

        if (url.indexOf("/api/debug")>0){
            return true;
        }



        AccountInfoDo accountInfoDo = new AccountInfoDo();
        accountInfoDo.setAccount("admin");
        accountInfoDo.setAccountType(DBConstantUnit.ACCOUNT_ADMIN);
        accountInfoDo.setIsActivate(true);

        AuthContext.get().setObj(accountInfoDo);









        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, @Nullable Exception ex) throws Exception {


        AuthContext.get().clear();
    }


}
