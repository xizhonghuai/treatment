package com.model;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.util.Date;

/**
 * @ClassName BaseModel
 * @Description: TODO
 * @Author xizhonghuai
 * @Date 2020/4/9
 * @Version V1.0
 **/
@Data
public class BaseModel {

    private Integer id;
    private String description;
    private Date createDate = new Date();
    @JSONField(serialize=false)
    private String authCode;
}
