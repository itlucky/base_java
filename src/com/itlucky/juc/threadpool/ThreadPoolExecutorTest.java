package com.itlucky.juc.threadpool;

import java.util.concurrent.*;


public class ThreadPoolExecutorTest {

    public static void main(String[] args) {

        System.out.println("当前硬件的CPU核数：" + Runtime.getRuntime().availableProcessors());

        //自定义线程池
       ThreadPoolExecutor threadPool =  new ThreadPoolExecutor(2, //核心线程池大小
                          5, //最大核心线程池大小
                              1, //超时等待时间
                            TimeUnit.SECONDS, //时间单位
            new LinkedBlockingQueue<>(4), //阻塞队列：类似银行的候客区
            Executors.defaultThreadFactory(), //默认线程工厂
            new ThreadPoolExecutor.AbortPolicy() //线程池满了，还有线程进来，不处理这个线程，抛出异常
        );
//        System.out.println("getMaximumPoolSize：" + threadPool.getMaximumPoolSize());
//        System.out.println("getLargestPoolSize：" + threadPool.getLargestPoolSize());
//        System.out.println("getPoolSize：" + threadPool.getPoolSize());
//        System.out.println("getCorePoolSize：" + threadPool.getCorePoolSize());
//        System.out.println("getActiveCount：" + threadPool.getActiveCount());
//        System.out.println("getQueue().size()：" + threadPool.getQueue().size());
//        System.out.println("getCompletedTaskCount：" + threadPool.getCompletedTaskCount());
        try {
            // 最大承载是队列容量+最大核心线程池大小，这里就是3+6=9。queue+max
            // 这里当创建的线程数量超过9，就会抛出拒绝策略的异常：java.util.concurrent.RejectedExecutionException
            // 调测可以发现，当循环5次以内，都是两个线程在处理，+1之后也就是超过了最大核心线程池设置，
            // 会有3个线程处理，依次类推，直到循环次数9时，最多可创建6个线程。超过9就会拒绝策略处理。
            for (int i = 1; i<=19; i++) {
                threadPool.execute(()->{
                    System.out.println(Thread.currentThread().getName() + " 创建ok~");
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            threadPool.shutdown();
        }

    }
}
