package com.model;

import lombok.Data;

/**
 * @ClassName UsesLogDo
 * @Description: TODO
 * @Author xizhonghuai
 * @Date 2020/4/23
 * @Version V1.0
 **/
@Data
public class UsesLogDo extends BaseModel {
//    id	int
//    auth_code	String
//    order_id	String
//    duration	int
//    cost	float
//    create_date	dateTime

    private String deviceId;
    private String orderId;
    private Integer duration;
    private Float cost;


}