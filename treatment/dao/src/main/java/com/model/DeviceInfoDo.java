package com.model;

import com.mapper.BaseMapper;
import lombok.Data;

/**
 * @ClassName DeviceInfoDo
 * @Description: TODO
 * @Author xizhonghuai
 * @Date 2020/4/23
 * @Version V1.0
 **/
@Data
public class DeviceInfoDo extends BaseModel {

//    id	int
//    device_id	String
//    device_name	String
//    city_id	int
//    address	String
//    longitude	String
//    latitude	String
//    nurse	String
//    tel	String
//    dtu_id	String
//    create_date	dateTime

    private String deviceId;
    private String deviceName;
    private Integer cityId;
    private String address;
    private String longitude;
    private String latitude;
    private String nurse;
    private String tel;
    private String dtuId;

    private String ranCityName;


}
