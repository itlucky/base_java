package com.itlucky.juc.bq;

import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.TimeUnit;


/**
 * SynchronousQueue：同步队列
 * 和其他 BlockingQueue不一样，SynchronousQueue 不存储元素
 * put了一个元素，必须从里面先take出来，否则不能put进去值！
 */
public class SynchronousQueueTest {

    public static void main(String[] args) {

        SynchronousQueue<String> synchronousQueue = new SynchronousQueue<>();

        //一个线程执行塞入操作
        new Thread(()->{
            try {
                System.out.println(Thread.currentThread().getName() + " put 1");
                synchronousQueue.put("1");
                System.out.println(Thread.currentThread().getName() + " put 2");
                synchronousQueue.put("2");
                System.out.println(Thread.currentThread().getName() + " put 3");
                synchronousQueue.put("3");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        },"T1").start();

        //一个线程执行取出操作
        new Thread(()->{
            try {
                TimeUnit.SECONDS.sleep(3);
                System.out.println(Thread.currentThread().getName() + " take " + synchronousQueue.take());
                TimeUnit.SECONDS.sleep(3);
                System.out.println(Thread.currentThread().getName() + " take " + synchronousQueue.take());
                TimeUnit.SECONDS.sleep(3);
                System.out.println(Thread.currentThread().getName() + " take " + synchronousQueue.take());

            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        },"T2").start();
    }
}
