package com.mapper;

import java.util.HashMap;
import java.util.List;

public interface BaseMapper<T> {
    void insert(T t);
    List<T> select(HashMap<String, Object> map);
    List<T> selectAll();
    void updateByPrimary(HashMap<String, Object> map);
}
