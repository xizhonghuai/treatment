package com.service;

import com.Dao;
import com.common.DaoBeans;
import com.mapper.BaseMapper;
import com.mapper.EmpowerMapper;
import com.model.EmpowerDo;
import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;

/**
 * @ClassName EmpowerService
 * @Description: TODO
 * @Author xizhonghuai
 * @Date 2020/4/23
 * @Version V1.0
 **/
@Component
public class EmpowerService extends BaseService<EmpowerMapper,EmpowerDo> {

    @Autowired
    private Dao dao;

    public void deleteByDeviceId(String id){
        SqlSession sqlSession = dao.getSqlSessionFactory().openSession();
        EmpowerMapper mapper =  sqlSession.getMapper(EmpowerMapper.class);
        mapper.deleteByDeviceId(id);
        sqlSession.commit();
        sqlSession.close();
    }


    public void deleteByAuthCode(String id){
        SqlSession sqlSession = dao.getSqlSessionFactory().openSession();
        EmpowerMapper mapper =  sqlSession.getMapper(EmpowerMapper.class);
        mapper.deleteByAuthCode(id);
        sqlSession.commit();
        sqlSession.close();
    }



}
