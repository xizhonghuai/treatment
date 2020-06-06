package com.controller;

import com.Dao;
import com.model.AccountInfoDo;
import com.model.DeviceInfoDo;
import com.service.AccountInfoService;
import com.service.DeviceInfoService;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.session.SqlSessionManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@Slf4j
@CrossOrigin
@RestController
@RequestMapping("/api/tc/")
public class TCController {

    @Autowired
    private DeviceInfoService deviceInfoService;
    @Autowired
    private AccountInfoService accountInfoService;

    @Autowired
    private Dao dao;

  public String insertDB() {

      AccountInfoDo accountInfoDo = new AccountInfoDo();
      accountInfoDo.setAccount("AAABC");
      accountInfoDo.setAccountType(0);
      accountInfoDo.setAge(1);
      accountInfoDo.setPassword("1111");
      accountInfoDo.setIsActivate(true);
      accountInfoDo.setTel("123" + UUID.randomUUID().toString());
      accountInfoDo.setAuthCode(UUID.randomUUID().toString());

      DeviceInfoDo deviceInfoDo = new DeviceInfoDo();
      deviceInfoDo.setDeviceId("5555");
      deviceInfoDo.setCityId(01);
      deviceInfoDo.setDeviceName("test");
      deviceInfoDo.setDtuId("110");

      try {
          ((SqlSessionManager)dao.getSqlSessionFactory()).startManagedSession();

          ((SqlSessionManager)dao.getSqlSessionFactory()).commit();
      } catch (Exception e) {
          e.printStackTrace();
          ((SqlSessionManager)dao.getSqlSessionFactory()).rollback();
      } finally {
      }


      return null;
  }
}
