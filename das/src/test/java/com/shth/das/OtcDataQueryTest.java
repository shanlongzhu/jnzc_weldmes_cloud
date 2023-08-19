package com.shth.das;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.shth.das.pojo.jnotc.JNRtDataDB;
import com.shth.das.sys.rtdata.service.OtcRtDataService;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Slf4j
public class OtcDataQueryTest extends BaseTest {

    @Autowired
    private OtcRtDataService otcRtDataService;

    @Test
    public void testPage() {
        QueryWrapper<JNRtDataDB> queryWrapper = new QueryWrapper<>();
        Page<JNRtDataDB> page = new Page<>(1, 10);
        Page<JNRtDataDB> dataDBPage = otcRtDataService.page(page, queryWrapper);
        long total = dataDBPage.getTotal();
        List<JNRtDataDB> records = dataDBPage.getRecords();

        log.info("total:{}", total);
        log.info("records:{}", records.size());

    }

    @Test
    public void testList() {
        QueryWrapper<JNRtDataDB> queryWrapper = new QueryWrapper<>();
        List<JNRtDataDB> list = otcRtDataService.list(queryWrapper);

        log.info("list:{}", list);
        log.info("list-size:{}", list.size());

    }

}
