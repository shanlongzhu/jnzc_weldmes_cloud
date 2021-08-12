package com.shth.das.common;

import com.shth.das.pojo.db.OtcMachineQueue;
import com.shth.das.pojo.db.SxWeldModel;
import com.shth.das.pojo.jnotc.JNRtDataDB;
import com.shth.das.pojo.jnsx.SxRtDataDb;

import java.util.concurrent.LinkedBlockingQueue;

/**
 * @description: 公共队列
 * @author: Shan Long
 * @create: 2021-08-04
 */
public class CommonQueue {

    /**
     * 阻塞队列存储实时数据，并定时同步到MySQL数据库
     */
    public static final LinkedBlockingQueue<JNRtDataDB> OTC_LINKED_BLOCKING_QUEUE = new LinkedBlockingQueue<>(10000);
    /**
     * 阻塞队列存储实时数据，并定时同步到MySQL数据库
     */
    public static final LinkedBlockingQueue<SxRtDataDb> SX_LINKED_BLOCKING_QUEUE = new LinkedBlockingQueue<>(10000);

    /**
     * OTC开机设备阻塞队列
     */
    public static final LinkedBlockingQueue<OtcMachineQueue> OTC_ON_MACHINE_QUEUES = new LinkedBlockingQueue<>(10000);

    /**
     * OTC关机设备阻塞队列
     */
    public static final LinkedBlockingQueue<OtcMachineQueue> OTC_OFF_MACHINE_QUEUES = new LinkedBlockingQueue<>(10000);

    /**
     * 松下设备新增到阻塞队列
     */
    public static final LinkedBlockingQueue<SxWeldModel> SX_ADD_MACHINE_QUEUES = new LinkedBlockingQueue<>(10000);

    /**
     * 松下开机设备阻塞队列
     * <E>(String):设备CID
     */
    public static final LinkedBlockingQueue<String> SX_ON_MACHINE_QUEUES = new LinkedBlockingQueue<>(10000);

    /**
     * 松下关机设备阻塞队列
     * <E>(String):设备CID
     */
    public static final LinkedBlockingQueue<String> SX_OFF_MACHINE_QUEUES = new LinkedBlockingQueue<>(10000);

}
