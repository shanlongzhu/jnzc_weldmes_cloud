package com.shth.das.common;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * 数据初始化
 */
@Component
public class DataInitialization {

    /**
     * OTC焊机端口
     */
    public static int otcPort;

    /**
     * 松下焊机端口
     */
    public static int sxPort;

    /**
     * OTC一次存入数据库的条数
     */
    public static int otcNumber = 100;
    /**
     * 松下一次存入数据库的条数
     */
    public static int sxNumber = 100;


    @Value("${nettyServer.port}")
    public void setOtcPort(int port) {
        otcPort = port;
    }

    @Value("${sxNettyServer.port}")
    public void setSxPort(int port) {
        sxPort = port;
    }

    @Value("${dataInsertForNumber.otc}")
    public void setOtcNumber(int number) {
        if (number > 0) {
            otcNumber = number;
        }
    }

    @Value("${dataInsertForNumber.sx}")
    public void setSxNumber(int number) {
        if (number > 0) {
            sxNumber = number;
        }
    }

}
