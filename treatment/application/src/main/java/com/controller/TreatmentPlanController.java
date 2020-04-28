package com.controller;

import com.cache.UsePlanCache;
import com.common.DBConstantUnit;
import com.config.LoginAccount;
import com.config.SystemConstantUnit;
import com.model.AccountInfoDo;
import com.model.EmpowerDo;
import com.model.UsePlanDo;
import com.service.AccountInfoService;
import com.service.EmpowerService;
import com.service.UsePlanService;
import lib.RestResult;
import lombok.extern.slf4j.Slf4j;
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
    private UsePlanService usePlanService;

    @Autowired
    private AccountInfoService accountInfoService;



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

            UsePlanDo usesLogDo = usePlanCache.get(deviceId);
            if (usesLogDo != null) {
                return new RestResult("设备使用中", "10000");
            }

            HashMap<String,Object> map = new HashMap<>();
            map.put("account",account);
            List<AccountInfoDo> accountInfoDos = accountInfoService.select(map);
            if (accountInfoDos.size()<=0){
               return new RestResult<>("请先添加客户资料","5555");
            }

            if (accountInfoDo.getAccountType() == DBConstantUnit.ACCOUNT_ADMIN) {
                UsePlanDo use = new UsePlanDo();
                use.setDeviceId(deviceId);
                use.setDuration(duration);
                use.setAccount(account);
                use.setAuthCode(accountInfoDos.get(0).getAuthCode());
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
                    UsePlanDo use = new UsePlanDo();
                    use.setDeviceId(deviceId);
                    use.setDuration(duration);
                    use.setAccount(account);
                    use.setAuthCode(accountInfoDos.get(0).getAuthCode());
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
    public RestResult<List<UsePlanDo>> get(
            @RequestParam(value = "deviceId",required = false) String deviceId
    ){
        if (deviceId != null ){
            List<UsePlanDo> usesLogDos = new ArrayList<>();
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


            UsePlanDo plan = usePlanCache.get(deviceId);
            plan.setAfterState(afterState);
            plan.setBeforeState(beforeState);
            //插入记录到DB
            usePlanService.insert(plan);
            usePlanCache.remove(deviceId);
            return new RestResult();

        } catch (Exception e) {
           return new RestResult<>(e.getMessage(), "655555");
        }

    }

}
