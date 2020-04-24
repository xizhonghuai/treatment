package com.config;

import com.model.AccountInfoDo;
import org.springframework.stereotype.Component;

/**
 * @ClassName LoginAccount
 * @Description: TODO
 * @Author xizhonghuai
 * @Date 2020/4/24
 * @Version V1.0
 **/
@Component
public class LoginAccount {
    public AccountInfoDo get() throws Exception {
        AccountInfoDo account = (AccountInfoDo) AuthContext.get().getObj();
        if (account == null || !account.getIsActivate()){
               throw new Exception(SystemConstantUnit.PERMISSION);
        }
        return account;
    }
}
