package com.shth.das.common;

import com.processdb.driver.record.RecordData;
import com.shth.das.pojo.db.OtcMachineQueue;
import com.shth.das.pojo.db.SxMachineQueue;
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
     * OTC阻塞队列存储实时数据，并定时同步ProcessDB
     */
    public static final LinkedBlockingQueue<RecordData> OTC_ADD_PROCESS_DB_QUEUE = new LinkedBlockingQueue<>(20000);

    /**
     * 松下阻塞队列存储实时数据，并定时同步ProcessDB
     */
    public static final LinkedBlockingQueue<RecordData> SX_ADD_PROCESS_DB_QUEUE = new LinkedBlockingQueue<>(20000);

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
     * <E>:OtcMachineQueue OTC设备存储实体类
     */
    public static final LinkedBlockingQueue<OtcMachineQueue> OTC_ON_MACHINE_QUEUES = new LinkedBlockingQueue<>(10000);

    /**
     * OTC关机设备阻塞队列
     * <E>:OtcMachineQueue OTC设备存储实体类
     */
    public static final LinkedBlockingQueue<OtcMachineQueue> OTC_OFF_MACHINE_QUEUES = new LinkedBlockingQueue<>(10000);

    /**
     * 松下设备新增到阻塞队列
     */
    public static final LinkedBlockingQueue<SxWeldModel> SX_ADD_MACHINE_QUEUES = new LinkedBlockingQueue<>(10000);

    /**
     * 松下开机设备阻塞队列
     * <E>:SxMachineQueue 松下设备存储实体类
     */
    public static final LinkedBlockingQueue<SxMachineQueue> SX_ON_MACHINE_QUEUES = new LinkedBlockingQueue<>(10000);

    /**
     * 松下关机设备阻塞队列
     * <E>:SxMachineQueue 松下设备存储实体类
     */
    public static final LinkedBlockingQueue<SxMachineQueue> SX_OFF_MACHINE_QUEUES = new LinkedBlockingQueue<>(10000);

}
