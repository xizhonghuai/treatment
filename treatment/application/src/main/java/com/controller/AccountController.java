package com.controller;

import com.alibaba.fastjson.JSONObject;
import com.model.AccountInfoDo;
import com.service.AccountInfoService;
import lib.RestResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.UUID;

/**
 * @ClassName AccountController
 * @Description: TODO
 * @Author xizhonghuai
 * @Date 2020/4/23
 * @Version V1.0
 **/
@Slf4j
@CrossOrigin
@RestController
@RequestMapping("/api/account/")
public class AccountController {

    @Autowired
    private AccountInfoService accountInfoService;

    @RequestMapping(value = "/reg", method = RequestMethod.POST)
    public RestResult reg(@RequestBody AccountInfoDo accountInfoDo){
        try {
            accountInfoDo.setAuthCode(UUID.randomUUID().toString());
            accountInfoService.insert(accountInfoDo);
           return new RestResult();
        }catch (Exception e){
            return new RestResult("err:"+e.getMessage(),"10001");
        }
    }


    @RequestMapping(value = "/get", method = RequestMethod.GET)
    public RestResult<List<AccountInfoDo>> get(@RequestParam(value = "id",required = false) Integer id,
                                @RequestParam(value = "account",required = false) String account,
                                @RequestParam(value = "tel",required = false) String tel
                          ){
        try {
            HashMap<String,Object> map = new HashMap<>();
            if (id != null){
                map.put("id",id);
            }
            if (account != null){
                map.put("account",account);
            }
            if (tel != null){
                map.put("tel",tel);
            }
            return new RestResult(accountInfoService.select(map));
        }catch (Exception e){
            return new RestResult("err:"+e.getMessage(),"10001");
        }
    }



    @RequestMapping(value = "/edit", method = RequestMethod.POST)
    public RestResult edit(@RequestBody AccountInfoDo accountInfoDo){
        try {
            if (accountInfoDo.getId() == null){
                new Exception(" id is null");
            }

//            accountInfoService.

//            deviceService.updateByPrimary(JSONObject.parseObject(JSONObject.toJSONString(deviceDO),HashMap.class));

//            accountInfoService.insert(accountInfoDo);
            return new RestResult();
        }catch (Exception e){
            return new RestResult("err:"+e.getMessage(),"10001");
        }
    }














}
