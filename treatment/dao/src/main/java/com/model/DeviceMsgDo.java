package com.model;

import lombok.Data;

/**
 * @ClassName DeviceMsgDo
 * @Description: TODO
 * @Author xizhonghuai
 * @Date 2020/4/23
 * @Version V1.0
 **/
@Data
public class DeviceMsgDo extends BaseModel {

//    id	int
//    devivce_id	String
//    auth_code	String
//    order_id	String
//    serial_id	int
//    msg_body	String
//    create_date	dateTime

    private String deviceId;
    private String orderId;
    private Integer serialId;
    private String msgBody;



}
