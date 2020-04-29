package com.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.common.DBConstantUnit;
import com.config.LoginAccount;
import com.config.SystemConstantUnit;
import com.model.AccountInfoDo;
import com.service.AccountInfoService;
import lib.RestResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collector;
import java.util.stream.Collectors;

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
    @Autowired
    private LoginAccount loginAccount;

    @RequestMapping(value = "/reg", method = RequestMethod.POST)
    public RestResult reg(@RequestBody AccountInfoDo accountInfoDo) {
        try {
            accountInfoDo.setAuthCode(UUID.randomUUID().toString());
            accountInfoService.insert(accountInfoDo);
            return new RestResult();
        } catch (Exception e) {
            return new RestResult("err:" + e.getMessage(), "10001");
        }
    }


    @RequestMapping(value = "/get", method = RequestMethod.GET)
    public RestResult<List<AccountInfoDo>> get(
            @RequestParam(value = "account", required = false) String account,
            @RequestParam(value = "tel", required = false) String tel,
            @RequestParam(value = "accountType", required = false) Integer accountType
    ) {
        try {

            if (loginAccount.get().getAccountType() != DBConstantUnit.ACCOUNT_ADMIN) {
                return new RestResult<>("无权限", "1234");
            }
            HashMap<String, Object> map = new HashMap<>();
            map.put("account", account);
            map.put("tel", tel);

            List<AccountInfoDo> select = accountInfoService.select(map);
            if (accountType != null) {
                List<AccountInfoDo> collect = select.stream().filter(d -> (d.getAccountType().intValue() == accountType.intValue())).collect(Collectors.toList());
                return new RestResult<>(collect);
            }

            return new RestResult(select);
        } catch (Exception e) {
            return new RestResult("err:" + e.getMessage(), "10001");
        }
    }


    @RequestMapping(value = "/system/get", method = RequestMethod.GET)
    public RestResult<List<AccountInfoDo>> getSystemAcc(
            @RequestParam(value = "account", required = false) String account,
            @RequestParam(value = "tel", required = false) String tel,
            @RequestParam(value = "accountType", required = false) Integer accountType
    ) {
        try {

            if (loginAccount.get().getAccountType() != DBConstantUnit.ACCOUNT_ADMIN) {
                return new RestResult<>("无权限", "1234");
            }
            HashMap<String, Object> map = new HashMap<>();
            map.put("account", account);
            map.put("tel", tel);

            List<AccountInfoDo> select = accountInfoService.select(map);

            List<AccountInfoDo> accs = select.stream().filter(d -> (
                    d.getAccountType() != DBConstantUnit.ACCOUNT_USER &&
                            d.getAccountType() != DBConstantUnit.ACCOUNT_AGENT
            )).collect(Collectors.toList());

            if (accountType != null) {
                List<AccountInfoDo> collect = accs.stream().filter(d -> (d.getAccountType().intValue() == accountType.intValue())).collect(Collectors.toList());
                return new RestResult<>(collect);
            }
            return new RestResult(accs);
        } catch (Exception e) {
            return new RestResult("err:" + e.getMessage(), "10001");
        }
    }


    @RequestMapping(value = "/activate/get", method = RequestMethod.GET)
    public RestResult<List<AccountInfoDo>> getActivate(
            @RequestParam(value = "account", required = false) String account,
            @RequestParam(value = "tel", required = false) String tel,
            @RequestParam(value = "accountType", required = false) Integer accountType
    ) {
        try {

            if (loginAccount.get().getAccountType() != DBConstantUnit.ACCOUNT_ADMIN) {
                return new RestResult<>("无权限", "1234");
            }


            HashMap<String, Object> map = new HashMap<>();
            map.put("isActivate", true);
            map.put("account", account);
            map.put("tel", tel);

            List<AccountInfoDo> select = accountInfoService.select(map);
            if (accountType != null) {
                List<AccountInfoDo> collect = select.stream().filter(d -> (d.getAccountType().intValue() == accountType.intValue())).collect(Collectors.toList());
                return new RestResult<>(collect);
            }

            return new RestResult(select);
        } catch (Exception e) {
            return new RestResult("err:" + e.getMessage(), "10001");
        }
    }


    @RequestMapping(value = "/edit", method = RequestMethod.POST)
    public RestResult edit(@RequestBody AccountInfoDo accountInfoDo) {
        try {
            if (accountInfoDo.getId() == null) {
                return new RestResult("is null ", "1111");
            }
            HashMap map = JSONObject.parseObject(JSON.toJSONString(accountInfoDo), HashMap.class);

            accountInfoService.updateByPrimary(map);
            return new RestResult();
        } catch (Exception e) {
            return new RestResult("err:" + e.getMessage(), "10001");
        }
    }

//    @RequestMapping(value = "/del", method = RequestMethod.POST)
//    public RestResult del(@RequestParam(value = "id") Integer id, @RequestParam(value = "account") String account) {
//        try {
//            if (loginAccount.get().getAccountType() == DBConstantUnit.ACCOUNT_ADMIN) {
//                accountInfoService.deleteByPrimary(id, account);
//                return new RestResult();
//            }
//            return new RestResult(SystemConstantUnit.PERMISSION ,"10001");
//        } catch (Exception e) {
//            return new RestResult("err:" + e.getMessage(), "10001");
//        }
//    }


    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public RestResult login(HttpServletRequest request, @RequestParam(value = "account") String account, @RequestParam(value = "password") String password) {
        try {
            HashMap<String, Object> map = new HashMap<>();
            map.put("account", account);
            List<AccountInfoDo> as = accountInfoService.select(map);
            if (as != null && as.size() > 0) {
                AccountInfoDo token = as.get(0);
                if (token.getPassword().equals(password)) {
                    request.getSession().setAttribute("token", token);
                   return new RestResult<>();
                } else {
                  return   new RestResult<>("密码错误", "1001");
                }
            }
            return new RestResult("用户未注册", "10001");
        } catch (Exception e) {
            return new RestResult("err:" + e.getMessage(), "10001");
        }
    }


}
