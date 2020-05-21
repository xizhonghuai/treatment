package com.hander;

import com.alibaba.fastjson.JSON;
import com.msgpush.MessagePush;
import com.transmission.business.BusinessHandler;
import com.transmission.business.Handler;
import com.transmission.server.core.IotSession;
import com.debug.push.WebSocketPush;
import lombok.extern.slf4j.Slf4j;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;

/**
 * @ClassName InterceptHandler
 * @Description: TODO
 * @Author xizhonghuai
 * @Date 2020/4/14
 * @Version V1.0
 **/
@Slf4j
public class InterceptHandler extends Handler {

    public InterceptHandler(BusinessHandler businessHandler) {
        super(businessHandler);
    }


    private String serviceId;

    private String connectType;


    public void setServiceId(String serviceId) {
        this.serviceId = serviceId;
    }

    public void setConnectType(String type) {
        this.connectType = type;
    }

    @Override
    public void sessionOpened(IoSession session) {
        log.info(session.getRemoteAddress() + "连接");
        businessHandler.sessionOpened(new IotSession(session,serviceId,connectType));
    }

    @Override
    public void sessionClosed(IoSession session) {
        businessHandler.sessionClosed(new IotSession(session,serviceId,connectType));

    }

    @Override
    public void sessionIdle(IoSession session, IdleStatus status) {
        log.info(session.getRemoteAddress() + "空闲");
        businessHandler.sessionIdle(new IotSession(session,serviceId,connectType));
    }

    @Override
    public void exceptionCaught(IoSession session, Throwable cause) {
        log.error(cause.getMessage());
    }

    @Override
    public void messageReceived(IoSession session, Object message) {
        IotSession iotSession = new IotSession(session, serviceId, connectType);
        businessHandler.messageReceived(iotSession, message);
        MessagePush messagePush = new WebSocketPush();
        messagePush.push(message);
//        iotSession.updateActivityTime();//更新活动时间
    }
}
