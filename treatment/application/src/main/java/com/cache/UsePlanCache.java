package com.cache;

import com.model.UsePlanDo;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class UsePlanCache {

    private static Map<String, UsePlanDo> cache = new ConcurrentHashMap<>();

    public void add(String deviceId,UsePlanDo usesLogDo){
        usesLogDo.setOrderId(UUID.randomUUID().toString());
        cache.put(deviceId,usesLogDo);
    }

    public  Map<String, UsePlanDo> getCache() {
        return cache;
    }

    public UsePlanDo get(String deviceId){
        return cache.get(deviceId);
    }

    public void update(String deviceId,UsePlanDo usesLogDo){
        cache.remove(deviceId);
        cache.put(deviceId,usesLogDo);
    }

    public UsePlanDo remove(String deviceId){
        return  cache.remove(deviceId);
    }





}
