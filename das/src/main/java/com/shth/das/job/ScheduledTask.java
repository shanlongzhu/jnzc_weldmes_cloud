package com.shth.das.job;

import com.shth.das.common.CommonDbData;
import com.shth.das.netty.NettyServerHandler;
import com.shth.das.sys.rtdata.service.RtDataService;
import com.shth.das.sys.weldmesdb.service.*;
import com.shth.das.util.CommonUtils;
import com.shth.das.util.DateTimeUtils;
import io.netty.channel.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.Set;

/**
 * 定时任务类
 */
@Component
@EnableScheduling
@Slf4j
public class ScheduledTask {

    @Autowired
    RtDataService rtDataService;
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

    /**
     * 每天0点执行
     * 任务：创建实时数据表结构
     */
    @Scheduled(cron = "0 0 0 * * ?")
    @Async
    public void scheduled1() {
        //当天数据库表名
        String otcTableName = "rtdata" + DateTimeUtils.getNowDate(DateTimeUtils.CUSTOM_DATE);
        int otcCreateResult = rtDataService.createNewTable(otcTableName);
        log.info("otcCreateResult:--->>>>{}", otcCreateResult);
    }

    /**
     * 每隔10分钟执行一次（单位：毫秒）
     * 任务：定时更新焊工、焊机、任务等数据
     */
    @Scheduled(fixedRate = 1000 * 60 * 10)
    @Async
    public void scheduled2() {
        CommonDbData.GATHER_LIST = gatherService.getMachineGatherAll();
        CommonDbData.WELD_LIST = weldService.getMachineWeldAll();
        CommonDbData.TASK_LIST = taskService.getTaskModelAll();
        CommonDbData.WELDER_LIST = welderService.getWelderModelAll();
        log.info("gatherList:--->>>>{}", CommonDbData.GATHER_LIST.size());
        log.info("weldList:--->>>>{}", CommonDbData.WELD_LIST.size());
        log.info("taskList:--->>>>{}", CommonDbData.TASK_LIST.size());
        log.info("welderList:--->>>>{}", CommonDbData.WELDER_LIST.size());
    }

    /**
     * 每个整点执行一次
     * 任务：实时数据定时统计
     */
    @Scheduled(cron = "0 0 0/1 * * ?")
    @Async
    public void scheduled3() throws ParseException {
        //获取今天的表名
        String otcTableName = "rtdata" + DateTimeUtils.getNowDate(DateTimeUtils.CUSTOM_DATE);
        //系统时间整点作为结束时间点
        String endTime = LocalDateTime.now().format(DateTimeUtils.HOUR_DATE);
        //查询出上一次统计的时间
        String otcMaxEndTime = statisticsDataService.selectOtcMaxEndTime();
        //今天零点日期时间
        String otcStartTime = LocalDateTime.now().format(DateTimeUtils.TODAY_DATETIME);
        if (CommonUtils.isNotEmpty(otcMaxEndTime)) {
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
    }

    /**
     * 时间矫正
     * 每10分钟执行一次
     */
    @Scheduled(fixedRate = 1000 * 60 * 10)
    @Async
    public void scheduled4() {
        try {
            LocalDateTime localDateTime = LocalDateTime.now();
            String year = CommonUtils.lengthJoint(localDateTime.format(DateTimeFormatter.ofPattern("yy")), 2);
            String month = CommonUtils.lengthJoint(String.valueOf(localDateTime.getMonthValue()), 2);
            String day = CommonUtils.lengthJoint(String.valueOf(localDateTime.getDayOfMonth()), 2);
            String hour = CommonUtils.lengthJoint(String.valueOf(localDateTime.getHour()), 2);
            String minute = CommonUtils.lengthJoint(String.valueOf(localDateTime.getMinute()), 2);
            String second = CommonUtils.lengthJoint(String.valueOf(localDateTime.getSecond()), 2);
            String head = "007E0F01010145";
            String foot = "007D";
            if (NettyServerHandler.CLIENT_IP_GATHER_NO_MAP.size() > 0 && NettyServerHandler.MAP.size() > 0) {
                synchronized (NettyServerHandler.CLIENT_IP_GATHER_NO_MAP) {
                    Set<Map.Entry<String, String>> entries = NettyServerHandler.CLIENT_IP_GATHER_NO_MAP.entrySet();
                    //循环查找value（采集编号）的key（IP地址）
                    for (Map.Entry<String, String> entry : entries) {
                        //采集盒IP地址
                        String clientIp = entry.getKey();
                        //采集编号
                        String gatherNo = entry.getValue();
                        gatherNo = CommonUtils.lengthJoint(gatherNo, 4);
                        if (CommonUtils.isNotEmpty(clientIp) && CommonUtils.isNotEmpty(gatherNo)) {
                            if (NettyServerHandler.MAP.containsKey(clientIp)) {
                                Channel channel = NettyServerHandler.MAP.get(clientIp).channel();
                                if (channel.isOpen() && channel.isActive() && channel.isWritable()) {
                                    String timeString = head + gatherNo + "20" + year + month + day + hour + minute + second + foot;
                                    channel.writeAndFlush(timeString).sync();
                                    log.info("时间校准：{}", year + month + day + hour + minute + second);
                                }
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            log.error("时间校准异常：{}", e.getMessage());
        }
    }

}
