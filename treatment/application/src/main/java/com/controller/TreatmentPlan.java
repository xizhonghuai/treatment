package com.controller;

import com.cache.UsePlanCache;
import com.common.DBConstantUnit;
import com.config.LoginAccount;
import com.config.SystemConstantUnit;
import com.model.AccountInfoDo;
import com.model.EmpowerDo;
import com.model.UsesLogDo;
import com.service.EmpowerService;
import lib.RestResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@CrossOrigin
@RestController
@RequestMapping("/api/plan/")
public class TreatmentPlan {

    @Autowired
    private LoginAccount loginAccount;

    @Autowired
    private UsePlanCache usePlanCache;

    @Autowired
    private EmpowerService empowerService;


    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public RestResult add(
            @RequestParam(value = "authCode") String authCode,
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
                use.setAuthCode(authCode);
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
                    use.setAuthCode(authCode);
                    usePlanCache.add(deviceId, use);
                    return new RestResult();
                }
            }
            return new RestResult<>(SystemConstantUnit.PERMISSION, "10001");
        } catch (Exception e) {
            return new RestResult("err:" + e.getMessage(), "10001");
        }

    }


}
