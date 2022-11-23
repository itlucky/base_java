package com.itlucky.juc.threadpool;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


/**
 * Executors:工具类
 * 实际不推荐使用Executors来创建线程池，因为默认容量大小是21亿，会导致OOM
 * 应该用：ThreadPoolExecutor
 */
public class ExecutorsTest {

    public static void main(String[] args) {

//        ExecutorService threadPool = Executors.newSingleThreadExecutor();//单个线程
        //创建一个固定容量的线程池，里面最多只能有入参指定个线程同时执行
//        ExecutorService threadPool = Executors.newFixedThreadPool(5);
        //可伸缩的，只要CPU支持，可以达到最大的并发
        ExecutorService threadPool = Executors.newCachedThreadPool();
        try {
            for (int i = 0; i<100; i++) {
                //使用线程池创建线程
                threadPool.execute(()->{
                    System.out.println(Thread.currentThread().getName() + " 创建ok");
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // 线程池使用完，程序结束，关闭线程池
            threadPool.shutdown();
        }
    }
}
