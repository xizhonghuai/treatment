package com.config;

import com.alibaba.fastjson.JSON;
import com.cache.TokenManager;
import com.init.Initialization;
import com.model.AccountInfoDo;
import org.springframework.lang.Nullable;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.HashMap;

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

        if ("/".equals(url)) {
            response.sendRedirect(Initialization.webUrl + "/treaweb/webmis/login.html");
            return false;
        }
        if (url.indexOf("api/account/reg") > 0) {
            return true;
        }

        if (url.indexOf("api/account/login") > 0) {
            return true;
        }

        if (url.indexOf("api/debug/") > 0) {
            return true;
        }

        try {
            String token = null;
            token = request.getParameter("token");
            if (token == null){
                HashMap<String,Object> body= JSON.parseObject(ReadAsBody(request),HashMap.class);
                token = (String) body.get("token");
            }

            if (token != null){
                AccountInfoDo accountInfo = TokenManager.getAccountInfo(token);
                if (accountInfo != null){
                    AuthContext.get().setObj(accountInfo);
                    return true;
                }
            }
        }catch (Exception e){
          e.printStackTrace();

        }

//        response.sendRedirect(Initialization.webUserUrl + "/webuser/login.html?code=1001");
        return false;


//        try {
//            System.out.println("token:   "+request.getHeader("token"));
//            String token = request.getHeader("token");
//            if (token != null){
//                AccountInfoDo accountInfo = TokenManager.getAccountInfo(token);
//                AuthContext.get().setObj(accountInfo);
//            }else {
//                RestResult restResult = new RestResult("err","5005");
//                response.getWriter().write(JSON.toJSONString(restResult));
//                return false;
//            }
//        }catch (Exception e){
//        }

    }


    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, @Nullable Exception ex) throws Exception {


        AuthContext.get().clear();
    }


    private   String ReadAsBody(HttpServletRequest request) {

        BufferedReader br = null;
        StringBuilder sb = new StringBuilder("");
        try {
            br = request.getReader();
            String str;
            while ((str = br.readLine()) != null) {
                sb.append(str);
            }
            br.close();
        } catch (Exception e) {
//            e.printStackTrace();
        } finally {
            if (null != br) {
                try {
                    br.close();
                } catch (IOException e) {
//                    e.printStackTrace();
                }
            }
        }
        return sb.toString();
    }


}
