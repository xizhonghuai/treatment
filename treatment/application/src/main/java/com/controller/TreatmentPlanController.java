package com.controller;

import com.cache.UsePlanCache;
import com.common.DBConstantUnit;
import com.config.LoginAccount;
import com.config.SystemConstantUnit;
import com.model.AccountInfoDo;
import com.model.EmpowerDo;
import com.model.UsesLogDo;
import com.service.EmpowerService;
import com.service.UsesLogService;
import lib.RestResult;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.ibatis.reflection.ArrayUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@CrossOrigin
@RestController
@RequestMapping("/api/plan/")
public class TreatmentPlanController {

    @Autowired
    private LoginAccount loginAccount;

    @Autowired
    private UsePlanCache usePlanCache;

    @Autowired
    private EmpowerService empowerService;

    @Autowired
    UsesLogService usesLogService;


    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public RestResult add(
            @RequestParam(value = "account") String account,
            @RequestParam(value = "duration") Integer duration,
            @RequestParam(value = "deviceId") String deviceId
    ) {
        try {
            AccountInfoDo accountInfoDo = loginAccount.get();

            if (accountInfoDo.getAccountType() == DBConstantUnit.ACCOUNT_USER) {

                new RestResult<>(SystemConstantUnit.PERMISSION, "10001");
            }

            UsesLogDo usesLogDo = usePlanCache.get(deviceId);
            if (usesLogDo != null) {
                return new RestResult("设备使用中", "10000");
            }

            if (accountInfoDo.getAccountType() == DBConstantUnit.ACCOUNT_ADMIN) {
                UsesLogDo use = new UsesLogDo();
                use.setDeviceId(deviceId);
                use.setDuration(duration);
                use.setAccount(account);
                use.setOrderId(UUID.randomUUID().toString());
                usePlanCache.add(deviceId, use);
                return new RestResult();
            }

            HashMap<String, Object> empowerPar = new HashMap<>();
            empowerPar.put("authCode", accountInfoDo.getAuthCode());
            List<EmpowerDo> empowers = empowerService.select(empowerPar);
            if (empowers.size() > 0) {
                List<String> empDevices = empowers.stream().map(d -> d.getDeviceId()).collect(Collectors.toList());
                if (empDevices.contains(deviceId)) {
                    UsesLogDo use = new UsesLogDo();
                    use.setDeviceId(deviceId);
                    use.setDuration(duration);
                    use.setAccount(account);
                    use.setOrderId(UUID.randomUUID().toString());
                    usePlanCache.add(deviceId, use);
                    return new RestResult();
                }
            }
            return new RestResult<>(SystemConstantUnit.PERMISSION, "10001");
        } catch (Exception e) {
            return new RestResult("err:" + e.getMessage(), "10001");
        }

    }


    @RequestMapping(value = "/get", method = RequestMethod.GET)
    public RestResult<List<UsesLogDo>> get(
            @RequestParam(value = "deviceId",required = false) String deviceId
    ){
        if (deviceId != null ){
            List<UsesLogDo> usesLogDos = new ArrayList<>();
            if (usePlanCache.get(deviceId) != null){
                usesLogDos.add(usePlanCache.get(deviceId));
            }
            return new RestResult<>(usesLogDos);
        }
        return  new RestResult<>(new ArrayList<>(usePlanCache.getCache().values()));
    }


    @RequestMapping(value = "/commit", method = RequestMethod.POST)
    public RestResult commit(
            @RequestParam(value = "deviceId") String deviceId,
            @RequestParam(value = "beforeState") String beforeState,
            @RequestParam(value = "afterState") String afterState
    )
    {
        try {
            UsesLogDo plan = usePlanCache.get(deviceId);
            plan.setAfterState(afterState);
            plan.setBeforeState(beforeState);
            //插入记录到DB
            usesLogService.insert(plan);
            usePlanCache.remove(deviceId);
            return new RestResult();

        } catch (Exception e) {
           return new RestResult<>(e.getMessage(), "655555");
        }

    }

}
