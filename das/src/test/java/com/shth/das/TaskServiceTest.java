package com.shth.das;

import com.shth.das.pojo.db.TaskModel;
import com.shth.das.sys.weldmesdb.mapper.TaskMapper;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.List;

@Slf4j
public class TaskServiceTest extends BaseTest {

    @Autowired
    private TaskMapper taskMapper;

    @Autowired
    private TransactionTemplate transactionTemplate;

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
    public void testTransactionTemplate() {
        transactionTemplate.executeWithoutResult(transactionStatus -> {
            TaskModel taskModel = new TaskModel();
            try {
                taskModel.setTaskName("测试编程式事务");
                taskModel.setTaskNo("000A");
                taskModel.setStatus(0);
                int insert = taskMapper.insert(taskModel);
                System.out.println("insert:" + insert);
                System.out.println("taskModel:" + taskModel);

                int i = 1 / 0;
            } catch (Exception e) {
                log.error("测试编程式事务异常,手动回滚事务：", e);
                //手动回滚数据
//                taskMapper.deleteById(taskModel);
                //手动回滚事务
                transactionStatus.setRollbackOnly();
            }
        });
    }

}
