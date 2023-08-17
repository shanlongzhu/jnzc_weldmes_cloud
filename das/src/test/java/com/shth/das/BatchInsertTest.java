package com.shth.das;

import com.shth.das.pojo.jnotc.JNRtDataDB;
import com.shth.das.sys.rtdata.service.OtcRtDataService;
import com.shth.das.util.DateTimeUtils;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

@Slf4j
public class BatchInsertTest extends BaseTest {

    @Autowired
    private OtcRtDataService otcRtDataService;

    /**
     * 测试批量插入性能
     */
    @Test
    public void testBatchInsert() {
        /**
         * 插入10000条数据耗时：2432ms
         *
         * 插入1000条数据耗时：4945ms
         * 插入2000条数据耗时：3551ms
         * 插入3000条数据耗时：4900ms
         * 插入4000条数据耗时：3611ms
         * 插入5000条数据耗时：3253ms
         */
        List<JNRtDataDB> jnRtDataDbList = new LinkedList<>();

        int size = 10000;
        String nowDateTime = DateTimeUtils.getNowDateTime();

        long startTime_ss = System.currentTimeMillis();
        int conut = 0;

        for (int mm = 0; mm < size; mm += 5000) {

            List<JNRtDataDB> jnRtDataDbLists = new LinkedList<>();

            for (int i = 0; i < 5000; i++) {
                JNRtDataDB jnRtDataDB = new JNRtDataDB();

                jnRtDataDB.setWeldModel(128);
                jnRtDataDB.setGatherNo("1");
                jnRtDataDB.setWeldStatus(0);
                jnRtDataDB.setMachineNo("0001");
                jnRtDataDB.setCreateTime(nowDateTime);

                jnRtDataDbLists.add(jnRtDataDB);
                jnRtDataDbList.add(jnRtDataDB);
            }

            conut++;

            long startTime = System.currentTimeMillis();
            otcRtDataService.insertRtDataList(jnRtDataDbLists);
            long endTime = System.currentTimeMillis();
            long executionTime = endTime - startTime;
            log.info("conut :" + conut + "插入" + jnRtDataDbLists.size() + "条数据耗时：" + executionTime + "ms");
        }

        long endTime = System.currentTimeMillis();
        long executionTime = endTime - startTime_ss;
        log.info("========插入" + jnRtDataDbList.size() + "条数据耗时：" + executionTime + "ms");

    }

    @Test
    public void saveBatch() {
        /**
         * 10000
         *
         * 插入1000条数据耗时：2241ms
         * 插入4000条数据耗时：2216ms
         */

        Collection<JNRtDataDB> list = getList();

        long startTime = System.currentTimeMillis();
        otcRtDataService.saveBatch(list, 5000);
        long endTime = System.currentTimeMillis();
        long executionTime = endTime - startTime;
        log.info("插入" + list.size() + "条数据耗时：" + executionTime + "ms");

    }

    private Collection<JNRtDataDB> getList() {
        List<JNRtDataDB> jnRtDataDbList = new LinkedList<>();

        int size = 10000;
        String nowDateTime = DateTimeUtils.getNowDateTime();

        for (int i = 0; i < size; i++) {
            JNRtDataDB jnRtDataDB = new JNRtDataDB();

            jnRtDataDB.setWeldModel(128);
            jnRtDataDB.setGatherNo("1");
            jnRtDataDB.setWeldStatus(0);
            jnRtDataDB.setMachineNo("0001");
            jnRtDataDB.setCreateTime(nowDateTime);

            jnRtDataDbList.add(jnRtDataDB);
        }
        return jnRtDataDbList;
    }

}
