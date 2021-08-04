package com.shth.das.job;

import com.shth.das.business.JnRtDataProtocol;
import com.shth.das.common.CommonDbData;
import com.shth.das.common.CommonQueue;
import com.shth.das.pojo.db.OtcMachineQueue;
import com.shth.das.pojo.db.SxWeldModel;
import com.shth.das.pojo.db.WeldOnOffTime;
import com.shth.das.sys.rtdata.service.RtDataService;
import com.shth.das.sys.rtdata.service.SxRtDataService;
import com.shth.das.sys.weldmesdb.service.MachineGatherService;
import com.shth.das.sys.weldmesdb.service.SxWeldService;
import com.shth.das.sys.weldmesdb.service.WeldOnOffTimeService;
import com.shth.das.util.DateTimeUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import java.math.BigInteger;

@Configuration
@Slf4j
public class RtDataJob {

    @Autowired
    RtDataService rtDataService;
    @Autowired
    SxRtDataService sxRtDataService;
    @Autowired
    SxWeldService sxWeldService;
    @Autowired
    WeldOnOffTimeService weldOnOffTimeService;
    @Autowired
    MachineGatherService machineGatherService;

    /**
     * 任务：创建OTC实时表
     */
    public void startJnOtcJob() {
        CommonDbData.THREAD_POOL_EXECUTOR.execute(() -> {
            //当天数据库表名
            String tableName = "rtdata" + DateTimeUtils.getNowDate(DateTimeUtils.CUSTOM_DATE);
            int newTable = rtDataService.createNewTable(tableName);
            log.info("系统启动创建当天OTC实时数据表:{} >>> {}", tableName, newTable);
        });
    }

    /**
     * 任务：创建松下实时表
     */
    public void startSxJob() {
        CommonDbData.THREAD_POOL_EXECUTOR.execute(() -> {
            //当天数据库表名
            String tableName = "sxrtd" + DateTimeUtils.getNowDate(DateTimeUtils.CUSTOM_DATE);
            int newTable = sxRtDataService.createNewTable(tableName);
            log.info("系统启动创建当天SX实时数据表:{} >>> {}", tableName, newTable);
        });
    }

    /**
     * 启动OTC开机设备阻塞队列消费者
     */
    @SuppressWarnings("InfiniteLoopStatement")
    public void startOtcOnQueueConsumer() {
        CommonDbData.THREAD_POOL_EXECUTOR.execute(() -> {
            try {
                while (true) {
                    //take()：当队列为空时进行阻塞对待，防止无限循环消耗CPU
                    OtcMachineQueue queue = CommonQueue.OTC_ON_MACHINE_QUEUES.take();
                    String gatherNo = queue.getGatherNo();
                    String weldIp = queue.getWeldIp();
                    //新增设备开机时间
                    WeldOnOffTime onOffTime = new WeldOnOffTime();
                    onOffTime.setGatherNo(gatherNo);
                    onOffTime.setStartTime(DateTimeUtils.getNowDateTime());
                    onOffTime.setMachineId(JnRtDataProtocol.getMachineIdByGatherNo(gatherNo));
                    onOffTime.setMachineType(0);
                    weldOnOffTimeService.insertWeldOnOffTime(onOffTime);
                    //修改OTC采集表的IP地址
                    machineGatherService.updateGatherIpByNumber(gatherNo, weldIp);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
    }

    /**
     * 启动OTC关机设备阻塞队列消费者
     */
    @SuppressWarnings("InfiniteLoopStatement")
    public void startOtcOffQueueConsumer() {
        CommonDbData.THREAD_POOL_EXECUTOR.execute(() -> {
            while (true) {
                try {
                    //take()：当队列为空时进行阻塞对待，防止无限循环消耗CPU
                    OtcMachineQueue queue = CommonQueue.OTC_OFF_MACHINE_QUEUES.take();
                    String gatherNo = queue.getGatherNo();
                    //log.info("OTC关机：" + "：{}", UpTopicEnum.rtcdata.name() + ":" + dataArray);
                    //新增设备关机时间
                    WeldOnOffTime onOffTime = new WeldOnOffTime();
                    onOffTime.setGatherNo(gatherNo);
                    onOffTime.setEndTime(DateTimeUtils.getNowDateTime());
                    onOffTime.setMachineId(JnRtDataProtocol.getMachineIdByGatherNo(gatherNo));
                    onOffTime.setMachineType(0);
                    weldOnOffTimeService.insertWeldOnOffTime(onOffTime);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * 启动松下新增设备从队列存储的消费者
     */
    @SuppressWarnings("InfiniteLoopStatement")
    public void startSxAddMachineQueue() {
        CommonDbData.THREAD_POOL_EXECUTOR.execute(() -> {
            while (true) {
                try {
                    SxWeldModel sxWeldModel = CommonQueue.SX_ADD_MACHINE_QUEUES.take();
                    //调用接口，数据存入数据库
                    sxWeldService.insertSxWeld(sxWeldModel);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * 读取松下开机阻塞队列数据，存储数据库
     */
    @SuppressWarnings("InfiniteLoopStatement")
    public void startSxOnMachineQueue() {
        CommonDbData.THREAD_POOL_EXECUTOR.execute(() -> {
            while (true) {
                try {
                    String weldIp = CommonQueue.SX_ON_MACHINE_QUEUES.take();
                    //根据IP地址查询松下焊机信息
                    SxWeldModel sxWeldModel = sxWeldService.getSxWeldByWeldIp(weldIp);
                    //新增设备关机时间
                    WeldOnOffTime onOffTime = new WeldOnOffTime();
                    onOffTime.setStartTime(DateTimeUtils.getNowDateTime());
                    if (null != sxWeldModel && null != sxWeldModel.getId()) {
                        onOffTime.setMachineId(sxWeldModel.getId());
                    } else {
                        onOffTime.setMachineId(BigInteger.ZERO);
                    }
                    onOffTime.setMachineType(1);
                    onOffTime.setWeldsxIp(weldIp);
                    weldOnOffTimeService.insertWeldOnOffTime(onOffTime);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * 读取松下关机阻塞队列数据，存储数据库
     */
    @SuppressWarnings("InfiniteLoopStatement")
    public void startSxOffMachineQueue() {
        CommonDbData.THREAD_POOL_EXECUTOR.execute(() -> {
            while (true) {
                try {
                    String weldIp = CommonQueue.SX_OFF_MACHINE_QUEUES.take();
                    //根据IP地址查询松下焊机信息
                    SxWeldModel sxWeldModel = sxWeldService.getSxWeldByWeldIp(weldIp);
                    //新增设备关机时间
                    WeldOnOffTime onOffTime = new WeldOnOffTime();
                    onOffTime.setEndTime(DateTimeUtils.getNowDateTime());
                    if (null != sxWeldModel && null != sxWeldModel.getId()) {
                        onOffTime.setMachineId(sxWeldModel.getId());
                    } else {
                        onOffTime.setMachineId(BigInteger.ZERO);
                    }
                    onOffTime.setMachineType(1);
                    onOffTime.setWeldsxIp(weldIp);
                    weldOnOffTimeService.insertWeldOnOffTime(onOffTime);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

}
