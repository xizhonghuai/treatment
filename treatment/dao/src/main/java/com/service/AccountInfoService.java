package com.service;

import com.Dao;
import com.common.DaoBeans;
import com.mapper.AccountInfoMapper;
import com.model.AccountInfoDo;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionManager;
import org.springframework.beans.factory.annotation.Autowired;
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


    @Autowired
    private Dao dao;

    public void deleteByPrimary(Integer id, String account) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("isActivate", false);
        map.put("id", id);
        map.put("account", account);
        updateByPrimary(map);
    }

   /* 数组 长度N  不重复    VALUE <  M*/





}
