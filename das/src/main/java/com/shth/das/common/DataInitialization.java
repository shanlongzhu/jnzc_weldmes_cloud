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
     * OTC待机数据是否存储（默认：true 存储）
     */
    private static boolean otcStandbySave = true;

    /**
     * 松下待机数据是否存储（默认：true 存储）
     */
    private static boolean sxStandbySave = true;

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

    /**
     * 刷卡启用设备功能（默认：true开启）
     */
    private static boolean slotCardEnableDevice = true;

    @Value("${otcNettyServer.port}")
    private void setOtcPort(int port) {
        otcPort = port;
    }

    @Value("${sxNettyServer.port}")
    private void setSxPort(int port) {
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
            sxIp = ip;
        }
    }

    public static boolean isOtcStandbySave() {
        return otcStandbySave;
    }

    @Value("${otcStandbySave}")
    private void setOtcStandbySave(boolean otcStandbySave) {
        DataInitialization.otcStandbySave = otcStandbySave;
    }

    public static boolean isSxStandbySave() {
        return sxStandbySave;
    }

    @Value("${sxStandbySave}")
    private void setSxStandbySave(boolean sxStandbySave) {
        DataInitialization.sxStandbySave = sxStandbySave;
    }

    public static boolean isSlotCardEnableDevice() {
        return slotCardEnableDevice;
    }

    @Value("${slotCardEnableDevice}")
    private void setSlotCardEnableDevice(boolean slotCardEnableDevice) {
        DataInitialization.slotCardEnableDevice = slotCardEnableDevice;
    }
}
