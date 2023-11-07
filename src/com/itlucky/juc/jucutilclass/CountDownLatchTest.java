package com.itlucky.juc.jucutilclass;

import java.util.concurrent.CountDownLatch;


/**
 * CountDownLatch:计数器
 * 减法计数器
 */
public class CountDownLatchTest {
    public static void main(String[] args)
        throws InterruptedException {
        //比如学生校门，全部出去就会锁门,然后执行之后的任务。
        //应用：必须要执行任务的时候，使用。
        CountDownLatch countDownLatch = new CountDownLatch(7);

        for (int i=1; i<=7; i++) {
            int finalI = i;
            new Thread(()->{
//                System.out.println(Thread.currentThread().getName() +" Go Out");
                System.out.println("TA_" + finalI + " Compelete");
                countDownLatch.countDown(); // 出去一个，计数器减 1
            },String.valueOf(i)).start();
        }
        countDownLatch.await(); //等待计数器归零，然后再向下执行

        System.out.println("Close Door!");
    }
}
