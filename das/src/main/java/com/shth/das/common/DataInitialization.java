package com.shth.das.common;

import com.shth.das.util.CommonUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * 数据初始化
 */
@Component
public class DataInitialization {

    /**
     * OTC焊机IP
     */
    private static String otcIp = "127.0.0.1";
    /**
     * 松下焊机IP
     */
    private static String sxIp = "127.0.0.1";

    /**
     * OTC焊机端口
     */
    private static int otcPort;

    /**
     * 松下焊机端口
     */
    private static int sxPort;

    /**
     * OTC一次存入数据库的条数
     */
    private static int otcNumber = 100;
    /**
     * 松下一次存入数据库的条数
     */
    private static int sxNumber = 100;


    @Value("${otcNettyServer.port}")
    public void setOtcPort(int port) {
        otcPort = port;
    }

    @Value("${sxNettyServer.port}")
    public void setSxPort(int port) {
        sxPort = port;
    }

    @Value("${dataInsertForNumber.otc}")
    private void setOtcNumber(int number) {
        if (number > 0) {
            otcNumber = number;
        }
    }

    @Value("${dataInsertForNumber.sx}")
    private void setSxNumber(int number) {
        if (number > 0) {
            sxNumber = number;
        }
    }

    public static int getOtcPort() {
        return otcPort;
    }

    public static int getSxPort() {
        return sxPort;
    }

    public static int getOtcNumber() {
        return otcNumber;
    }

    public static int getSxNumber() {
        return sxNumber;
    }

    public static String getOtcIp() {
        return otcIp;
    }

    @Value("${otcNettyServer.ip}")
    private void setOtcIp(String ip) {
        if (CommonUtils.isNotEmpty(ip)) {
            otcIp = ip;
        }
    }

    public static String getSxIp() {
        return sxIp;
    }

    @Value("${sxNettyServer.ip}")
    private void setSxIp(String ip) {
        if (CommonUtils.isNotEmpty(ip)) {
            sxIp = sxIp;
        }
    }
}
