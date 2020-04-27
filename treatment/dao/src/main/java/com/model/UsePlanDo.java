package com.model;

import lombok.Data;

/**
 * @ClassName UsePlanDo
 * @Description: TODO
 * @Author xizhonghuai
 * @Date 2020/4/23
 * @Version V1.0
 **/
@Data
public class UsePlanDo extends BaseModel {
//    id	int
//    auth_code	String
//    order_id	String
//    duration	int
//    cost	float
//    create_date	dateTime

    private String account;
    private String username;
    private String tel;
    private String deviceId;
    private String orderId;
    private Integer duration;
    private Integer realDuration;
    private Float cost;
    private String beforeState;
    private String afterState;






}
