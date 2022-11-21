package com.shth.das;

import com.shth.das.pojo.db.TaskModel;
import com.shth.das.pojo.jnotc.JNRtDataDB;
import com.shth.das.sys.weldmesdb.mapper.TaskMapper;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Slf4j
public class DasApplicationTests extends BaseTest {

    @Autowired
    TaskMapper taskMapper;

    @Autowired
    MongoTemplate mongoTemplate;

    @Test
    public void test() {
        List<TaskModel> taskModels = taskMapper.selectList(null);
        System.out.println(taskModels.size());
        System.out.println("-------------------------");
        for (TaskModel taskModel : taskModels) {
            System.out.println(taskModel);
        }
        log.info("-------------------------------");
    }

    @Test
    public void testInsertByMongoDB() {
        List<JNRtDataDB> list = new ArrayList<>();
        JNRtDataDB jnRtDataDB = new JNRtDataDB();
        jnRtDataDB.setGatherNo("0001");
        jnRtDataDB.setWelderNo("张三");
        jnRtDataDB.setElectricity(BigDecimal.valueOf(10.22));
        JNRtDataDB jnRtDataDB2 = new JNRtDataDB();
        jnRtDataDB2.setGatherNo("0002");
        jnRtDataDB2.setWeldStatus(0);
        jnRtDataDB2.setWelderNo("李四");
        jnRtDataDB2.setMachineNo("0002");
        list.add(jnRtDataDB);
        list.add(jnRtDataDB2);
        Collection<JNRtDataDB> insert = mongoTemplate.insert(list, JNRtDataDB.class);
        int size = insert.size();
        System.out.println("---size---" + size);
    }

    @Test
    public void testSelectByMongoDB() {
        Query query = new Query();
        List<JNRtDataDB> otc_weld_rtdata = mongoTemplate.find(query, JNRtDataDB.class);
        System.out.println("---size---" + otc_weld_rtdata.size());
    }

}
