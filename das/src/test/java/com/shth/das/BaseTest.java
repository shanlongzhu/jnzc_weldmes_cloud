package com.shth.das;

import org.junit.After;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Date;

@SpringBootTest
@RunWith(SpringRunner.class)
public abstract class BaseTest {

    private Date startTime;

    @Before
    public void before() {
        System.out.println();
        System.out.println("==================TEST START==================");
        System.out.println();
        startTime = new Date();
    }

    @After
    public void after() {
        long totalTime = System.currentTimeMillis() - startTime.getTime();
        System.out.println();
        System.out.println("==================TEST END==================");
        System.out.println("==================运行总耗时：" + totalTime + " 毫秒==================");
        System.out.println();
    }

}
