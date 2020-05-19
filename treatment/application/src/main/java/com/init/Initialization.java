package com.init;

import com.Dao;
import com.commonmanger.ServerManage;
import com.toolutils.ConstantUtils;
import com.transmission.server.core.BootServerParameter;
import com.transmission.server.debug.DebugService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Arrays;

/**
 * @ClassName Initialization
 * @Description: TODO
 * @Author xizhonghuai
 * @Date 2020/4/15
 * @Version V1.0
 **/
@Slf4j
@Component
public class Initialization {


    @Value("${sysConf.webHost}")
    private String webHost;

    @Value("${sysConf.webUserHost}")
    private String webUserHost;

    @Value("${sysConf.defaultServerPort}")
    private Integer defaultServerPort;

    @Value("${sysConf.debugPort}")
    private Integer debugPort;

    public static String webUrl;
    public static String webUserUrl;


    @Autowired
    private Dao dao;

    @Autowired
    private ServerManage serverManage;




    @PostConstruct
    public void init(){
       // todo 加载数据

        try {
            //启动调试服务
            new DebugService(debugPort).start();
            //数据库初始化
            dao.init();

            //todo 启动服务
            BootServerParameter bootServerParameter = new BootServerParameter();
            bootServerParameter.setServerName("YX-Cloud-Iot");
            bootServerParameter.setIdle(300);
            bootServerParameter.setPort(Arrays.asList(defaultServerPort));
            bootServerParameter.setDebug(true);
            bootServerParameter.setServiceId("treatment");
            bootServerParameter.setServerType(ConstantUtils.TCP);
            try {
                serverManage.createService(bootServerParameter);
                serverManage.startServer("treatment");
            } catch (Exception e) {
                e.printStackTrace();
                log.error(e.toString());
            }

            webUrl = webHost;
            webUserUrl = webUserHost;

        }catch (Exception e){
            e.printStackTrace();
            log.error(e.toString());
        }
    }


}
