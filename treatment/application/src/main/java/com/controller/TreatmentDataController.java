package com.controller;

import com.Dao;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.common.DBConstantUnit;
import com.config.LoginAccount;
import com.entity.TreatmentData;
import com.model.AccountInfoDo;
import com.model.DeviceMsgDo;
import com.model.EmpowerDo;
import com.model.UsePlanDo;
import com.service.AccountInfoService;
import com.service.DeviceMsgService;
import com.service.EmpowerService;
import com.service.UsePlanService;
import lib.RestResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.*;
import java.util.function.Consumer;
import java.util.stream.Collectors;

/**
 * @ClassName TreatmentDataController
 * @Description: TODO
 * @Author xizhonghuai
 * @Date 2020/4/27
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
    private UsePlanService usePlanService;

    @Autowired
    private AccountInfoService accountInfoService;


    @RequestMapping(value = "/getTreatmentData", method = RequestMethod.GET)
    public RestResult<TreatmentData> getTreatmentData(
            @RequestParam(value = "orderId") String orderId
    ) {

        try {

            List<DeviceMsgDo> deviceData = null;

            AccountInfoDo accountInfoDo = loginAccount.get();
            HashMap<String, Object> deviceMap = new HashMap();
            deviceMap.put("orderId", orderId);
            if (accountInfoDo.getAccountType() == DBConstantUnit.ACCOUNT_USER) {
                deviceMap.put("authCode", accountInfoDo.getAuthCode());
                deviceData = deviceMsgService.select(deviceMap);

            } else if (accountInfoDo.getAccountType() == DBConstantUnit.ACCOUNT_ADMIN) {

                deviceData = deviceMsgService.select(deviceMap);
            } else if (accountInfoDo.getAccountType() == DBConstantUnit.ACCOUNT_AGENT) {

                HashMap<String, Object> empowerPar = new HashMap<>();
                empowerPar.put("authCode", accountInfoDo.getAuthCode());
                List<EmpowerDo> empowers = empowerService.select(empowerPar);
                if (empowers.size() > 0) {
                    List<DeviceMsgDo> deviceMsgDos = deviceMsgService.select(deviceMap);
                    List<String> empDevices = empowers.stream().map(d -> d.getDeviceId()).collect(Collectors.toList());
                    deviceData = deviceMsgDos.stream().filter(d -> empDevices.contains(d.getDeviceId())).collect(Collectors.toList());
                }
            }

            if (deviceData != null) {
                TreatmentData treatmentData = new TreatmentData();
                treatmentData.setDeviceId(deviceData.get(0).getDeviceId());
                treatmentData.setOrderId(orderId);
                treatmentData.setAccountInfoDo(accountInfoDo);
                Collections.sort(deviceData, Comparator.comparingInt(DeviceMsgDo::getSerialId));
                deviceData.forEach(deviceMsgDo -> {

                    String msgBody = deviceMsgDo.getMsgBody();
                    HashMap<String, Object> msgMap = JSON.parseObject(msgBody, HashMap.class);

                    LinkedList<Float> np = JSON.parseObject( JSON.toJSONString(msgMap.get("np")), new TypeReference<LinkedList<Float>>() {
                    });
                    treatmentData.getNp().addAll(np);

                    LinkedList<Float> ec = JSON.parseObject(JSON.toJSONString(msgMap.get("ec")), new TypeReference<LinkedList<Float>>() {
                    });
                    treatmentData.getEc().addAll(ec);

                    LinkedList<Float> tp = JSON.parseObject(JSON.toJSONString(msgMap.get("tp")), new TypeReference<LinkedList<Float>>() {
                    });
                    treatmentData.getTp().addAll(tp);

                });

                treatmentData.setNp(getMapList(treatmentData.getNp()));
                treatmentData.setTp(getMapList(treatmentData.getTp()));
                treatmentData.setEc(getMapList(treatmentData.getEc()));

                return new RestResult<>(treatmentData);
            }

        } catch (Exception e) {
            return new RestResult("err:" + e.getMessage(), "10001");
        }

        return new RestResult<>();
    }


    @RequestMapping(value = "/getDetailsData", method = RequestMethod.GET)
    public RestResult<List<DeviceMsgDo>> getDetailsData(
            @RequestParam(value = "deviceId", required = false) String deviceId,
            @RequestParam(value = "account", required = false) String account,
            @RequestParam(value = "orderId", required = false) String orderId,
            @RequestParam(value = "beginDate", required = false) Date beginDate,
            @RequestParam(value = "endDate", required = false) Date endDate
    ) {
        try {
            AccountInfoDo accountInfoDo = loginAccount.get();
            HashMap<String, Object> map = new HashMap();
            map.put("deviceId", deviceId);
            map.put("orderId", orderId);
            map.put("beginDate", beginDate);
            map.put("endDate", endDate);
            if (accountInfoDo.getAccountType() == DBConstantUnit.ACCOUNT_USER) {
                map.put("authCode", accountInfoDo.getAuthCode());
                return new RestResult<>(deviceMsgService.select(map));
            }

            if (account != null) {

                HashMap<String, Object> accMap = new HashMap<>();
                accMap.put("account", account);
                List<AccountInfoDo> accs = accountInfoService.select(accMap);
                if (accs.size() <= 0) {
                    return new RestResult<>("不存在用户", "12332");
                }
                map.put("authCode", accs.get(0).getAuthCode());
            }


            if (accountInfoDo.getAccountType() == DBConstantUnit.ACCOUNT_ADMIN) {
                return new RestResult<>(deviceMsgService.select(map));
            }

            HashMap<String, Object> empowerPar = new HashMap<>();
            empowerPar.put("authCode", accountInfoDo.getAuthCode());
            List<EmpowerDo> empowers = empowerService.select(empowerPar);
            if (empowers.size() > 0) {
                List<DeviceMsgDo> deviceMsgDos = deviceMsgService.select(map);
                List<String> empDevices = empowers.stream().map(d -> d.getDeviceId()).collect(Collectors.toList());
                List<DeviceMsgDo> collect = deviceMsgDos.stream().filter(d -> empDevices.contains(d.getDeviceId())).collect(Collectors.toList());
                return new RestResult<>(collect);
            }
            return new RestResult(new ArrayList<>());
        } catch (Exception e) {
            return new RestResult("err:" + e.getMessage(), "10001");
        }
    }


    @RequestMapping(value = "/getRecord", method = RequestMethod.GET)
    public RestResult<List<UsePlanDo>> getRecord(
            @RequestParam(value = "deviceId", required = false) String deviceId,
            @RequestParam(value = "account", required = false) String account,
            @RequestParam(value = "orderId", required = false) String orderId,
            @RequestParam(value = "beginDate", required = false) Date beginDate,
            @RequestParam(value = "endDate", required = false) Date endDate
    ) {
        try {
            AccountInfoDo accountInfoDo = loginAccount.get();
            HashMap<String, Object> map = new HashMap();
            map.put("deviceId", deviceId);
            map.put("account", account);
            map.put("orderId", orderId);
            map.put("beginDate", beginDate);
            map.put("endDate", endDate);
            if (accountInfoDo.getAccountType() == DBConstantUnit.ACCOUNT_USER) {
                map.put("account", accountInfoDo.getAccount());
                return new RestResult<>(usePlanService.select(map));
            }


            if (accountInfoDo.getAccountType() == DBConstantUnit.ACCOUNT_ADMIN) {
                return new RestResult<>(usePlanService.select(map));
            }

            HashMap<String, Object> empowerPar = new HashMap<>();
            empowerPar.put("authCode", accountInfoDo.getAuthCode());
            List<EmpowerDo> empowers = empowerService.select(empowerPar);
            if (empowers.size() > 0) {
                List<UsePlanDo> usesLogDos = usePlanService.select(map);
                List<String> empDevices = empowers.stream().map(d -> d.getDeviceId()).collect(Collectors.toList());
                List<UsePlanDo> collect = usesLogDos.stream().filter(d -> empDevices.contains(d.getDeviceId())).collect(Collectors.toList());
                return new RestResult<>(collect);
            }
            return new RestResult();
        } catch (Exception e) {
            return new RestResult("err:" + e.getMessage(), "10001");
        }
    }



    private  List<Float> getMapList(List<Float> list){
        if (list.size() > 800){
            int group = list.size()/800;
            int index = 0;
            List<Float> result = new ArrayList<>();
            Double sum = 0d;
            for (int i = 0; i < list.size(); i++) {
                index++;
                sum += list.get(i);
                if (index == group || i == list.size() - 1) {
                    result.add(new BigDecimal(String.valueOf(sum / index)).setScale(1, BigDecimal.ROUND_DOWN).floatValue());
                    sum = 0d;
                    index = 0;
                }

            }
            return result;
        }

        return list;
    }


    public static void main(String[] args) {


        TreatmentDataController treatmentDataController = new TreatmentDataController();

        RestResult<TreatmentData> treatmentData = treatmentDataController.getTreatmentData("695e4199-0a77-4ef1-9f25-ab92129c134e");

        System.out.println(treatmentData.getData());


    }

}
