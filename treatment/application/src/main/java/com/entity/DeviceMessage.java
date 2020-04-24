package com.entity;

import lombok.Data;

import java.util.Date;
import java.util.HashMap;

/**
 * @ClassName DeviceMessage
 * @Description: TODO
 * @Author xizhonghuai
 * @Date 2020/4/24
 * @Version V1.0
 **/
@Data
public class DeviceMessage {

    //    private String userId;
//    private String orderId;
//    private String serialId;

    private String id;
    private HashMap<String, Object> body;
    private Date date = new Date();

    public DeviceMessage(String id) {
        this.id = id;
    }
}
