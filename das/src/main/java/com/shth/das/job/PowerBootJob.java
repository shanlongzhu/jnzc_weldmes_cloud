package com.shth.das.job;

import com.alibaba.druid.util.StringUtils;
import com.shth.das.business.JnOtcRtDataProtocol;
import com.shth.das.codeparam.TableStrategy;
import com.shth.das.common.CommonFunction;
import com.shth.das.common.CommonQueue;
import com.shth.das.common.CommonThreadPool;
import com.shth.das.pojo.db.OtcMachineQueue;
import com.shth.das.pojo.db.SxMachineQueue;
import com.shth.das.pojo.db.SxWeldModel;
import com.shth.das.pojo.db.WeldOnOffTime;
import com.shth.das.sys.rtdata.service.OtcRtDataService;
import com.shth.das.sys.rtdata.service.SxRtDataService;
import com.shth.das.sys.weldmesdb.service.MachineGatherService;
import com.shth.das.sys.weldmesdb.service.SxWeldService;
import com.shth.das.sys.weldmesdb.service.WeldOnOffTimeService;
import com.shth.das.util.DateTimeUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigInteger;

/**
 * 开机启动的任务
 */
@Component
@Slf4j
public class PowerBootJob {

    @Autowired
    OtcRtDataService otcRtDataService;
    @Autowired
    SxRtDataService sxRtDataService;
    @Autowired
    SxWeldService sxWeldService;
    @Autowired
    WeldOnOffTimeService weldOnOffTimeService;
    @Autowired
    MachineGatherService machineGatherService;

    /**
     * 启动所有任务
     */
//    @PostConstruct
    public void startAllJob() {
        //创建OTC实时数据表
        startJnOtcJob();
        //创建松下实时数据表
        startSxJob();
        //启动OTC阻塞队列消费者
        startOtcOnQueueConsumer();
        //启动OTC阻塞队列消费者
        startOtcOffQueueConsumer();
        //启动松下新增设备的阻塞队列消费者
        startSxAddMachineQueue();
        //启动松下开机设备的阻塞队列消费者
        startSxOnMachineQueue();
        //启动松下关机设备的阻塞队列消费者
        startSxOffMachineQueue();
    }

    /**
     * 任务：创建OTC实时表
     */
    public void startJnOtcJob() {
        //判断是否启用OTC业务功能
        if (CommonFunction.isEnableOtcFunction()) {
            CommonThreadPool.THREAD_POOL_EXECUTOR.execute(() -> {
                //当天数据库表名
                String tableName = TableStrategy.getOtcTableByDateTime(DateTimeUtils.getNowDateTime());
                if (!StringUtils.isEmpty(tableName)) {
                    int newTable = otcRtDataService.createNewTable(tableName);
                    log.info("系统启动创建当天OTC实时数据表:{} >>> {}", tableName, newTable);
                }
            });
        }
    }

    /**
     * 任务：创建松下实时表
     */
    public void startSxJob() {
        //判断是否启用松下业务功能
        if (CommonFunction.isEnableSxFunction()) {
            CommonThreadPool.THREAD_POOL_EXECUTOR.execute(() -> {
                //当天数据库表名
                String tableName = TableStrategy.getSxTableByDateTime(DateTimeUtils.getNowDateTime());
                if (!StringUtils.isEmpty(tableName)) {
                    int newTable = sxRtDataService.createNewTable(tableName);
                    log.info("系统启动创建当天SX实时数据表:{} >>> {}", tableName, newTable);
                }
            });
        }
    }

    /**
     * 启动OTC开机设备阻塞队列消费者
     */
    @SuppressWarnings("InfiniteLoopStatement")
    public void startOtcOnQueueConsumer() {
        //判断是否启用OTC业务功能
        if (CommonFunction.isEnableOtcFunction()) {
            CommonThreadPool.THREAD_POOL_EXECUTOR.execute(() -> {
                try {
                    while (true) {
                        //take()：当队列为空时进行阻塞对待，防止无限循环消耗CPU
                        OtcMachineQueue queue = CommonQueue.OTC_ON_MACHINE_QUEUES.take();
                        String gatherNo = queue.getGatherNo();
                        String weldIp = queue.getWeldIp();
                        String weldTime = queue.getWeldTime();
                        //新增设备开机时间
                        WeldOnOffTime onOffTime = new WeldOnOffTime();
                        onOffTime.setGatherNo(gatherNo);
                        onOffTime.setStartTime(weldTime);
                        onOffTime.setMachineId(JnOtcRtDataProtocol.getMachineIdByGatherNo(gatherNo));
                        onOffTime.setMachineType(0);
                        weldOnOffTimeService.insertWeldOnOffTime(onOffTime);
                        //修改OTC采集表的IP地址
                        machineGatherService.updateGatherIpByNumber(gatherNo, weldIp);
                    }
                } catch (InterruptedException e) {
                    log.error("启动OTC开机设备阻塞队列消费者异常：", e);
                }
            });
        }
    }

    /**
     * 启动OTC关机设备阻塞队列消费者
     */
    @SuppressWarnings("InfiniteLoopStatement")
    public void startOtcOffQueueConsumer() {
        //判断是否启用OTC业务功能
        if (CommonFunction.isEnableOtcFunction()) {
            CommonThreadPool.THREAD_POOL_EXECUTOR.execute(() -> {
                while (true) {
                    try {
                        //take()：当队列为空时进行阻塞对待，防止无限循环消耗CPU
                        OtcMachineQueue queue = CommonQueue.OTC_OFF_MACHINE_QUEUES.take();
                        String gatherNo = queue.getGatherNo();
                        //修改设备关机时间
                        WeldOnOffTime onOffTime = new WeldOnOffTime();
                        onOffTime.setGatherNo(gatherNo);
                        onOffTime.setEndTime(queue.getWeldTime());
                        onOffTime.setMachineId(JnOtcRtDataProtocol.getMachineIdByGatherNo(gatherNo));
                        onOffTime.setMachineType(0);
                        weldOnOffTimeService.updateWeldOnOffTime(onOffTime);
                    } catch (InterruptedException e) {
                        log.error("启动OTC关机设备阻塞队列消费者异常：", e);
                    }
                }
            });
        }
    }

    /**
     * 启动松下新增设备从队列存储的消费者
     */
    @SuppressWarnings("InfiniteLoopStatement")
    public void startSxAddMachineQueue() {
        //判断是否启用松下业务功能
        if (CommonFunction.isEnableSxFunction()) {
            CommonThreadPool.THREAD_POOL_EXECUTOR.execute(() -> {
                while (true) {
                    try {
                        //take()：如果队列为空，进行阻塞，直到有值，防止无限空轮询
                        SxWeldModel sxWeldModel = CommonQueue.SX_ADD_MACHINE_QUEUES.take();
                        //调用接口，数据存入数据库
                        sxWeldService.insertSxWeld(sxWeldModel);
                    } catch (Exception e) {
                        log.error("启动松下新增设备队列存储的消费者异常：", e);
                    }
                }
            });
        }
    }

    /**
     * 读取松下开机阻塞队列数据，存储数据库
     */
    @SuppressWarnings("InfiniteLoopStatement")
    public void startSxOnMachineQueue() {
        //判断是否启用松下业务功能
        if (CommonFunction.isEnableSxFunction()) {
            CommonThreadPool.THREAD_POOL_EXECUTOR.execute(() -> {
                while (true) {
                    try {
                        SxMachineQueue sxMachineQueue = CommonQueue.SX_ON_MACHINE_QUEUES.take();
                        //根据IP地址查询松下焊机信息
                        SxWeldModel sxWeldModel = sxWeldService.getSxWeldByWeldCid(sxMachineQueue.getWeldCid());
                        //新增设备关机时间
                        WeldOnOffTime onOffTime = new WeldOnOffTime();
                        onOffTime.setStartTime(sxMachineQueue.getWeldTime());
                        if (null != sxWeldModel && null != sxWeldModel.getId()) {
                            onOffTime.setMachineId(sxWeldModel.getId());
                        } else {
                            onOffTime.setMachineId(BigInteger.ZERO);
                        }
                        onOffTime.setMachineType(1);
                        onOffTime.setWeldCid(sxMachineQueue.getWeldCid());
                        //开机新增一条
                        weldOnOffTimeService.insertWeldOnOffTime(onOffTime);
                    } catch (Exception e) {
                        log.error("启动松下开机设备队列存储的消费者异常：", e);
                    }
                }
            });
        }
    }

    /**
     * 读取松下关机阻塞队列数据，存储数据库
     */
    @SuppressWarnings("InfiniteLoopStatement")
    public void startSxOffMachineQueue() {
        //判断是否启用松下业务功能
        if (CommonFunction.isEnableSxFunction()) {
            CommonThreadPool.THREAD_POOL_EXECUTOR.execute(() -> {
                while (true) {
                    try {
                        SxMachineQueue sxMachineQueue = CommonQueue.SX_OFF_MACHINE_QUEUES.take();
                        //根据IP地址查询松下焊机信息
                        SxWeldModel sxWeldModel = sxWeldService.getSxWeldByWeldCid(sxMachineQueue.getWeldCid());
                        //新增设备关机时间
                        WeldOnOffTime onOffTime = new WeldOnOffTime();
                        onOffTime.setEndTime(sxMachineQueue.getWeldTime());
                        if (null != sxWeldModel && null != sxWeldModel.getId()) {
                            onOffTime.setMachineId(sxWeldModel.getId());
                        } else {
                            onOffTime.setMachineId(BigInteger.ZERO);
                        }
                        onOffTime.setMachineType(1);
                        onOffTime.setWeldCid(sxMachineQueue.getWeldCid());
                        //关机修改最近一条
                        weldOnOffTimeService.updateWeldOnOffTime(onOffTime);
                    } catch (Exception e) {
                        log.error("启动松下关机设备队列存储的消费者异常：", e);
                    }
                }
            });
        }
    }

}
