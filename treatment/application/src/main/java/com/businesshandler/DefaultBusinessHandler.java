package com.businesshandler;


import com.transmission.business.BusinessHandler;
import com.transmission.server.core.IotSession;

/**
 * @ClassName DefaultBusinessHandler
 * @Description: TODO
 * @Author xizhonghuai
 * @Date 2020/4/18
 * @Version V1.0
 **/

public class DefaultBusinessHandler implements BusinessHandler {

    @Override
    public Boolean init() {
        return null;
    }

    @Override
    public void destroy() {

    }


    @Override
    public void messageReceived(IotSession iotSession, Object message) {

        System.out.println(message);
    }

    @Override
    public void sessionOpened(IotSession iotSession) {

    }

    @Override
    public void sessionClosed(IotSession iotSession) {

    }

    @Override
    public void sessionIdle(IotSession iotSession) {
        iotSession.close();
    }

    @Override
    public void forward(Object message) {



    }
}
