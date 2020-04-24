package com.cache;

import com.model.UsesLogDo;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class UsePlanCache {

    private static Map<String, UsesLogDo> cache = new ConcurrentHashMap<>();

    public void add(String deviceId,UsesLogDo usesLogDo){
        usesLogDo.setOrderId(UUID.randomUUID().toString());
        cache.put(deviceId,usesLogDo);
    }

    public  Map<String, UsesLogDo> getCache() {
        return cache;
    }

    public UsesLogDo get(String deviceId){
        return cache.get(deviceId);
    }

    public void update(String deviceId,UsesLogDo usesLogDo){
        cache.remove(deviceId);
        add(deviceId,usesLogDo);
    }

    public UsesLogDo remove(String deviceId){
        return  cache.remove(deviceId);
    }





}
