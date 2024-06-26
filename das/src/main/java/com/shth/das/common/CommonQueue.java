package com.shth.das.common;

import com.processdb.driver.record.RecordData;
import com.shth.das.pojo.db.OtcMachineQueue;
import com.shth.das.pojo.db.SxMachineQueue;
import com.shth.das.pojo.db.SxWeldModel;
import com.shth.das.pojo.jnotc.JNRtDataDB;
import com.shth.das.pojo.jnsx.SxRtDataDb;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;

/**
 * @description: 公共队列
 * @author: Shan Long
 * @create: 2021-08-04
 */
public class CommonQueue {

    private static final int DATA_MAX_SIZE = 20000;

    /**
     * OTC阻塞队列存储实时数据，并定时同步ProcessDB
     */
    public static final BlockingQueue<RecordData> OTC_ADD_PROCESS_DB_QUEUE = new LinkedBlockingDeque<>(DATA_MAX_SIZE);

    /**
     * 松下阻塞队列存储实时数据，并定时同步ProcessDB
     */
    public static final BlockingQueue<RecordData> SX_ADD_PROCESS_DB_QUEUE = new LinkedBlockingDeque<>(DATA_MAX_SIZE);

    /**
     * 阻塞队列存储实时数据，并定时同步到MySQL数据库
     */
    public static final BlockingQueue<JNRtDataDB> OTC_LINKED_BLOCKING_QUEUE = new LinkedBlockingDeque<>(DATA_MAX_SIZE);

    /**
     * 阻塞队列存储实时数据，并定时同步到MySQL数据库
     */
    public static final BlockingQueue<SxRtDataDb> SX_LINKED_BLOCKING_QUEUE = new LinkedBlockingDeque<>(DATA_MAX_SIZE);

    /**
     * OTC开机设备阻塞队列
     * <E>:OtcMachineQueue OTC设备存储实体类
     */
    public static final BlockingQueue<OtcMachineQueue> OTC_ON_MACHINE_QUEUES = new LinkedBlockingDeque<>(DATA_MAX_SIZE);

    /**
     * OTC关机设备阻塞队列
     * <E>:OtcMachineQueue OTC设备存储实体类
     */
    public static final BlockingQueue<OtcMachineQueue> OTC_OFF_MACHINE_QUEUES = new LinkedBlockingDeque<>(DATA_MAX_SIZE);

    /**
     * 松下设备新增到阻塞队列
     */
    public static final BlockingQueue<SxWeldModel> SX_ADD_MACHINE_QUEUES = new LinkedBlockingDeque<>(DATA_MAX_SIZE);

    /**
     * 松下开机设备阻塞队列
     * <E>:SxMachineQueue 松下设备存储实体类
     */
    public static final BlockingQueue<SxMachineQueue> SX_ON_MACHINE_QUEUES = new LinkedBlockingDeque<>(DATA_MAX_SIZE);

    /**
     * 松下关机设备阻塞队列
     * <E>:SxMachineQueue 松下设备存储实体类
     */
    public static final BlockingQueue<SxMachineQueue> SX_OFF_MACHINE_QUEUES = new LinkedBlockingDeque<>(DATA_MAX_SIZE);

}
