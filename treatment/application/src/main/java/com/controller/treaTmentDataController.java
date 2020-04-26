package com.controller;

import com.common.DBConstantUnit;
import com.config.LoginAccount;
import com.model.AccountInfoDo;
import com.model.DeviceMsgDo;
import com.model.EmpowerDo;
import com.model.UsesLogDo;
import com.service.DeviceInfoService;
import com.service.DeviceMsgService;
import com.service.EmpowerService;
import com.service.UsesLogService;
import lib.RestResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @ClassName UseDataController
 * @Description: TODO
 * @Author xizhonghuai
 * @Date 2020/4/24
 * @Version V1.0
 **/
@Slf4j
@CrossOrigin
@RestController
@RequestMapping("/api/data/")
public class TreatmentDataController {

    @Autowired
    private LoginAccount loginAccount;

    @Autowired
    private DeviceMsgService deviceMsgService;

    @Autowired
    private EmpowerService empowerService;

    @Autowired
    private UsesLogService usesLogService;


    @RequestMapping(value = "/getDetailsData", method = RequestMethod.GET)
    public RestResult<List<DeviceMsgDo>> getDetailsData(
            @RequestParam(value = "authCode", required = false) String authCode,
            @RequestParam(value = "orderId", required = false) String orderId,
            @RequestParam(value = "beginDate", required = false) Date beginDate,
            @RequestParam(value = "endDate", required = false) Date endDate
    ) {
        try {
            AccountInfoDo accountInfoDo = loginAccount.get();
            HashMap<String, Object> map = new HashMap();
            map.put("authCode", authCode);
            map.put("orderId", orderId);
            map.put("beginDate", beginDate);
            map.put("endDate", endDate);
            if (accountInfoDo.getAccountType()==DBConstantUnit.ACCOUNT_USER){
                map.put("authCode", accountInfoDo.getAuthCode());
                new RestResult<>(deviceMsgService.select(map));
            }


            if (accountInfoDo.getAccountType() == DBConstantUnit.ACCOUNT_ADMIN) {
                new RestResult<>(deviceMsgService.select(map));
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



    @RequestMapping(value = "/getRecord", method = RequestMethod.GET)
    public RestResult<List<UsesLogDo>> getRecord(
            @RequestParam(value = "authCode", required = false) String authCode,
            @RequestParam(value = "orderId", required = false) String orderId,
            @RequestParam(value = "beginDate", required = false) Date beginDate,
            @RequestParam(value = "endDate", required = false) Date endDate
    ) {
        try {
            AccountInfoDo accountInfoDo = loginAccount.get();
            HashMap<String, Object> map = new HashMap();
            map.put("authCode", authCode);
            map.put("orderId", orderId);
            map.put("beginDate", beginDate);
            map.put("endDate", endDate);
            if (accountInfoDo.getAccountType()==DBConstantUnit.ACCOUNT_USER){
                map.put("authCode", accountInfoDo.getAuthCode());
                new RestResult<>(usesLogService.select(map));
            }


            if (accountInfoDo.getAccountType() == DBConstantUnit.ACCOUNT_ADMIN) {
                new RestResult<>(usesLogService.select(map));
            }

            HashMap<String, Object> empowerPar = new HashMap<>();
            empowerPar.put("authCode", accountInfoDo.getAuthCode());
            List<EmpowerDo> empowers = empowerService.select(empowerPar);
            if (empowers.size() > 0) {
                List<UsesLogDo> usesLogDos = usesLogService.select(map);
                List<String> empDevices = empowers.stream().map(d -> d.getDeviceId()).collect(Collectors.toList());
                List<UsesLogDo> collect = usesLogDos.stream().filter(d -> empDevices.contains(d.getDeviceId())).collect(Collectors.toList());
                new RestResult<>(collect);
            }
            return new RestResult();
        } catch (Exception e) {
            return new RestResult("err:" + e.getMessage(), "10001");
        }
    }


}
