package com.shth.das.util;

import com.shth.das.common.CommonThreadPool;
import lombok.extern.slf4j.Slf4j;
import oshi.SystemInfo;
import oshi.hardware.CentralProcessor;
import oshi.hardware.GlobalMemory;
import oshi.util.Util;

import java.text.DecimalFormat;
import java.util.Properties;

/**
 * @description: 使用Oshi监控系统运行状况
 * @author: Shan Long
 * @create: 2021-09-17
 */
@Slf4j
public class OshiSystemInfo {

    /**
     * 获取所有信息
     */
    public static void getSystemInfoAll() {
        getThreadPoolInfo();
        getCpuInfo();
        getMemoryInfo();
        getJvmInfo();
        getThreadInfo();
    }

    /**
     * 自定义线程池信息
     */
    public static void getThreadPoolInfo() {
        log.info("-----------------------------------------");
        log.info("自定义线程池--->创建过最大线程数：{} -- 当前线程数：{} -- 活跃线程数：{} -- 队列数量：{}",
                CommonThreadPool.THREAD_POOL_EXECUTOR.getLargestPoolSize(), CommonThreadPool.THREAD_POOL_EXECUTOR.getPoolSize(),
                CommonThreadPool.THREAD_POOL_EXECUTOR.getActiveCount(), CommonThreadPool.THREAD_POOL_EXECUTOR.getQueue().size());
        log.info("-----------------------------------------");
    }

    /**
     * 系统CPU信息
     */
    public static void getCpuInfo() {
        SystemInfo systemInfo = new SystemInfo();
        CentralProcessor processor = systemInfo.getHardware().getProcessor();
        long[] prevTicks = processor.getSystemCpuLoadTicks();
        Util.sleep(100);
        long[] ticks = processor.getSystemCpuLoadTicks();
        long nice = ticks[CentralProcessor.TickType.NICE.getIndex()] - prevTicks[CentralProcessor.TickType.NICE.getIndex()];
        long irq = ticks[CentralProcessor.TickType.IRQ.getIndex()] - prevTicks[CentralProcessor.TickType.IRQ.getIndex()];
        long softirq = ticks[CentralProcessor.TickType.SOFTIRQ.getIndex()] - prevTicks[CentralProcessor.TickType.SOFTIRQ.getIndex()];
        long steal = ticks[CentralProcessor.TickType.STEAL.getIndex()] - prevTicks[CentralProcessor.TickType.STEAL.getIndex()];
        long cSys = ticks[CentralProcessor.TickType.SYSTEM.getIndex()] - prevTicks[CentralProcessor.TickType.SYSTEM.getIndex()];
        long user = ticks[CentralProcessor.TickType.USER.getIndex()] - prevTicks[CentralProcessor.TickType.USER.getIndex()];
        long iowait = ticks[CentralProcessor.TickType.IOWAIT.getIndex()] - prevTicks[CentralProcessor.TickType.IOWAIT.getIndex()];
        long idle = ticks[CentralProcessor.TickType.IDLE.getIndex()] - prevTicks[CentralProcessor.TickType.IDLE.getIndex()];
        long totalCpu = user + nice + cSys + idle + iowait + irq + softirq + steal;
        if (totalCpu == 0) {
            return;
        }
        DecimalFormat decimalFormat = new DecimalFormat("#.##%");
        log.info("-----------------------------------------");
        log.info("cpu核数:{}", processor.getLogicalProcessorCount());
        log.info("cpu系统使用率:{}", decimalFormat.format(cSys / totalCpu));
        log.info("cpu用户使用率:{}", decimalFormat.format(user / totalCpu));
        log.info("cpu当前等待率:{}", decimalFormat.format(iowait / totalCpu));
        log.info("cpu当前使用率:{}", decimalFormat.format(1 - (idle / totalCpu)));
        log.info("-----------------------------------------");
    }

    /**
     * 主机内存信息
     */
    public static void getMemoryInfo() {
        SystemInfo systemInfo = new SystemInfo();
        GlobalMemory memory = systemInfo.getHardware().getMemory();
        //总内存
        long totalByte = memory.getTotal();
        //剩余
        long acaliableByte = memory.getAvailable();
        log.info("-----------------------------------------");
        log.info("主机总内存：{}", formatByte(totalByte));
        log.info("使用内存：{}", formatByte(totalByte - acaliableByte));
        log.info("剩余内存：{}", formatByte(acaliableByte));
        log.info("内存使用率：{}", new DecimalFormat("#.##%").format((totalByte - acaliableByte) * 1.0 / totalByte));
        log.info("-----------------------------------------");
    }

    /**
     * JVM内存信息
     */
    public static void getJvmInfo() {
        Properties props = System.getProperties();
        Runtime runtime = Runtime.getRuntime();
        //jvm总内存
        long jvmTotalMemoryByte = runtime.totalMemory();
        //jvm最大可申请
        long jvmMaxMoryByte = runtime.maxMemory();
        //空闲空间
        long freeMemoryByte = runtime.freeMemory();
        //jdk版本
        String jdkVersion = props.getProperty("java.version");
        //jdk路径
        String jdkHome = props.getProperty("java.home");
        log.info("-----------------------------------------");
        log.info("jvm内存总量：{}", formatByte(jvmTotalMemoryByte));
        log.info("jvm已使用内存：{}", formatByte(jvmTotalMemoryByte - freeMemoryByte));
        log.info("jvm剩余内存：{}", formatByte(freeMemoryByte));
        log.info("jvm内存使用率：{}", new DecimalFormat("#.##%").format((jvmTotalMemoryByte - freeMemoryByte) * 1.0 / jvmTotalMemoryByte));
        log.info("-----------------------------------------");
    }

    /**
     * 进程内线程数
     */
    public static void getThreadInfo() {
        ThreadGroup currentGroup = Thread.currentThread().getThreadGroup();
        while (currentGroup.getParent() != null) {
            // 返回此线程组的父线程组
            currentGroup = currentGroup.getParent();
        }
        //此线程组中活动线程的估计数
        int noThreads = currentGroup.activeCount();
        log.info("-----------------------------------------");
        log.info("进程中线程数：{}", noThreads);
        log.info("-----------------------------------------");
    }

    /**
     * 单位换算
     *
     * @param byteNumber
     * @return String
     */
    private static String formatByte(long byteNumber) {
        //换算单位
        final double format = 1024.0;
        final double kbNumber = byteNumber / format;
        if (kbNumber < format) {
            return new DecimalFormat("#.##KB").format(kbNumber);
        }
        double mbNumber = kbNumber / format;
        if (mbNumber < format) {
            return new DecimalFormat("#.##MB").format(mbNumber);
        }
        double gbNumber = mbNumber / format;
        if (gbNumber < format) {
            return new DecimalFormat("#.##GB").format(gbNumber);
        }
        double tbNumber = gbNumber / format;
        return new DecimalFormat("#.##TB").format(tbNumber);
    }
}
