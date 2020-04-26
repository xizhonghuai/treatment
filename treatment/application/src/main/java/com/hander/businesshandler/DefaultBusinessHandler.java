package com.hander.businesshandler;


import com.config.SpringUtil;
import com.alibaba.fastjson.JSON;
import com.cache.UsePlanCache;
import com.entity.DeviceMessage;
import com.model.UsesLogDo;
import com.service.UsesLogService;
import com.transmission.business.BusinessHandler;
import com.transmission.server.core.IotSession;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.List;

/**
 * @ClassName DefaultBusinessHandler
 * @Description: TODO
 * @Author xizhonghuai
 * @Date 2020/4/18
 * @Version V1.0
 **/
@Slf4j
public class DefaultBusinessHandler implements BusinessHandler {

    @Override
    public Boolean init() {
        return true;
    }

    @Override
    public void destroy() {

    }


    @Override
    public void messageReceived(IotSession iotSession, Object message) {
        log.info("rec : {}", message.toString());
        try {
            DeviceMessage deviceMsg = JSON.parseObject(message.toString(), DeviceMessage.class);
            if (iotSession.getDeviceId() == null || !deviceMsg.getId().equals(iotSession.getDeviceId())) {
                iotSession.setDeviceId(deviceMsg.getId());
            }

            String deviceId = iotSession.getDeviceId();

            HashMap<String, Object> body = deviceMsg.getBody();
            if (body != null) {

                //返回数据
                if (body.get("np") !=null){




                }












                //请求uid oid min
                List<String> pulls = (List<String>) body.get("pull");
                if (pulls != null && pulls.contains("uid")) {

                    DeviceMessage ack = new DeviceMessage(deviceId);
                    HashMap<String, Object> bodyMap = new HashMap<>();
                    UsePlanCache usePlanCache = (UsePlanCache) SpringUtil.getBean("usePlanCache");
                    UsesLogDo usesLogDo = usePlanCache.get(deviceId);
                    if (usesLogDo != null) {
                        bodyMap.put("oid", usesLogDo.getOrderId());
                        bodyMap.put("uid", usesLogDo.getAuthCode());
                        bodyMap.put("min", usesLogDo.getDuration());
                    }
                    ack.setBody(bodyMap);
                    //返回uid、oid、min
                    iotSession.sendMsg(JSON.toJSONString(ack));
                }


                //返回状态
                String state = (String) body.get("state");
                if (state != null) {
                    if ("done".equals(state)) {
                        //删除缓存
                        UsePlanCache usePlanCache = (UsePlanCache) SpringUtil.getBean("usePlanCache");
                        UsesLogDo us = usePlanCache.remove(deviceId);
                        //插入记录到DB
                        UsesLogService usesLogService = (UsesLogService) SpringUtil.getBean("usesLogService");
                        usesLogService.insert(us);
                    }

                }
            } else {


            }




        } catch (Exception e) {
            log.info(e.toString());
            e.printStackTrace();

        }
        //回复心跳
        iotSession.sendMsg(JSON.toJSONString(new DeviceMessage(iotSession.getDeviceId())));
    }

    @Override
    public void sessionOpened(IotSession iotSession) {

    }

    @Override
    public void sessionClosed(IotSession iotSession) {
        iotSession.close();
    }

    @Override
    public void sessionIdle(IotSession iotSession) {
        iotSession.close();
    }


}
