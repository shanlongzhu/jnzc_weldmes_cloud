package com.shth.das;

import org.junit.After;
import org.junit.Before;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Date;

@SpringBootTest
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
