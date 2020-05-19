package com.model;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

/**
 * @ClassName AccountInfoDo
 * @Description: TODO
 * @Author xizhonghuai
 * @Date 2020/4/23
 * @Version V1.0
 **/
@Data
public class AccountInfoDo extends BaseModel {
//    id	int
//    account	String
//    password	String
//    auth_code	String
//    account_type	int
//    username	String
//    company	String
//    sex	int
//    age	int
//    address	String
//    tel	String
//    is_activate	bool
//    create_date	dateTime

    private String account;
//    @JSONField(serialize=false)
    private String password;
    private Integer accountType;
    private String username;
    private String company;
    private String tel;
    private Integer sex;
    private Integer age;
    private String address;
    private Boolean isActivate;
    private String token;





}
