package com.shth.das.common;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.*;

/**
 * 公共线程池
 *
 * @author zsl
 */
@Slf4j
public class CommonThreadPool {

    /**
     * 执行THREAD_POOL_EXECUTOR多出的任务
     */
    /*public static final ThreadPoolExecutor CUSTOM_THREAD_POOL = new ThreadPoolExecutor(10, 1000, 30000, TimeUnit.MILLISECONDS,
            new ArrayBlockingQueue<>(10), new ThreadPoolExecutor.DiscardOldestPolicy());*/

    /**
     * 使用guava提供的ThreadFactoryBuilder创建线程池
     */
    private static final ThreadFactory threadFactory = new ThreadFactoryBuilder().setNameFormat("das-pool-%d").build();

    /**
     * 核心线程：10
     * 最大线程：200
     * 超时时间：30秒
     * 拒绝策略：由调用线程处理该任务
     */
    private static final ThreadPoolExecutor THREAD_POOL_EXECUTOR = new ThreadPoolExecutor(10, 200, 30000, TimeUnit.MILLISECONDS,
            new LinkedBlockingQueue<>(50), threadFactory, new ThreadPoolExecutor.CallerRunsPolicy());


    public static ThreadPoolExecutor threadPoolExecutor() {
        return THREAD_POOL_EXECUTOR;
    }

    public static void executeTask(Runnable runnable) {
        if (THREAD_POOL_EXECUTOR.isShutdown() || THREAD_POOL_EXECUTOR.isTerminating() || THREAD_POOL_EXECUTOR.isTerminated()) {
            return;
        }
        THREAD_POOL_EXECUTOR.execute(runnable);
    }


    /**
     * 自定义拒绝策略
     */
    private static class CustomRejectedExecutionHandler implements RejectedExecutionHandler {
        @Override
        public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
            int poolSize = executor.getPoolSize();
            int activeCount = executor.getActiveCount();
            log.info("线程池拒绝策略,当前线程数：{},活跃线程数：{}", poolSize, activeCount);
            /*
             * 设置拒绝策略
             * AbortPolicy：丢弃任务并抛出异常
             * DiscardPolicy：丢弃任务，但是不抛出异常
             * DiscardOldestPolicy：丢弃队列最前面的任务，然后重新提交被拒绝的任务
             * CallerRunsPolicy：由调用线程处理该任务
             */
            executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        }
    }

}
