package com.shth.das.job;

import com.alibaba.druid.util.StringUtils;
import com.google.common.collect.Queues;
import com.shth.das.codeparam.TableStrategy;
import com.shth.das.common.CommonFunction;
import com.shth.das.common.CommonList;
import com.shth.das.common.CommonMap;
import com.shth.das.common.CommonQueue;
import com.shth.das.pojo.jnotc.JNRtDataDB;
import com.shth.das.pojo.jnsx.SxRtDataDb;
import com.shth.das.sys.rtdata.service.OtcRtDataService;
import com.shth.das.sys.rtdata.service.SxRtDataService;
import com.shth.das.sys.weldmesdb.service.*;
import com.shth.das.util.CommonUtils;
import com.shth.das.util.DateTimeUtils;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * 定时任务类
 */
@Component
@Slf4j
@EnableAsync
public class ScheduledTask {

    @Autowired
    OtcRtDataService otcRtDataService;
    @Autowired
    MachineGatherService gatherService;
    @Autowired
    MachineWeldService weldService;
    @Autowired
    TaskService taskService;
    @Autowired
    WelderService welderService;
    @Autowired
    StatisticsDataService statisticsDataService;
    @Autowired
    SxRtDataService sxRtDataService;

    /**
     * 每天晚上23点执行
     * 任务：创建第二天的实时数据表结构
     */
    @Scheduled(cron = TableStrategy.EXECUTE_TIME)
    @Async
    public void scheduled1() {
        final String nowDateTime = DateTimeUtils.getNowDateTime();
        //判断是否启用OTC业务功能
        if (CommonFunction.isEnableOtcFunction()) {
            //根据时间获取OTC下一个表的表名
            String otcTableName = TableStrategy.getNextOtcTableByDateTime(nowDateTime);
            if (!StringUtils.isEmpty(otcTableName)) {
                int otcCreateResult = otcRtDataService.createNewTable(otcTableName);
                log.info("otcCreateResult:--->>>>{}", otcCreateResult);
            }
        }
        //判断是否启用松下业务功能
        if (CommonFunction.isEnableSxFunction()) {
            //根据时间获取松下下一个表的表名
            String sxTableName = TableStrategy.getNextSxTableByDateTime(nowDateTime);
            if (!StringUtils.isEmpty(sxTableName)) {
                int sxCreateResult = sxRtDataService.createNewTable(sxTableName);
                log.info("sxCreateResult:--->>>>{}", sxCreateResult);
            }
        }
    }

    /**
     * 每隔10分钟执行一次（单位：毫秒）
     * 任务：定时更新焊工、焊机、任务等数据
     */
    @Scheduled(fixedRate = 1000 * 60 * 10)
    @Async
    public void scheduled2() {
        CommonList.setGatherList(gatherService.getMachineGatherAll());
        CommonList.setWeldList(weldService.getMachineWeldAll());
        //CommonDbData.TASK_LIST = taskService.getTaskModelAll();
        //CommonDbData.WELDER_LIST = welderService.getWelderModelAll();
        //log.info("gatherList:--->>>>{}", CommonDbData.getGatherList().size());
        //log.info("weldList:--->>>>{}", CommonDbData.getWeldList().size());
    }

    /**
     * 每个整点执行一次
     * 任务：OTC实时数据定时统计
     */
    @Scheduled(cron = "0 0 0/1 * * ?")
    @Async
    public void scheduled3() {
        //判断是否启用OTC业务功能
        if (CommonFunction.isEnableOtcFunction()) {
            try {
                //延迟1分钟
                Thread.sleep(1000 * 60);
                final String nowDateTime = DateTimeUtils.getNowDateTime();
                //获取当前时间对应的表名
                String otcTableName = TableStrategy.getOtcTableByDateTime(nowDateTime);
                //系统时间整点作为结束时间点
                String endTime = LocalDateTime.now().format(DateTimeUtils.HOUR_DATE);
                //查询出上一次统计的时间
                String otcMaxEndTime = statisticsDataService.selectOtcMaxEndTime();
                //今天零点日期时间
                String otcStartTime = LocalDateTime.now().format(DateTimeUtils.TODAY_DATETIME);
                if (CommonUtils.isNotEmpty(otcMaxEndTime)) {
                    if (otcMaxEndTime.length() > 19) {
                        otcMaxEndTime = otcMaxEndTime.substring(0, 19);
                    }
                    String otcNowMaxEndTime = LocalDateTime.parse(otcMaxEndTime, DateTimeUtils.DEFAULT_DATETIME).format(DateTimeUtils.TODAY_DATE);
                    //判断上一次统计时间是不是今天，true：作为开始时间，false：默认从今天开始
                    if (otcNowMaxEndTime.equals(DateTimeUtils.getNowDate())) {
                        //上一次结束时间作为新的开始时间
                        otcStartTime = LocalDateTime.parse(otcMaxEndTime, DateTimeUtils.DEFAULT_DATETIME).format(DateTimeUtils.HOUR_DATE);
                    }
                }
                //相差了几个小时
                int hours = DateTimeUtils.differHours(otcStartTime, endTime);
                if (hours < 2) {
                    //不超过2个小时，则直接新增
                    statisticsDataService.insertWeldStatisticsData(otcStartTime, endTime, otcTableName);
                } else {
                    //循环每个小时统计
                    String nowEndTime = "";
                    for (int i = 0; i < hours; i++) {
                        nowEndTime = DateTimeUtils.addDateMinut(otcStartTime, 1);
                        statisticsDataService.insertWeldStatisticsData(otcStartTime, nowEndTime, otcTableName);
                        otcStartTime = nowEndTime;
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 每个整点执行一次
     * 任务：松下实时数据定时统计（逻辑同OTC的相似）
     */
    @Scheduled(cron = "0 0 0/1 * * ?")
    @Async
    public void scheduled4() {
        //判断是否启用松下业务功能
        if (CommonFunction.isEnableSxFunction()) {
            try {
                //延迟1分钟
                Thread.sleep(1000 * 60);
                final String nowDateTime = DateTimeUtils.getNowDateTime();
                //系统时间整点作为结束时间点
                String endTime = LocalDateTime.now().format(DateTimeUtils.HOUR_DATE);
                String sxTableName = TableStrategy.getSxTableByDateTime(nowDateTime);
                String sxMaxEndTime = statisticsDataService.selectSxMaxEndTime();
                String sxStartTime = LocalDateTime.now().format(DateTimeUtils.TODAY_DATETIME);
                if (CommonUtils.isNotEmpty(sxMaxEndTime)) {
                    if (sxMaxEndTime.length() > 19) {
                        sxMaxEndTime = sxMaxEndTime.substring(0, 19);
                    }
                    String sxNowMaxEndTime = LocalDateTime.parse(sxMaxEndTime, DateTimeUtils.DEFAULT_DATETIME).format(DateTimeUtils.TODAY_DATE);
                    //判断上一次统计时间是不是今天，true：作为开始时间，false：默认从今天开始
                    if (sxNowMaxEndTime.equals(DateTimeUtils.getNowDate())) {
                        //上一次结束时间作为新的开始时间
                        sxStartTime = LocalDateTime.parse(sxMaxEndTime, DateTimeUtils.DEFAULT_DATETIME).format(DateTimeUtils.HOUR_DATE);
                    }
                }
                //相差了几个小时
                int sxHours = DateTimeUtils.differHours(sxStartTime, endTime);
                if (sxHours < 2) {
                    //不超过2个小时，则直接新增
                    statisticsDataService.insertSxWeldStatisticsData(sxStartTime, endTime, sxTableName);
                } else {
                    //循环每个小时统计
                    String nowEndTime = "";
                    for (int i = 0; i < sxHours; i++) {
                        nowEndTime = DateTimeUtils.addDateMinut(sxStartTime, 1);
                        statisticsDataService.insertSxWeldStatisticsData(sxStartTime, nowEndTime, sxTableName);
                        sxStartTime = nowEndTime;
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 每10分钟执行一次
     * 任务：时间校准
     */
    @Scheduled(fixedRate = 1000 * 60 * 10)
    @Async
    public void scheduled5() {
        if (CommonFunction.isEnableOtcFunction()) {
            try {
                LocalDateTime localDateTime = LocalDateTime.now();
                String year = CommonUtils.lengthJoint(localDateTime.format(DateTimeFormatter.ofPattern("yy")), 2);
                String month = CommonUtils.lengthJoint(String.valueOf(localDateTime.getMonthValue()), 2);
                String day = CommonUtils.lengthJoint(String.valueOf(localDateTime.getDayOfMonth()), 2);
                String hour = CommonUtils.lengthJoint(String.valueOf(localDateTime.getHour()), 2);
                String minute = CommonUtils.lengthJoint(String.valueOf(localDateTime.getMinute()), 2);
                String second = CommonUtils.lengthJoint(String.valueOf(localDateTime.getSecond()), 2);
                String head = "007E1001010145";
                String foot = "007D";
                if (!CommonMap.OTC_GATHER_NO_CTX_MAP.isEmpty()) {
                    for (Map.Entry<String, ChannelHandlerContext> entry : CommonMap.OTC_GATHER_NO_CTX_MAP.entrySet()) {
                        //采集编号
                        String gatherNo = CommonUtils.lengthJoint(entry.getKey(), 4);
                        //设备通道
                        Channel channel = entry.getValue().channel();
                        if (channel.isOpen() && channel.isActive() && channel.isWritable()) {
                            //字符总长度：36
                            String timeString = (head + gatherNo + "20" + year + month + day + hour + minute + second + foot).toUpperCase();
                            if (timeString.length() == 36) {
                                channel.writeAndFlush(timeString).sync();
                            }
                        }
                    }
                }
            } catch (Exception e) {
                log.error("时间校准异常：{}", e.getMessage());
                e.printStackTrace();
            }
        }
    }

    /**
     * 3秒执行一次
     * 任务：江南OTC设备实时数据定时存储
     */
    @Scheduled(fixedRate = 1000 * 3)
    @Async
    public void scheduled6() {
        if (CommonFunction.isEnableOtcFunction()) {
            LinkedBlockingQueue<JNRtDataDB> otcLinkedBlockingQueue = CommonQueue.OTC_LINKED_BLOCKING_QUEUE;
            try {
                while (!otcLinkedBlockingQueue.isEmpty()) {
                    List<JNRtDataDB> jnRtDataDbList = new ArrayList<>();
                    Queues.drain(otcLinkedBlockingQueue, jnRtDataDbList, 2000, Duration.ofMillis(0));
                    otcRtDataService.insertRtDataList(jnRtDataDbList);
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                otcLinkedBlockingQueue.clear();
            }
        }
    }

    /**
     * 3秒执行一次
     * 任务：江南松下设备实时数据定时存储
     */
    @Scheduled(fixedRate = 1000 * 3)
    @Async
    public void scheduled7() {
        if (CommonFunction.isEnableSxFunction()) {
            LinkedBlockingQueue<SxRtDataDb> sxLinkedBlockingQueue = CommonQueue.SX_LINKED_BLOCKING_QUEUE;
            try {
                while (!sxLinkedBlockingQueue.isEmpty()) {
                    ArrayList<SxRtDataDb> sxRtDataList = new ArrayList<>();
                    Queues.drain(sxLinkedBlockingQueue, sxRtDataList, 2000, Duration.ofMillis(0));
                    sxRtDataService.insertSxRtDataList(sxRtDataList);
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                sxLinkedBlockingQueue.clear();
            }
        }
    }

    /**
     * 每天早晨7点执行
     * 任务：清除前一天任务绑定信息
     */
    @Scheduled(cron = "${clearTaskBindingByTime}")
    @Async
    public void scheduled8() {
        //清空OTC设备的任务
        CommonMap.OTC_TASK_CLAIM_MAP.clear();
        //清空松下设备的任务
        CommonMap.SX_TASK_CLAIM_MAP.clear();
    }

}
