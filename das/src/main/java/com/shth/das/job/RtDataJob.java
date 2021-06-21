package com.shth.das.job;

import com.shth.das.sys.rtdata.service.RtDataService;
import com.shth.das.util.DateTimeUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

@Configuration
@Slf4j
public class RtDataJob {

    @Autowired
    RtDataService rtDataService;

    public void start() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                //当天数据库表名
                String tableName = "rtdata" + DateTimeUtils.getSdfDate.format(System.currentTimeMillis());
                int newTable = rtDataService.createNewTable(tableName);
                log.info("系统启动创建当天实时数据表:{} >>> {}", tableName, newTable);
            }
        }).start();
    }

}
