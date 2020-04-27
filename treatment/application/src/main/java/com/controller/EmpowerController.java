package com.controller;

import com.common.DBConstantUnit;
import com.config.LoginAccount;
import com.config.SystemConstantUnit;
import com.model.AccountInfoDo;
import com.model.DeviceInfoDo;
import com.model.EmpowerDo;
import com.service.AccountInfoService;
import com.service.DeviceInfoService;
import com.service.EmpowerService;
import lib.RestResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @ClassName EmpowerController
 * @Description: TODO
 * @Author xizhonghuai
 * @Date 2020/4/24
 * @Version V1.0
 **/
@Slf4j
@CrossOrigin
@RestController
@RequestMapping("/api/empower/")
public class EmpowerController {

    @Autowired
    private EmpowerService empowerService;

    @Autowired
    private LoginAccount loginAccount;

    @Autowired
    private AccountInfoService accountInfoService;

    @Autowired
    private DeviceInfoService deviceInfoService;

    @RequestMapping(value = "/bind", method = RequestMethod.POST)
    public RestResult bind(@RequestParam(value = "deviceId") String deviceId,
                          @RequestParam(value = "authCode") String authCode) {
        try {

            AccountInfoDo accountInfoDo = loginAccount.get();
            if (accountInfoDo.getAccountType() != DBConstantUnit.ACCOUNT_ADMIN) {
                return new RestResult(SystemConstantUnit.PERMISSION, "10001");
            }

            EmpowerDo empowerDo = new EmpowerDo();
            empowerDo.setAuthCode(authCode);
            empowerDo.setDeviceId(deviceId);
            empowerService.insert(empowerDo);
            return new RestResult();
        } catch (Exception e) {
            return new RestResult("err:" + e.getMessage(), "10001");
        }
    }


    @RequestMapping(value = "/unbind", method = RequestMethod.POST)
    public RestResult unbind(@RequestParam(value = "deviceId") String deviceId,
                          @RequestParam(value = "account") String account) {
        try {

            AccountInfoDo accountInfoDo = loginAccount.get();
            if (accountInfoDo.getAccountType() != DBConstantUnit.ACCOUNT_ADMIN) {
                return new RestResult(SystemConstantUnit.PERMISSION, "10001");
            }
            HashMap map = new HashMap();
            map.put("account", account);
            List<AccountInfoDo> acs = accountInfoService.select(map);
            EmpowerDo empowerDo = new EmpowerDo();
            empowerDo.setAuthCode(acs.get(0).getAuthCode());
            empowerDo.setDeviceId(deviceId);
            empowerService.deleteByPrimary(map);
            return new RestResult();
        } catch (Exception e) {
            return new RestResult("err:" + e.getMessage(), "10001");
        }
    }


    @RequestMapping(value = "/getDevice", method = RequestMethod.GET)
    public RestResult<List<DeviceInfoDo>> getDevice(
                                                      @RequestParam(value = "deviceId",required = false) String deviceId,
                                                      @RequestParam(value = "authCode") String authCode

    ) {
        try {
            AccountInfoDo accountInfoDo = loginAccount.get();

            if (accountInfoDo.getAccountType() == DBConstantUnit.ACCOUNT_ADMIN){

                HashMap<String,Object> empowerPar = new HashMap<>();
                empowerPar.put("authCode",authCode);
                List<EmpowerDo> empowers = empowerService.select(empowerPar);
                if(empowers.size()>0){
                    HashMap<String,Object> map = new HashMap();
                    map.put("deviceId",deviceId);
                    List<DeviceInfoDo> deviceInfoDos = deviceInfoService.select(map);
                    List<String> empDevices = empowers.stream().map(d -> d.getDeviceId()).collect(Collectors.toList());
                    List<DeviceInfoDo> collect = deviceInfoDos.stream().filter(d -> empDevices.contains(d.getDeviceId())).collect(Collectors.toList());
                   return new RestResult<>(collect);
                }


            }


            return new RestResult(new ArrayList<>());
        } catch (Exception e) {
            return new RestResult("err:" + e.getMessage(), "10001");
        }
    }










}
