package com;

import com.common.DaoBeans;
import com.mapper.AccountInfoMapper;
import com.mapper.DeviceInfoMapper;
import com.mapper.DeviceMsgMapper;
import com.model.AccountInfoDo;
import com.model.DeviceInfoDo;
import com.model.DeviceMsgDo;
import org.apache.ibatis.session.SqlSession;

import java.io.IOException;
import java.sql.SQLException;
import java.util.UUID;

/**
 * @ClassName CodeTest
 * @Description: TODO
 * @Author xizhonghuai
 * @Date 2020/3/11
 * @Version V1.0
 **/
public class CodeTest {

    public static Dao dao = new Dao();

    public static void main(String[] args) throws IOException {
        dao.init();
        SqlSession sqlSession = dao.getSqlSessionFactory().openSession();

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
            sqlSession.getMapper(AccountInfoMapper.class).insert(accountInfoDo);
            sqlSession.getMapper(DeviceInfoMapper.class).insert(deviceInfoDo);

//            sqlSession.insert(AccountInfoMapper.class.getName() + ".insert", accountInfoDo);
//            sqlSession.insert(DeviceInfoMapper.class.getName() + ".insert", deviceInfoDo);


            sqlSession.commit();

        } catch (Exception e) {
            sqlSession.rollback();
            System.out.println("rollback");
            e.printStackTrace();


        } finally {
            sqlSession.close();
        }


    }


}
