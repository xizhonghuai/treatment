package com.debug;

import com.transmission.server.core.AbstractBootServer;
import com.transmission.server.core.ConnectProperty;
import com.transmission.server.core.ServerUtils;
import com.transmission.server.core.WriteMsgUtils;
import org.apache.mina.core.session.IoSession;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @ClassName DeviceManage
 * @Description: TODO
 * @Author xizhonghuai
 * @Date 2020/4/24
 * @Version V1.0
 **/
@Component
public class DeviceDebug {


    public void sendCmd(String serviceId, Object cmd) {
        AbstractBootServer server = (AbstractBootServer) ServerUtils.getServer(serviceId);
        if (server != null) {
            WriteMsgUtils.sendMsg(server.getManagedSessions(), cmd);
        }
    }

    public void sendCmd(String serviceId, String redId, Object cmd) {
        AbstractBootServer server = (AbstractBootServer) ServerUtils.getServer(serviceId);
        if (server != null) {
            WriteMsgUtils.sendMsg(server.getManagedSessions(), cmd, redId);
        }
    }


    public void disconnect(String serviceId, String regId) {
        AbstractBootServer server = (AbstractBootServer) ServerUtils.getServer(serviceId);
        if (server != null) {
            IoSession ioSession = WriteMsgUtils.regIdToSession(server.getManagedSessions(), regId);
            if (ioSession != null) ioSession.close(true);
        }
    }


    public List<ConnectProperty> getOnlineDeviceList(String serviceId) {


        if (null != serviceId) {
            AbstractBootServer server = (AbstractBootServer) ServerUtils.getServer(serviceId);
            if (server != null) {
                List<ConnectProperty> connectPropertyList = new ArrayList<>();
                Map<Long, IoSession> managedSessions = server.getManagedSessions();
                managedSessions.forEach((aLong, ioSession) -> {

                    if (ioSession.getAttribute("regId") !=null){

                        ConnectProperty connectProperty = new ConnectProperty();
                        connectProperty.setRegId((String) ioSession.getAttribute("regId"));
                        connectProperty.setAddress(ioSession.getRemoteAddress().toString());
                        connectProperty.setRegisterTime(new Date(ioSession.getCreationTime()));
                        connectProperty.setActivityTime(new Date(ioSession.getLastReadTime()));
                        connectPropertyList.add(connectProperty);
                    }
                });
                return connectPropertyList;
            }
        }
        return null;
    }


    public List<ConnectProperty> getOnlineDeviceList() {

        Map<String, Object> servers = (ConcurrentHashMap<String, Object>) ServerUtils.getServers();
        List<ConnectProperty> connectPropertyList = new ArrayList<>();
        servers.forEach((id, o) -> {
            AbstractBootServer server = (AbstractBootServer) o;
            Map<Long, IoSession> managedSessions = server.getManagedSessions();
            managedSessions.forEach((aLong, ioSession) -> {
                ConnectProperty connectProperty = (ConnectProperty) ioSession.getAttribute("connectProperty");
                if (connectProperty != null) {

                    connectPropertyList.add(connectProperty);

                }
            });
        });
        return connectPropertyList;
    }


}
