package com.controller;

import com.Dao;
import com.alibaba.fastjson.JSON;
import com.cache.UsePlanCache;
import com.common.DBConstantUnit;
import com.config.LoginAccount;
import com.config.SystemConstantUnit;
import com.model.AccountInfoDo;
import com.model.DeviceInfoDo;
import com.model.EmpowerDo;
import com.model.UsePlanDo;
import com.service.AccountInfoService;
import com.service.DeviceInfoService;
import com.service.EmpowerService;
import com.service.UsePlanService;
import com.transmission.server.core.AbstractBootServer;
import com.transmission.server.core.ServerUtils;
import com.transmission.server.core.WriteMsgUtils;
import lib.RestResult;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.session.SqlSessionManager;
import org.apache.mina.core.session.IoSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@CrossOrigin
@RestController
@RequestMapping("/api/debug/plan/")
public class TestController {



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
            @RequestParam(value = "duration") Integer duration,
            @RequestParam(value = "deviceId") String deviceId
    ) {

        String debugUser = "8888";//生产测试账号

        try {

            UsePlanDo usesLogDo = usePlanCache.get(deviceId);
            if (usesLogDo != null) {
                return new RestResult("设备使用中", "10000");
            }

            HashMap<String, Object> map = new HashMap<>();
            map.put("account", debugUser);
            List<AccountInfoDo> accountInfoDos = accountInfoService.select(map);
            if (accountInfoDos.size() <= 0 && accountInfoDos.get(0).getAccountType() != DBConstantUnit.ACCOUNT_USER) {

                return new RestResult<>("生产测试账号不存在！", "5555");
            }


            UsePlanDo use = new UsePlanDo();
            use.setDeviceId(deviceId);
            use.setDuration(duration);
            use.setAccount(debugUser);
            use.setAuthCode(accountInfoDos.get(0).getAuthCode());
            use.setOrderId(UUID.randomUUID().toString());
            usePlanCache.add(deviceId, use);
            return new RestResult();


        } catch (Exception e) {
            return new RestResult("err:" + e.getMessage(), "10001");
        }

    }


    @RequestMapping(value = "/get", method = RequestMethod.GET)
    public RestResult<List<UsePlanDo>> get(
            @RequestParam(value = "deviceId") String deviceId

    ) {

        try {


            List<UsePlanDo> usesLogDos = new ArrayList<>();
            if (usePlanCache.get(deviceId) != null) {
                usesLogDos.add(usePlanCache.get(deviceId));
            }
            return new RestResult<>(usesLogDos);


        } catch (Exception e) {
            return new RestResult<>("err:"+e.getMessage(), "4555");
        }

    }


    @RequestMapping(value = "/commit", method = RequestMethod.POST)
    public RestResult commit(
            @RequestParam(value = "deviceId") String deviceId
    ) {
        try {


            UsePlanDo plan = usePlanCache.get(deviceId);
            plan.setAfterState("测试");
            plan.setBeforeState("测试");
            //插入记录到DB
            usePlanService.insert(plan);
            usePlanCache.remove(deviceId);

            //下发停止指令
            HashMap<String, Object> cmd = new HashMap<>();
            cmd.put("id", deviceId);
            cmd.put("ctrl", "stop");
            cmd.put("date", new Date());
            AbstractBootServer server = (AbstractBootServer) ServerUtils.getServer("treatment");
            if (server != null) {
                IoSession ioSession = WriteMsgUtils.regIdToSession(server.getManagedSessions(), deviceId);
                if (ioSession != null) {
                    ioSession.write(JSON.toJSON(cmd));
                }
            }

            return new RestResult();

        } catch (Exception e) {
            return new RestResult<>(e.getMessage(), "655555");
        }

    }


}
