package com.itlucky.juc.threadpool;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;


public class MyTest {
    public static void main(String[] args) {
        //可用cpu核数
        final int processors = Runtime.getRuntime().availableProcessors();

        ThreadPoolExecutor threadPool = new ThreadPoolExecutor(processors, processors*2, 1, TimeUnit.SECONDS, new LinkedBlockingQueue<>(10),
            new ThreadPoolExecutor.AbortPolicy());

        try {
            for (int i = 1; i<=90; i++) {
                threadPool.execute(()->{
                    // 线程TODO
                    System.out.println(Thread.currentThread().getName() + "--》创建了！");
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            threadPool.shutdown();
        }
    }
}
