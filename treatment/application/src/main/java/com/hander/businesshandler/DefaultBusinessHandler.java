package com.hander.businesshandler;


import com.cache.DeviceMsgCache;
import com.config.SpringUtil;
import com.alibaba.fastjson.JSON;
import com.cache.UsePlanCache;
import com.entity.DeviceMessage;
import com.fasterxml.jackson.databind.deser.std.DateDeserializers;
import com.model.DeviceMsgDo;
import com.model.UsePlanDo;
import com.service.DeviceMsgService;
import com.transmission.business.BusinessHandler;
import com.transmission.server.core.AbstractBootServer;
import com.transmission.server.core.IotSession;
import com.transmission.server.core.ServerUtils;
import com.transmission.server.core.WriteMsgUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.mina.core.session.IoSession;

import java.util.Date;
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

            UsePlanCache usePlanCache = (UsePlanCache) SpringUtil.getBean("usePlanCache");
            DeviceMsgCache deviceMsgCache = (DeviceMsgCache) SpringUtil.getBean("deviceMsgCache");
            UsePlanDo usePlanDo = usePlanCache.get(deviceId);

            if (body != null && usePlanDo != null) {
                //接收数据
                if (body.get("np") != null) {
                    if (usePlanDo.getOrderId().equals((String) body.get("oid"))) {
                        DeviceMsgService deviceMsgService = (DeviceMsgService) SpringUtil.getBean("deviceMsgService");
                        DeviceMsgDo deviceMsgDo = new DeviceMsgDo();
                        deviceMsgDo.setDeviceId(deviceMsg.getId());
                        deviceMsgDo.setOrderId((String) body.get("oid"));
                        deviceMsgDo.setSerialId((Integer) body.get("sid"));
                        deviceMsgDo.setMsgBody(JSON.toJSONString(body));
                        deviceMsgDo.setAuthCode(usePlanDo.getAuthCode());


                        //更新状态
                        String state = (String) body.get("state");
                        if (state != null) {
                            usePlanDo.setState(state);
                            usePlanCache.update(deviceId, usePlanDo);
                        }


                        //更新使用时长
                        Integer min = (Integer) body.get("min");
                        if (min != null) {
                            usePlanDo.setRealDuration(min);
                            usePlanCache.update(deviceId, usePlanDo);
                        }


                        //数据入库
                        deviceMsgService.insert(deviceMsgDo);
                        //更新缓存
                        deviceMsgCache.update(deviceId, deviceMsgDo);
                    }
                }


                //请求uid oid min
                List<String> pulls = (List<String>) body.get("pull");
                if (pulls != null && pulls.contains("uid")) {
                    UsePlanDo usesLogDo = usePlanCache.get(deviceId);
                    HashMap<String, Object> ack = new HashMap<>();
                    ack.put("id", deviceId);
                    ack.put("date", new Date());
                    if (usesLogDo != null) {
                        ack.put("oid", usesLogDo.getOrderId());
                        ack.put("uid", usesLogDo.getAuthCode());
                        ack.put("min", usesLogDo.getDuration());
                    }
                    //返回uid、oid、min
                    iotSession.sendMsg(JSON.toJSONString(ack));
                    return;
                }


            }


            //回复心跳
            iotSession.sendMsg(JSON.toJSONString(new DeviceMessage(iotSession.getDeviceId())));

        } catch (Exception e) {
            log.info(e.toString());
            e.printStackTrace();

        }

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
