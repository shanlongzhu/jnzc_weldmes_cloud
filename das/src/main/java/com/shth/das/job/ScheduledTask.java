package com.shth.das.job;

import com.google.common.collect.Queues;
import com.processdb.driver.record.RecordData;
import com.shth.das.codeparam.TableNameEnum;
import com.shth.das.codeparam.TableStrategy;
import com.shth.das.common.CommonFunction;
import com.shth.das.common.CommonList;
import com.shth.das.common.CommonMap;
import com.shth.das.common.CommonQueue;
import com.shth.das.mqtt.EmqMqttClient;
import com.shth.das.pojo.jnotc.JNRtDataDB;
import com.shth.das.pojo.jnsx.SxRtDataDb;
import com.shth.das.processdb.DBCreateMethod;
import com.shth.das.sys.rtdata.service.OtcRtDataService;
import com.shth.das.sys.rtdata.service.SxRtDataService;
import com.shth.das.sys.weldmesdb.service.*;
import com.shth.das.util.CommonUtils;
import com.shth.das.util.DateTimeUtils;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Vector;

/**
 * 定时任务类
 */
@Component
@Slf4j
@EnableAsync
public class ScheduledTask {

    /**
     * 新增数据库记录最大数量
     */
    private static final int ADD_DATA_MAX_SIZE = 1000;

    @Autowired
    private OtcRtDataService otcRtDataService;
    @Autowired
    private MachineGatherService gatherService;
    @Autowired
    private MachineWeldService weldService;
    @Autowired
    private TaskService taskService;
    @Autowired
    private WelderService welderService;
    @Autowired
    private StatisticsDataService statisticsDataService;
    @Autowired
    private SxRtDataService sxRtDataService;

    //在线cron表达式生成器：https://cron.qqe2.com/

    /**
     * 每天晚上23点执行
     * 任务：创建第二天的实时数据表结构
     */
    @Scheduled(cron = TableStrategy.EXECUTE_TIME)
    @Async("taskExecutor")
    public void scheduled1() {
        String nowDateTime = DateTimeUtils.getNowDateTime();
        //判断是否启用OTC业务功能
        if (CommonFunction.isEnableOtcFunction()) {
            //根据时间获取OTC下一个表的表名
            String otcTableName = TableStrategy.getNextTableNameByDateTime(TableNameEnum.OTC, nowDateTime);
            if (StringUtils.isBlank(otcTableName)) {
                return;
            }
            int otcCreateTableResult = otcRtDataService.selectTableName(otcTableName);
            if (otcCreateTableResult != 1) {
                otcCreateTableResult = otcRtDataService.createNewTable(otcTableName);
            }
            log.info("otcCreateTableResult:--->>>> {}", otcCreateTableResult);
        }
    }

    /**
     * 每天晚上23点执行
     * 任务：创建第二天的实时数据表结构
     */
    @Scheduled(cron = TableStrategy.EXECUTE_TIME)
    @Async("taskExecutor")
    public void scheduled2() {
        String nowDateTime = DateTimeUtils.getNowDateTime();
        //判断是否启用松下业务功能
        if (CommonFunction.isEnableSxFunction()) {
            //根据时间获取松下下一个表的表名
            String sxTableName = TableStrategy.getNextTableNameByDateTime(TableNameEnum.SX, nowDateTime);
            if (StringUtils.isBlank(sxTableName)) {
                return;
            }
            int sxCreateTableResult = sxRtDataService.selectTableName(sxTableName);
            if (sxCreateTableResult != 1) {
                sxCreateTableResult = sxRtDataService.createNewTable(sxTableName);
            }
            log.info("sxCreateTableResult:--->>>> {}", sxCreateTableResult);
        }
    }

    /**
     * 每隔10分钟执行一次（单位：毫秒）
     * 任务：定时更新焊工、焊机、任务等数据
     */
    @Scheduled(fixedRate = 1000 * 60 * 10)
    @Async("taskExecutor")
    public void scheduled3() {
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
    @Scheduled(cron = "0 1 0/1 * * ?")
    @Async("taskExecutor")
    public void scheduled4() {
        //判断是否启用OTC业务功能
        if (CommonFunction.isEnableOtcFunction()) {
            try {
                //延迟1分钟
                //Thread.sleep(1000 * 60);
                String nowDateTime = DateTimeUtils.getNowDateTime();
                //获取当前时间对应的表名
                String otcTableName = TableStrategy.getTableNameByDateTime(TableNameEnum.OTC, nowDateTime);
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
                    String nowEndTime;
                    for (int i = 0; i < hours; i++) {
                        nowEndTime = DateTimeUtils.addDateHour(otcStartTime, 1);
                        statisticsDataService.insertWeldStatisticsData(otcStartTime, nowEndTime, otcTableName);
                        otcStartTime = nowEndTime;
                    }
                }
            } catch (Exception e) {
                log.error("每个整点执行一次OTC实时数据定时统计异常：", e);
            }
        }
    }

    /**
     * 每个整点执行一次
     * 任务：松下实时数据定时统计（逻辑同OTC的相似）
     */
    @Scheduled(cron = "0 1 0/1 * * ?")
    @Async("taskExecutor")
    public void scheduled5() {
        //判断是否启用松下业务功能
        if (CommonFunction.isEnableSxFunction()) {
            try {
                //延迟1分钟
                //Thread.sleep(1000 * 60);
                String nowDateTime = DateTimeUtils.getNowDateTime();
                //系统时间整点作为结束时间点
                String endTime = LocalDateTime.now().format(DateTimeUtils.HOUR_DATE);
                String sxTableName = TableStrategy.getTableNameByDateTime(TableNameEnum.SX, nowDateTime);
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
                    String nowEndTime;
                    for (int i = 0; i < sxHours; i++) {
                        nowEndTime = DateTimeUtils.addDateHour(sxStartTime, 1);
                        statisticsDataService.insertSxWeldStatisticsData(sxStartTime, nowEndTime, sxTableName);
                        sxStartTime = nowEndTime;
                    }
                }
            } catch (Exception e) {
                log.error("每个整点执行一次松下实时数据定时统计异常：", e);
            }
        }
    }

    /**
     * 每10分钟执行一次
     * 任务：时间校准
     */
    @Scheduled(fixedRate = 1000 * 60 * 10)
    @Async("taskExecutor")
    public void scheduled6() {
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
                if (CommonMap.OTC_GATHER_NO_CTX_MAP.isEmpty()) {
                    return;
                }
                StringBuilder sb = new StringBuilder();
                for (Map.Entry<String, ChannelHandlerContext> next : CommonMap.OTC_GATHER_NO_CTX_MAP.entrySet()) {
                    String key = next.getKey();
                    //采集编号
                    String gatherNo = CommonUtils.lengthJoint(key, 4);
                    //设备通道
                    Channel channel = CommonMap.OTC_GATHER_NO_CTX_MAP.get(key).channel();
                    if (channel.isOpen() && channel.isActive() && channel.isWritable()) {
                        sb.setLength(0); // 清空之前的字符串
                        sb.append(head).append(gatherNo).append("20").append(year).append(month)
                                .append(day).append(hour).append(minute).append(second).append(foot);
                        //字符总长度：36
                        String timeString = sb.toString().toUpperCase();
                        if (timeString.length() == 36) {
                            channel.writeAndFlush(timeString);
                        }
                    }
                }
            } catch (Exception e) {
                log.error("OTC设备时间定时校准异常：", e);
            }
        }
    }

    /**
     * 3秒执行一次
     * 任务：江南OTC设备实时数据定时存储
     */
    @Scheduled(fixedRate = 1000 * 3)
    @Async("taskExecutor")
    public void scheduled7() {
        if (CommonFunction.isEnableOtcFunction()) {
            try {
                int size = CommonQueue.OTC_LINKED_BLOCKING_QUEUE.size();
                if (size == 0) {
                    return;
                }
                StopWatch stopWatch = new StopWatch();
                for (int i = 0; i < size; i += ADD_DATA_MAX_SIZE) {
                    stopWatch.start();
                    List<JNRtDataDB> jnRtDataDbList = new LinkedList<>();
                    Queues.drain(CommonQueue.OTC_LINKED_BLOCKING_QUEUE, jnRtDataDbList, ADD_DATA_MAX_SIZE, Duration.ofMillis(0));
                    otcRtDataService.insertRtDataList(jnRtDataDbList);
                    stopWatch.stop();
                }
                log.info("3秒执行一次OTC设备实时数据存MySQL总耗时：{} ms,总数：{}", stopWatch.getTotalTimeMillis(), size);
            } catch (Exception e) {
                log.error("3秒执行一次OTC设备实时数据存MySQLDB异常：", e);
            }
        }
    }

    /**
     * 3秒执行一次
     * 任务：江南松下设备实时数据定时存储
     */
    @Scheduled(fixedRate = 1000 * 3)
    @Async("taskExecutor")
    public void scheduled8() {
        if (CommonFunction.isEnableSxFunction()) {
            try {
                int size = CommonQueue.SX_LINKED_BLOCKING_QUEUE.size();
                if (size == 0) {
                    return;
                }
                StopWatch stopWatch = new StopWatch();
                for (int i = 0; i < size; i += ADD_DATA_MAX_SIZE) {
                    stopWatch.start();
                    List<SxRtDataDb> sxRtDataList = new LinkedList<>();
                    Queues.drain(CommonQueue.SX_LINKED_BLOCKING_QUEUE, sxRtDataList, ADD_DATA_MAX_SIZE, Duration.ofMillis(0));
                    sxRtDataService.insertSxRtDataList(sxRtDataList);
                    stopWatch.stop();
                }
                log.info("3秒执行一次松下设备实时数据存MySQL总耗时：{} ms,总数：{}", stopWatch.getTotalTimeMillis(), size);
            } catch (Exception e) {
                log.error("3秒执行一次松下设备实时数据存MySQLDB异常：", e);
            }
        }
    }

    /**
     * 每天早晨7点执行
     * 任务：清除前一天任务绑定信息
     */
    @Scheduled(cron = "${clearTaskBindingByTime}")
    @Async("taskExecutor")
    public void scheduled9() {
        //清空OTC设备的任务
        CommonMap.OTC_TASK_CLAIM_MAP.clear();
        //清空松下设备的任务
        CommonMap.SX_TASK_CLAIM_MAP.clear();
    }

    /**
     * 3秒执行一次
     * 任务：读取OTC实时数据队列并存储到ProcessDB实时数据库中
     */
    @Scheduled(fixedRate = 1000 * 3)
    @Async("taskExecutor")
    public void scheduled10() {
        if (CommonFunction.isEnableOtcFunction() && CommonFunction.isEnableProcessDB()) {
            try {
                int size = CommonQueue.OTC_ADD_PROCESS_DB_QUEUE.size();
                if (size == 0) {
                    return;
                }
                for (int i = 0; i < size; i += ADD_DATA_MAX_SIZE) {
                    Vector<RecordData> vector = new Vector<>();
                    Queues.drain(CommonQueue.OTC_ADD_PROCESS_DB_QUEUE, vector, ADD_DATA_MAX_SIZE, Duration.ofMillis(0));
                    //添加OTC实时数据数据到ProcessDB
                    DBCreateMethod.addPointData(vector);
                }
            } catch (Exception e) {
                log.error("3秒执行一次OTC设备实时数据存ProcessDB异常：", e);
            }
        }
    }

    /**
     * 3秒执行一次
     * 任务：读取SX实时数据队列并存储到ProcessDB实时数据库中
     */
    @Async("taskExecutor")
    @Scheduled(fixedRate = 1000 * 3)
    public void scheduled11() {
        if (CommonFunction.isEnableSxFunction() && CommonFunction.isEnableProcessDB()) {
            try {
                int size = CommonQueue.SX_ADD_PROCESS_DB_QUEUE.size();
                if (size == 0) {
                    return;
                }
                for (int i = 0; i < size; i += ADD_DATA_MAX_SIZE) {
                    Vector<RecordData> vector = new Vector<>();
                    Queues.drain(CommonQueue.SX_ADD_PROCESS_DB_QUEUE, vector, ADD_DATA_MAX_SIZE, Duration.ofMillis(0));
                    //添加松下实时数据数据到ProcessDB
                    DBCreateMethod.addPointData(vector);
                }
            } catch (Exception e) {
                log.error("3秒执行一次松下设备实时数据存ProcessDB异常：", e);
            }
        }
    }

    /**
     * 每隔10分钟检测MQTT连接，如果断开则重连
     */
    @Async("taskExecutor")
    @Scheduled(fixedRate = 1000 * 60 * 10)
    public void scheduled12() {
        EmqMqttClient.reConnectMqtt();
    }

}
