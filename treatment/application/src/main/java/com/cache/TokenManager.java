package com.cache;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.model.AccountInfoDo;

import java.util.UUID;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

public class TokenManager {

    private static Cache<String, AccountInfoDo> cache = CacheBuilder.newBuilder()
            .expireAfterAccess(60*50, TimeUnit.SECONDS).build();

    public static AccountInfoDo getAccountInfo(String token) {
        return cache.getIfPresent(token);
    }

    public static void saveAccountTOkenInfo(String token, AccountInfoDo accountInfoDo) {
        cache.put(token, accountInfoDo);
    }

    public static String generateToken() {
        return UUID.randomUUID().toString().replace("-", "");
    }

//
//    public static void main(String[] args) throws InterruptedException {
//
//        AccountInfoDo accountInfoDo = new AccountInfoDo();
//
//        accountInfoDo.setAccount("admin");
//
//        String token = generateToken();
//        TokenManager.saveAccountTOkenInfo(token,accountInfoDo);
//
//        Thread.sleep(1000*3);
//
//        System.out.println(getAccountInfo(token));
//
//        Thread.sleep(1000*4);
//
//        System.out.println(getAccountInfo(token));
//
//    }


}
