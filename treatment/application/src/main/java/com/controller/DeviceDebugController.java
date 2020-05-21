package com.controller;

import com.alibaba.fastjson.JSON;
import com.debug.DeviceDebug;
import com.transmission.server.core.AbstractBootServer;
import com.transmission.server.core.ServerUtils;
import com.transmission.server.core.WriteMsgUtils;
import lib.RestResult;
import lombok.extern.slf4j.Slf4j;
import org.apache.mina.core.session.IoSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

@Slf4j
@CrossOrigin
@RestController
@RequestMapping("/api/debug/")
public class DeviceDebugController {

    @Autowired
    private DeviceDebug deviceDebug;


    @RequestMapping(value = "/broadcast/sendCmd", method = RequestMethod.POST)
    public RestResult sendCmd(@RequestParam("serviceId") String serviceId, @RequestParam("cmd") String cmd) {
        AbstractBootServer server = (AbstractBootServer) ServerUtils.getServer(serviceId);
        if (server == null){
            return   new RestResult<>("服务未启动","556");
        }
        WriteMsgUtils.sendMsg(server.getManagedSessions(),cmd);
        return new RestResult();
    }



    @RequestMapping(value = "/sendCmd", method = RequestMethod.POST)
    public RestResult sendCmd(@RequestParam("serviceId") String serviceId, @RequestParam("regId") String regId, @RequestParam("cmd") String cmd) {
        AbstractBootServer server = (AbstractBootServer) ServerUtils.getServer(serviceId);
        if (server == null){
          return   new RestResult<>("服务未启动","556");
        }
        IoSession ioSession = WriteMsgUtils.regIdToSession(server.getManagedSessions(), regId);
        if (ioSession == null){
            return new RestResult("设备已离线","1233");
        }
        ioSession.write(cmd);
        return new RestResult();
    }

    @RequestMapping(value = "/disconnect", method = RequestMethod.POST)
    public RestResult disconnect(@RequestParam("serviceId") String serviceId, @RequestParam("regId") String regId) {
        deviceDebug.disconnect(serviceId, regId);
        return new RestResult();
    }


    @RequestMapping(value = "/lbs", method = RequestMethod.POST)
    public RestResult lbs(@RequestParam("serviceId") String serviceId, @RequestParam("regId") String regId) {
        deviceDebug.disconnect(serviceId, regId);
        HashMap<String, Object> map = new HashMap<>();
        map.put("id",regId);
        map.put("ctrl","lbs");
        map.put("date",new Date());
        deviceDebug.sendCmd(serviceId,regId, JSON.toJSONString(map));
        return new RestResult();
    }




    @RequestMapping(value = "/getOnline", method = RequestMethod.GET)
    public RestResult<List> getOnline(@RequestParam(value = "serviceId",required = false) String serviceId) {
        if (serviceId != null){
            return new RestResult(deviceDebug.getOnlineDeviceList(serviceId));
        }
        return new RestResult(deviceDebug.getOnlineDeviceList());
    }



}
