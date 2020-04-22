package com.service;

import com.Dao;
import com.common.Context;
import com.common.DaoBeans;
import com.mapper.BaseMapper;
import com.model.BaseModel;
import com.model.UserDO;
import org.apache.ibatis.session.SqlSession;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * @ClassName BaseService
 * @Description: TODO
 * @Author xizhonghuai
 * @Date 2020/4/9
 * @Version V1.0
 **/

public class BaseService<Mapper, Model> {

    private Class<Mapper> mapperClass;

    public BaseService() {

        Type type = getClass().getGenericSuperclass();
        Type trueType = ((ParameterizedType) type).getActualTypeArguments()[0];
        this.mapperClass = (Class<Mapper>) trueType;

    }

    public void insert(Model model) {

        if (model instanceof BaseModel) {
            UserDO userDO = (UserDO) Context.getInstance().getObj();
            if (userDO == null) {
                return;
            }
            ((BaseModel) model).setAuthCode(userDO.getAuthCode());
        }

        Dao dao = (Dao) DaoBeans.getBean("dao");
        SqlSession sqlSession = dao.getSqlSessionFactory().openSession();
        BaseMapper baseMapper = (BaseMapper) sqlSession.getMapper(mapperClass);
        baseMapper.insert(model);
        sqlSession.commit();
        sqlSession.close();


    }

    public List<Model> select(HashMap<String, Object> map) {
        //查询时可手动传入authCode
        if (map.get("authCode") == null){
            UserDO userDO = (UserDO) Context.getInstance().getObj();
            if (userDO == null) {
                return new ArrayList<>();
            }
            map.put("authCode", userDO.getAuthCode());
        }
        Dao dao = (Dao) DaoBeans.getBean("dao");
        SqlSession sqlSession = dao.getSqlSessionFactory().openSession();
        BaseMapper baseMapper = (BaseMapper) sqlSession.getMapper(mapperClass);
        List<Model> list = baseMapper.select(map);
        sqlSession.close();
        return list;

    }

    public List<Model> selectAll() {
        Dao dao = (Dao) DaoBeans.getBean("dao");
        SqlSession sqlSession = dao.getSqlSessionFactory().openSession();
        BaseMapper baseMapper = (BaseMapper) sqlSession.getMapper(mapperClass);
        List<Model> list = baseMapper.selectAll();
        sqlSession.close();
        return list;

    }


    public void updateByPrimary(HashMap<String, Object> map){
        UserDO userDO = (UserDO) Context.getInstance().getObj();
        if (userDO == null) {
            return;
        }
        map.put("authCode", userDO.getAuthCode());
        Dao dao = (Dao) DaoBeans.getBean("dao");
        SqlSession sqlSession = dao.getSqlSessionFactory().openSession();
        BaseMapper baseMapper = (BaseMapper) sqlSession.getMapper(mapperClass);
        baseMapper.updateByPrimary(map);
        sqlSession.commit();
        sqlSession.close();
    }


}
