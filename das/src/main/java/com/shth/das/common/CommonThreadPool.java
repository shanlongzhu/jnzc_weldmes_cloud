package com.shth.das.common;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 公共线程池
 *
 * @author zsl
 */
@Component
@Slf4j
public class CommonThreadPool {

    /**
     * 执行THREAD_POOL_EXECUTOR多出的任务
     */
    private static final ThreadPoolExecutor CUSTOM_THREAD_POOL = new ThreadPoolExecutor(10, 1000, 30000, TimeUnit.MILLISECONDS,
            new ArrayBlockingQueue<>(10), new ThreadPoolExecutor.DiscardOldestPolicy());

    /**
     * 核心线程：100
     * 最大线程：2000
     * 超时时间：30秒
     */
    public static final ThreadPoolExecutor THREAD_POOL_EXECUTOR = new ThreadPoolExecutor(100, 1000, 30000, TimeUnit.MILLISECONDS,
            new ArrayBlockingQueue<>(10), new CommonThreadPool.CustomRejectedExecutionHandler());


    /**
     * 自定义拒绝策略
     */
    private static class CustomRejectedExecutionHandler implements RejectedExecutionHandler {

        @Override
        public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
            //多出的任务添加的自定义线程池继续处理
            CUSTOM_THREAD_POOL.execute(r);
            //打印线程池日志
            log.warn("[THREAD_POOL_EXECUTOR]-->创建过最大线程数：{} -- 当前线程数：{} -- 活跃线程数：{} -- 队列数量：{}",
                    executor.getLargestPoolSize(), executor.getPoolSize(), executor.getActiveCount(), executor.getQueue().size());
            log.warn("[CUSTOM_THREAD_POOL]-->创建过最大线程数：{} -- 当前线程数：{} -- 活跃线程数：{} -- 队列数量：{}",
                    CUSTOM_THREAD_POOL.getLargestPoolSize(), CUSTOM_THREAD_POOL.getPoolSize(), CUSTOM_THREAD_POOL.getActiveCount(), CUSTOM_THREAD_POOL.getQueue().size());
        }
    }

}
