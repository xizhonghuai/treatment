package com.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.common.DBConstantUnit;
import com.config.LoginAccount;
import com.config.SystemConstantUnit;
import com.model.AccountInfoDo;
import com.model.DeviceInfoDo;
import com.model.DeviceMsgDo;
import com.model.EmpowerDo;
import com.service.DeviceInfoService;
import com.service.DeviceMsgService;
import com.service.EmpowerService;
import lib.RestResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @ClassName DeviceController
 * @Description: TODO
 * @Author xizhonghuai
 * @Date 2020/4/24
 * @Version V1.0
 **/
@Slf4j
@CrossOrigin
@RestController
@RequestMapping("/api/device/")
public class DeviceController {

    @Autowired
    private LoginAccount loginAccount;

    @Autowired
    private DeviceInfoService deviceInfoService;

    @Autowired
    private DeviceMsgService deviceMsgService;

    @Autowired
    private EmpowerService empowerService;

    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public RestResult add(@RequestBody DeviceInfoDo deviceInfoDo) {
        try {

            AccountInfoDo accountInfoDo = loginAccount.get();
            if (accountInfoDo.getAccountType() != DBConstantUnit.ACCOUNT_ADMIN) {
                return new RestResult(SystemConstantUnit.PERMISSION, "10001");
            }
            deviceInfoService.insert(deviceInfoDo);
            return new RestResult();
        } catch (Exception e) {
            return new RestResult("err:" + e.getMessage(), "10001");
        }
    }


    @RequestMapping(value = "/edit", method = RequestMethod.POST)
    public RestResult edit(@RequestBody DeviceInfoDo deviceInfoDo) {
        try {
            AccountInfoDo accountInfoDo = loginAccount.get();
            if (accountInfoDo.getAccountType() != DBConstantUnit.ACCOUNT_ADMIN) {
                return new RestResult(SystemConstantUnit.PERMISSION, "10001");
            }
            if (deviceInfoDo.getDeviceId() == null) {
                return new RestResult("deviceid is null", "10001");
            }
            HashMap map = JSONObject.parseObject(JSON.toJSONString(deviceInfoDo), HashMap.class);
            deviceInfoService.updateByPrimary(map);

            return new RestResult();
        } catch (Exception e) {
            return new RestResult("err:" + e.getMessage(), "10001");
        }
    }


    @RequestMapping(value = "/del", method = RequestMethod.POST)
    public RestResult del(@RequestParam(value = "id") Integer id, @RequestParam(value = "deviceId") String deviceId) {
        try {
            AccountInfoDo accountInfoDo = loginAccount.get();
            if (accountInfoDo.getAccountType() != DBConstantUnit.ACCOUNT_ADMIN) {
                return new RestResult(SystemConstantUnit.PERMISSION, "10011");
            }
            HashMap<String,Object> map = new HashMap();
            map.put("id",id);
            map.put("deviceId",deviceId);
            deviceInfoService.deleteByPrimary(map);
            empowerService.deleteByDeviceId(deviceId);
            return new RestResult();
        } catch (Exception e) {
            return new RestResult("err:" + e.getMessage(), "10001");
        }
    }



    @RequestMapping(value = "/getBaseInfo", method = RequestMethod.GET)
    public RestResult<List<DeviceInfoDo>> getBaseInfo(@RequestParam(value = "id",required = false) Integer id,
                                  @RequestParam(value = "deviceId",required = false) String deviceId,
                                  @RequestParam(value = "deviceName",required = false) String deviceName,
                                  @RequestParam(value = "cityId",required = false) Integer cityId
                                  ) {
        try {
            AccountInfoDo accountInfoDo = loginAccount.get();
            HashMap<String,Object> map = new HashMap();
            map.put("id",id);
            map.put("deviceId",deviceId);
            map.put("deviceName",deviceName);
            map.put("cityId",cityId);

            if (accountInfoDo.getAccountType() == DBConstantUnit.ACCOUNT_ADMIN){
              return   new RestResult<>(deviceInfoService.select(map));
            }

            HashMap<String,Object> empowerPar = new HashMap<>();
            empowerPar.put("authCode",accountInfoDo.getAuthCode());
            List<EmpowerDo> empowers = empowerService.select(empowerPar);
            if(empowers.size()>0){
                List<DeviceInfoDo> deviceInfoDos = deviceInfoService.select(map);
                List<String> empDevices = empowers.stream().map(d -> d.getDeviceId()).collect(Collectors.toList());
                List<DeviceInfoDo> collect = deviceInfoDos.stream().filter(d -> empDevices.contains(d.getDeviceId())).collect(Collectors.toList());
                new RestResult<>(collect);
            }
            return new RestResult();
        } catch (Exception e) {
            return new RestResult("err:" + e.getMessage(), "10001");
        }
    }



//    @RequestMapping(value = "/getDeviceSate", method = RequestMethod.GET)
//    public RestResult<List<DeviceMsgDo>> getDeviceSate(@RequestParam(value = "id",required = false) Integer id,
//                                                      @RequestParam(value = "deviceId",required = false) String deviceId,
//                                                      @RequestParam(value = "deviceName",required = false) String deviceName,
//                                                      @RequestParam(value = "cityId",required = false) Integer cityId,
//                                                      @RequestParam(value = "beginDate",required = false) Date beginDate,
//                                                      @RequestParam(value = "endDate",required = false) Date endDate
//    ) {
//        try {
//            AccountInfoDo accountInfoDo = loginAccount.get();
//            HashMap<String,Object> empowerPar = new HashMap<>();
//            empowerPar.put("authCode",accountInfoDo.getAuthCode());
//            List<EmpowerDo> empowers = empowerService.select(empowerPar);
//            if(empowers.size()>0){
//                HashMap<String,Object> map = new HashMap();
//                map.put("id",id);
//                map.put("deviceId",deviceId);
////                map.put("deviceName",deviceName);
////                map.put("cityId",cityId);
//                map.put("beginDate",beginDate);
//                map.put("endDate",endDate);
////                List<DeviceInfoDo> deviceInfoDos = deviceInfoService.select(map);
//                List<DeviceMsgDo> deviceMsgDos = deviceMsgService.select(map);
//
//
//                List<String> empDevices = empowers.stream().map(d -> d.getDeviceId()).collect(Collectors.toList());
//                List<DeviceMsgDo> collect = deviceMsgDos.stream().filter(d -> empDevices.contains(d.getDeviceId())).collect(Collectors.toList());
//
//                new RestResult<>(collect);
//            }
//            return new RestResult();
//        } catch (Exception e) {
//            return new RestResult("err:" + e.getMessage(), "10001");
//        }
//    }










}
