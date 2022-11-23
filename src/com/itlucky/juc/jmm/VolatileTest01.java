package com.itlucky.juc.jmm;

import java.util.concurrent.TimeUnit;


/**
 * 可见性
 */
public class VolatileTest01 {
    //如果不加volatile关键字，会发现main线程执行完了打印num的值为1，而线程t1还会一直在死循环中。
    //加了volatile，就保证了可见性，也就是一旦num被main线程修改了，t1线程也能拿到这个最新值。
    private volatile static int num = 0;
    public static void main(String[] args)
        throws InterruptedException {

        new Thread(()->{
            while (num ==0){

            }
        },"t1").start();

        TimeUnit.SECONDS.sleep(2);
        num = 1;
        System.out.println(num);
    }
}
