package com.service;

import com.Dao;
import com.alibaba.fastjson.JSON;
import com.common.DaoBeans;
import com.mapper.BaseMapper;
import org.apache.ibatis.session.SqlSession;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
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
        Dao dao = (Dao) DaoBeans.getBean("dao");
        SqlSession sqlSession = dao.getSqlSessionFactory().openSession();
        BaseMapper baseMapper = (BaseMapper) sqlSession.getMapper(mapperClass);
        baseMapper.insert(model);
        sqlSession.commit();
        sqlSession.close();


    }

    public List<Model> select(HashMap<String, Object> map) {
        System.out.println("select sql par:"+ JSON.toJSON(map));
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
        Dao dao = (Dao) DaoBeans.getBean("dao");
        SqlSession sqlSession = dao.getSqlSessionFactory().openSession();
        BaseMapper baseMapper = (BaseMapper) sqlSession.getMapper(mapperClass);
        baseMapper.updateByPrimary(map);
        sqlSession.commit();
        sqlSession.close();
    }


    public void deleteByPrimary(HashMap<String,Object> map){
        Dao dao = (Dao) DaoBeans.getBean("dao");
        SqlSession sqlSession = dao.getSqlSessionFactory().openSession();
        BaseMapper baseMapper = (BaseMapper) sqlSession.getMapper(mapperClass);
        baseMapper.deleteByPrimary(map);
        sqlSession.commit();
        sqlSession.close();
    }


}
