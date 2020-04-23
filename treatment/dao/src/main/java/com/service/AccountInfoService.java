package com.service;

import com.Dao;
import com.common.AuthContext;
import com.common.DBConstantUnit;
import com.mapper.AccountInfoMapper;
import com.mapper.BaseMapper;
import com.model.AccountInfoDo;
import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;

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


    public void insert(AccountInfoDo accountInfoDo) {
        AccountInfoDo loginAccount = super.getLoginAccount();
        if (loginAccount != null && loginAccount.getAccountType() != DBConstantUnit.ACCOUNT_USER) {
            SqlSession sqlSession = dao.getSqlSessionFactory().openSession();
            AccountInfoMapper mapper =  sqlSession.getMapper(AccountInfoMapper.class);
            mapper.insert(accountInfoDo);
            sqlSession.commit();
            sqlSession.close();
        }
    }


    public List<AccountInfoDo> select(HashMap<String, Object> map) {
        AccountInfoDo loginAccount = super.getLoginAccount();
        if (loginAccount != null && loginAccount.getAccountType() == DBConstantUnit.ACCOUNT_ADMIN) {
            SqlSession sqlSession = dao.getSqlSessionFactory().openSession();
            AccountInfoMapper mapper =  sqlSession.getMapper(AccountInfoMapper.class);
            List<AccountInfoDo> list = mapper.select(map);
            sqlSession.close();
            return list;
        }
        return null;
    }


//    public void updateByPrimary(HashMap<String, Object> map){
//
//        AccountInfoDo loginAccount = super.getLoginAccount();
//        if (loginAccount != null && loginAccount.getAccountType() == DBConstantUnit.ACCOUNT_ADMIN) {
//
//
//
//
//
//
//        }
//
//
//
//        map.put("authCode", userDO.getAuthCode());
//        Dao dao = (Dao) DaoBeans.getBean("dao");
//        SqlSession sqlSession = dao.getSqlSessionFactory().openSession();
//        BaseMapper baseMapper = (BaseMapper) sqlSession.getMapper(mapperClass);
//        baseMapper.updateByPrimary(map);
//        sqlSession.commit();
//        sqlSession.close();
//    }


}
