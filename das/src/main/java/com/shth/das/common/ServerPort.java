package com.shth.das.common;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class ServerPort {

    /**
     * OTC焊机端口
     */
    public static int otcPort;

    @Value("${nettyServer.port}")
    public void setOtcPort(int port) {
        otcPort = port;
    }

}
