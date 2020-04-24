package com.controller;

import com.model.CityDo;
import com.service.CityService;
import lib.RestResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @ClassName CityController
 * @Description: TODO
 * @Author xizhonghuai
 * @Date 2020/4/24
 * @Version V1.0
 **/
@Slf4j
@CrossOrigin
@RestController
@RequestMapping("/api/city/")
public class CityController {

    @Autowired
    private CityService cityService;

    @RequestMapping(value = "/get", method = RequestMethod.GET)
    public RestResult<List<CityDo>> get(){
        try {
           return new RestResult<>(cityService.selectAll());
        } catch (Exception e) {
            return new RestResult("err:" + e.getMessage(), "10001");
        }
    }
}
