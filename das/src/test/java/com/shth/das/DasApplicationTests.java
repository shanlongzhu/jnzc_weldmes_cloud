package com.shth.das;

import com.shth.das.pojo.db.TaskModel;
import com.shth.das.sys.weldmesdb.mapper.TaskMapper;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Slf4j
public class DasApplicationTests extends BaseTest {

    @Autowired
    TaskMapper taskMapper;

    @Test
    void contextLoads() {
    }

    @Test
    public void test() {
        List<TaskModel> taskModels = taskMapper.selectList(null);
        System.out.println(taskModels.size());
        System.out.println(taskModels);
    }

}
