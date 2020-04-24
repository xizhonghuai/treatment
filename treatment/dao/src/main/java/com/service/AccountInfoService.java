package com.service;

import com.mapper.AccountInfoMapper;
import com.model.AccountInfoDo;
import org.springframework.stereotype.Component;

import java.util.HashMap;

/**
 * @ClassName AccountInfoService
 * @Description: TODO
 * @Author xizhonghuai
 * @Date 2020/4/23
 * @Version V1.0
 **/
@Component
public class AccountInfoService extends BaseService<AccountInfoMapper, AccountInfoDo> {


    public void deleteByPrimary(Integer id, String account) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("isActivate", false);
        map.put("id", id);
        map.put("account", account);
        updateByPrimary(map);
    }


}
