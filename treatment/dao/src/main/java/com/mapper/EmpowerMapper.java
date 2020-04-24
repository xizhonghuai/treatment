package com.mapper;

import com.model.EmpowerDo;

public interface EmpowerMapper extends BaseMapper<EmpowerDo> {
    void deleteByDeviceId(String id);
    void deleteByAuthCode(String id);

}
