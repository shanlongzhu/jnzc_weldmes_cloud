package com.shth.das.job;

import com.shth.das.common.CommonDbData;
import com.shth.das.sys.rtdata.service.RtDataService;
import com.shth.das.sys.rtdata.service.SxRtDataService;
import com.shth.das.util.DateTimeUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

@Configuration
@Slf4j
public class RtDataJob {

    @Autowired
    RtDataService rtDataService;
    @Autowired
    SxRtDataService sxRtDataService;

    /**
     * 任务：创建OTC实时表
     */
    public void startJnOtcJob() {
        //CommonDbData.THREAD_POOL_EXECUTOR.execute(() -> {
            //当天数据库表名
            String tableName = "rtdata" + DateTimeUtils.getNowDate(DateTimeUtils.CUSTOM_DATE);
            int newTable = rtDataService.createNewTable(tableName);
            log.info("系统启动创建当天OTC实时数据表:{} >>> {}", tableName, newTable);
        //});
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

}
