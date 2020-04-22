package com.service;

import com.mapper.UserMapper;
import com.model.UserDO;
import org.springframework.stereotype.Component;

/**
 * @ClassName UserService
 * @Description: TODO
 * @Author xizhonghuai
 * @Date 2020/4/9
 * @Version V1.0
 **/
@Component
public class UserService extends BaseService<UserMapper,UserDO> {

    public UserDO getRootUser(){
        UserDO userDO = new UserDO();
        userDO.setUsername("root");
        userDO.setAuthCode("@");
        return userDO;
    }
}
