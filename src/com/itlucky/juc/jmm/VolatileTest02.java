package com.itlucky.juc.jmm;

import java.util.concurrent.atomic.AtomicInteger;


/**
 * 不保证原子性
 */
public class VolatileTest02 {
    //这里使用关键字volatile，执行结果仍然是<=20000.说明volatile不能保证原子操作。
//    private volatile static int num = 0;
    // volatile不能保证原子性，可以改为juc下的原子操作(CAS)
    private static volatile AtomicInteger num = new AtomicInteger(0);

    private static void add(){
        num.getAndIncrement(); //AtomicInteger的+1方法
    }

    public static void main(String[] args) {
        // 开启20个线程，每个线程都执行加1000次的操作。
        // 理论是哪个最后的结果是20000就是对的。
        for (int i = 1; i<=20; i++) {
            new Thread(()->{
                for (int j = 1; j<=1000; j++) {
                    add();
                }
            }).start();
        }
        //线程数超过2个，说明上面没有执行完. java默认有2个线程：main 和 gc(即：守护线程)
        while (Thread.activeCount() > 2){
            Thread.yield();
        }
        System.out.println(Thread.currentThread().getName() + "==>" + num);
    }
}
