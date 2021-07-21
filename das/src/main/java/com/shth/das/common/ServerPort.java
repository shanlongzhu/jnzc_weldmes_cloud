package com.shth.das.common;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class ServerPort {

    /**
     * OTC焊机端口
     */
    public static int otcPort;

    /**
     * 松下焊机端口
     */
    public static int sxPort;

    @Value("${nettyServer.port}")
    public void setOtcPort(int port) {
        otcPort = port;
    }

    @Value("${sxNettyServer.port}")
    public void setSxPort(int port) {
        sxPort = port;
    }

}
