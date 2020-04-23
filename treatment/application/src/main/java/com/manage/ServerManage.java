package com.manage;

import com.hander.InterceptHandler;
import com.hander.businesshandler.DefaultBusinessHandler;
import com.toolutils.ConstantUtils;
import com.transmission.server.TcpServer;
import com.transmission.server.core.AbstractBootServer;
import com.transmission.server.core.BootServerParameter;
import com.transmission.server.core.ServerUtils;
import org.springframework.stereotype.Component;

import java.util.concurrent.ConcurrentHashMap;

/**
 * @ClassName ServerManageController
 * @Description: TODO
 * @Author xizhonghuai
 * @Date 2020/4/15
 * @Version V1.0
 **/
@Component
public class ServerManage {


    public synchronized void createService(BootServerParameter bootServerParameter) throws Exception {

        /*if (!StringUtils.isEmpty(bootServerParameter.getHandlerClassName()) && !StringUtils.isEmpty(bootServerParameter.getHandlerJarFile())) {
            Class<?> clazz;
            try {
                clazz = ToolUtils.getClass(new File(bootServerParameter.getHandlerJarFile()), bootServerParameter.getHandlerClassName());
                InterceptHandler yxHandler = new InterceptHandler((BusinessHandler) clazz.newInstance());
                yxHandler.setServiceId(bootServerParameter.getServiceId());
                bootServerParameter.setHandler(yxHandler);
            } catch (Exception e) {
                new Exception("服务初始化错误" + e.getMessage());
            }
        } else {
            InterceptHandler yxHandler = new InterceptHandler( new DefaultBusinessHandler());
            yxHandler.setServiceId(bootServerParameter.getServiceId());
            bootServerParameter.setHandler(yxHandler);
        }*/


        InterceptHandler interceptHandler = new InterceptHandler( new DefaultBusinessHandler());
        interceptHandler.setServiceId(bootServerParameter.getServiceId());
        interceptHandler.setConnectType(bootServerParameter.getServerType());
        bootServerParameter.setHandler(interceptHandler);

        AbstractBootServer cloudIotServer = null;
        String serverType = bootServerParameter.getServerType();
        if (ConstantUtils.TCP.equals(serverType)) {
            cloudIotServer = new TcpServer(bootServerParameter);
        } else if (ConstantUtils.UDP.equals(serverType)) {
            throw new Exception("Service type error or not supported");
        } else if (ConstantUtils.SERIAL.equals(serverType)) {
            throw new Exception("Service type error or not supported");
        } else {
            throw new Exception("Service type error or not supported");
        }
        if (cloudIotServer != null) {
            if (null == ServerUtils.add(bootServerParameter.getServiceId(),cloudIotServer)) {
                 cloudIotServer.init();

                // todo 服务信息入库



                /*cloudIotServer.start();*/
            } else  throw new Exception("Service already exists");
        }
    }


    public synchronized boolean startServer(String serviceId) {
        AbstractBootServer cloudIotServer = getCloudIotServer(serviceId);
        if (cloudIotServer != null) {
            if (cloudIotServer.getStatus()) {
                return true;
            }
            return cloudIotServer.start();
        }
        return false;
    }



    public void stopServer(String serviceId) {
        AbstractBootServer cloudIotServer = getCloudIotServer(serviceId);
        if (cloudIotServer != null){
            if (cloudIotServer.getStatus()) {
                cloudIotServer.stop();
            }
        }
    }

    public boolean restServer(String serviceId) {
        AbstractBootServer cloudIotServer = getCloudIotServer(serviceId);
        return cloudIotServer.rest();
    }

    public void delServer(String serviceId) {
        stopServer(serviceId);
        ServerUtils.remove(serviceId);
        {
            // todo del db
        }

    }


    public AbstractBootServer getCloudIotServer(String serviceId) {
        return (AbstractBootServer) ServerUtils.getServer(serviceId);
    }

    public ConcurrentHashMap<String,AbstractBootServer> getCloudIotServers() {
        return (ConcurrentHashMap<String, AbstractBootServer>) ServerUtils.getServers();
    }

}
