package com.controller;

import com.cache.DeviceMsgCache;
import com.common.DBConstantUnit;
import com.config.LoginAccount;
import com.config.SystemConstantUnit;
import com.model.AccountInfoDo;
import com.model.DeviceMsgDo;
import com.model.EmpowerDo;
import com.service.DeviceInfoService;
import com.service.DeviceMsgService;
import com.service.EmpowerService;
import lib.RestResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @ClassName DeviceMsgController
 * @Description: TODO
 * @Author xizhonghuai
 * @Date 2020/4/24
 * @Version V1.0
 **/
@Slf4j
@CrossOrigin
@RestController
@RequestMapping("/api/devicemsg/")
public class DeviceMsgController {

    @Autowired
    private LoginAccount loginAccount;

    @Autowired
    private DeviceInfoService deviceInfoService;

    @Autowired
    private DeviceMsgService deviceMsgService;

    @Autowired
    private EmpowerService empowerService;


    @Autowired
    private DeviceMsgCache deviceMsgCache;

    @RequestMapping(value = "/getRealTimeData", method = RequestMethod.GET)
    public RestResult<List<DeviceMsgDo>> getRealTimeData(
            @RequestParam(value = "deviceId", required = false) String deviceId
    ) {
        try {
            AccountInfoDo accountInfoDo = loginAccount.get();
//            HashMap<String, Object> map = new HashMap();
//            map.put("deviceId", deviceId);

            if  (accountInfoDo.getAccountType() == DBConstantUnit.ACCOUNT_USER) {
                return new RestResult(SystemConstantUnit.PERMISSION, "10001");
            }

            if (accountInfoDo.getAccountType() == DBConstantUnit.ACCOUNT_ADMIN) {
                new RestResult<>(new ArrayList<>(deviceMsgCache.getCache().values()));
            }

            HashMap<String, Object> empowerPar = new HashMap<>();
            empowerPar.put("authCode", accountInfoDo.getAuthCode());
            List<EmpowerDo> empowers = empowerService.select(empowerPar);
            if (empowers.size() > 0) {

                List<DeviceMsgDo> deviceMsgDos = new ArrayList<>(deviceMsgCache.getCache().values());
                List<String> empDevices = empowers.stream().map(d -> d.getDeviceId()).collect(Collectors.toList());
                List<DeviceMsgDo> collect = deviceMsgDos.stream().filter(d -> empDevices.contains(d.getDeviceId())).collect(Collectors.toList());
                new RestResult<>(collect);
            }
            return new RestResult();
        } catch (Exception e) {
            return new RestResult("err:" + e.getMessage(), "10001");
        }
    }


    @RequestMapping(value = "/getmsg", method = RequestMethod.GET)
    public RestResult<List<DeviceMsgDo>> getDeviceSate(
            @RequestParam(value = "deviceId", required = false) String deviceId,
            @RequestParam(value = "authCode", required = false) String authCode,
            @RequestParam(value = "orderId", required = false) String orderId,
            @RequestParam(value = "beginDate", required = false) Date beginDate,
            @RequestParam(value = "endDate", required = false) Date endDate
    ) {
        try {
            AccountInfoDo accountInfoDo = loginAccount.get();

            HashMap<String, Object> map = new HashMap();
            map.put("deviceId", deviceId);
            map.put("authCode", authCode);
            map.put("orderId", orderId);
            map.put("beginDate", beginDate);
            map.put("endDate", endDate);

            if (accountInfoDo.getAccountType() == DBConstantUnit.ACCOUNT_ADMIN) {
                new RestResult<>(deviceInfoService.select(map));
            }

            HashMap<String, Object> empowerPar = new HashMap<>();
            empowerPar.put("authCode", accountInfoDo.getAuthCode());
            List<EmpowerDo> empowers = empowerService.select(empowerPar);
            if (empowers.size() > 0) {
                List<DeviceMsgDo> deviceMsgDos = deviceMsgService.select(map);
                List<String> empDevices = empowers.stream().map(d -> d.getDeviceId()).collect(Collectors.toList());
                List<DeviceMsgDo> collect = deviceMsgDos.stream().filter(d -> empDevices.contains(d.getDeviceId())).collect(Collectors.toList());

                new RestResult<>(collect);
            }
            return new RestResult();
        } catch (Exception e) {
            return new RestResult("err:" + e.getMessage(), "10001");
        }
    }


}
