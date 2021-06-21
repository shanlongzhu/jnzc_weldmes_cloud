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
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
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
        String tableName = "rtdata" + DateTimeUtils.getSdfDate.format(System.currentTimeMillis());
        int newTable = rtDataService.createNewTable(tableName);
        log.info("newTable:--->>>>{}", newTable);
    }

    /**
     * 每隔10分钟执行一次（单位：毫秒）
     * 任务：定时更新焊工、焊机、任务等数据
     */
    @Scheduled(fixedRate = 1000 * 60 * 10)
    @Async
    public void scheduled2() {
        CommonDbData.gatherList = gatherService.getMachineGatherAll();
        CommonDbData.weldList = weldService.getMachineWeldAll();
        CommonDbData.taskList = taskService.getTaskModelAll();
        CommonDbData.welderList = welderService.getWelderModelAll();
        log.info("gatherList:--->>>>{}", CommonDbData.gatherList.size());
        log.info("weldList:--->>>>{}", CommonDbData.weldList.size());
        log.info("taskList:--->>>>{}", CommonDbData.taskList.size());
        log.info("welderList:--->>>>{}", CommonDbData.welderList.size());
        //System.out.println("gatherList-->>"+CommonDbData.gatherList);
        //System.out.println("weldList-->>"+CommonDbData.weldList);
        //System.out.println("taskList-->>"+CommonDbData.taskList);
        //System.out.println("welderList-->>"+CommonDbData.welderList);
    }

    /**
     * 每个整点执行一次
     * 任务：实时数据定时统计
     */
    @Scheduled(cron = "0 0 0/1 * * ?")
    @Async
    public void scheduled3() throws ParseException {
        String tableName = "rtdata" + DateTimeUtils.getSdfDate.format(System.currentTimeMillis());//获取今天的表名
        String endTime = DateTimeUtils.getHourDate.format(System.currentTimeMillis()); //系统时间整点作为结束时间点
        String maxEndTime = statisticsDataService.selectMaxEndTime();//查询出上一次统计的时间
        String startTime = DateTimeUtils.getTodayTime.format(System.currentTimeMillis());//今天时间
        if (CommonUtils.isNotEmpty(maxEndTime)) {
            String nowMaxEndTime = DateTimeUtils.sdfDate.format(DateTimeUtils.parse("yyyy-MM-dd", maxEndTime));
            //判断上一次统计时间是不是今天，true：作为开始时间，false：默认从今天开始
            if (nowMaxEndTime.equals(DateTimeUtils.sdfDate.format(System.currentTimeMillis()))) {
                startTime = DateTimeUtils.getHourDate.format(DateTimeUtils.parse("yyyy-MM-dd HH:00:00", maxEndTime)); //作为新的开始时间
            }
        }
        int hours = DateTimeUtils.differHours(startTime, endTime);//相差了几个小时
        if (hours < 2) {
            //不超过2个小时，则直接新增
            statisticsDataService.insertWeldStatisticsData(startTime, endTime, tableName);
        } else {
            //循环每个小时统计
            String nowendTime = "";
            for (int i = 0; i < hours; i++) {
                nowendTime = DateTimeUtils.addDateMinut(startTime, 1);
                statisticsDataService.insertWeldStatisticsData(startTime, nowendTime, tableName);
                startTime = nowendTime;
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
            Calendar date = Calendar.getInstance();
            String year = String.valueOf(date.get(Calendar.YEAR));
            String month = CommonUtils.stringLengthJoint(String.valueOf(date.get(Calendar.MONTH) + 1), 2);
            String day = CommonUtils.stringLengthJoint(String.valueOf(date.get(Calendar.DAY_OF_MONTH)), 2);
            String hour = CommonUtils.stringLengthJoint(String.valueOf(date.get(Calendar.HOUR_OF_DAY)), 2);
            String minute = CommonUtils.stringLengthJoint(String.valueOf(date.get(Calendar.MINUTE)), 2);
            String second = CommonUtils.stringLengthJoint(String.valueOf(date.get(Calendar.SECOND)), 2);
            String head = "007E0F01010145";
            String foot = "007D";
            if (NettyServerHandler.gatherAndIpMap.size() > 0 && NettyServerHandler.MAP.size() > 0) {
                synchronized (NettyServerHandler.gatherAndIpMap) {
                    Set<Map.Entry<String, String>> entries = NettyServerHandler.gatherAndIpMap.entrySet();
                    //循环查找value（采集编号）的key（IP地址）
                    for (Map.Entry<String, String> entry : entries) {
                        String clientIp = entry.getKey(); //采集盒IP地址
                        String gatherNo = entry.getValue(); //采集编号
                        gatherNo = CommonUtils.lengthJoint(gatherNo, 4);
                        if (CommonUtils.isNotEmpty(clientIp) && CommonUtils.isNotEmpty(gatherNo)) {
                            if (NettyServerHandler.MAP.containsKey(clientIp)) {
                                Channel channel = NettyServerHandler.MAP.get(clientIp).channel();
                                if (channel.isOpen() && channel.isActive() && channel.isWritable()) {
                                    String timeString = head + gatherNo + year + month + day + hour + minute + second + foot;
                                    channel.writeAndFlush(timeString).sync();
                                    log.info("时间校准：{}", year + month + day + hour + minute + second);
                                    //007E0F01010145000320210606112118007D
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
