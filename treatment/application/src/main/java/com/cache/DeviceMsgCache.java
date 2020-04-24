package com.cache;

import com.model.DeviceMsgDo;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @ClassName DeviceMsgCache
 * @Description: TODO
 * @Author xizhonghuai
 * @Date 2020/4/24
 * @Version V1.0
 **/
@Component
public class DeviceMsgCache {

    private static Map<String, DeviceMsgDo> cache = new ConcurrentHashMap<>();

    public void add(String deviceId,DeviceMsgDo deviceMsgDo){
        cache.put(deviceId,deviceMsgDo);
    }

    public  Map<String, DeviceMsgDo> getCache() {
        return cache;
    }
}
