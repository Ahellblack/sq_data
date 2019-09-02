package com.siti.wisdomhydrologic.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by DC on 2019/7/31.
 *
 * @data ${DATA}-18:55
 */
public class ColorsExecutor {
    private static final Logger logger =LoggerFactory.getLogger(ColorsExecutor.class);
    private ThreadPoolExecutor pool = null;
    /**
     * 线程池初始化方法
     * corePoolSize 核心线程池大小----1  不常用使用0
     * maximumPoolSize 最大线程池大小----3  2N+1         int n = Runtime.getRuntime().availableProcessors() >> 2;
     * keepAliveTime 线程池中超过corePoolSize数目的空闲线程最大存活时间----30+单位TimeUnit
     * TimeUnit keepAliveTime时间单位----TimeUnit.MINUTES
     * workQueue 阻塞队列----new ArrayBlockingQueue<Runnable>(5)====5容量的阻塞队列
     * threadFactory 新建线程工厂----new CustomThreadFactory()====定制的线程工厂
     * rejectedExecutionHandler 当提交任务数超过maxmumPoolSize+workQueue之和时,
     *                          即当提交第41个任务时(前面线程都没有执行完,此测试方法中用sleep(100)),
     *                                任务会交给RejectedExecutionHandler来处理
     */
    public ThreadPoolExecutor init() {
        pool = new ThreadPoolExecutor(
                1,
                2,
                30,
                TimeUnit.MINUTES,
                new ArrayBlockingQueue<Runnable>(3),
                new ColorThreadFactory(),
                new ColorExecutionHandler());
        return pool;
    }

    /**
     * 销毁
     */
    public void leaveGracefully() {
        if(pool != null) {
            pool.shutdown();
        }
    }

    public ThreadPoolExecutor getCustomThreadPoolExecutor() {
        return this.pool;
    }

    private class ColorThreadFactory implements ThreadFactory {
        private AtomicInteger count = new AtomicInteger(0);
        @Override
        public Thread newThread(Runnable r) {
            Thread t = new Thread(r);
            String threadName = ColorsExecutor.class.getSimpleName() + count.addAndGet(1);
            t.setName(threadName);
            logger.info("ColorsExecutor start creating thread :{}",threadName);
            return t;
        }
    }

    private class ColorExecutionHandler implements RejectedExecutionHandler {
        @Override
        public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
            try {
                executor.getQueue().put(r);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

}
