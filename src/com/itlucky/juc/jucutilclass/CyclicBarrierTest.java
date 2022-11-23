package com.itlucky.juc.jucutilclass;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;


/**
 * 实现加法计数器
 * 实现：集齐7颗龙珠召唤神龙
 */
public class CyclicBarrierTest {

    public static void main(String[] args) {

        //CyclicBarrier，参数1是计数的值，参数2是达到计数的值要执行的线程
        CyclicBarrier cyclicBarrier = new CyclicBarrier(7,()->{
            System.out.println("召唤神龙成功!");
        });

        for (int i = 1; i<=7; i++) {
            //注意：这里的lambda表达式里面是直接拿不到i的值，借助中间变量
            final int tmp = i;
            new Thread(()->{
                System.out.println(Thread.currentThread().getName() + "收集了" + tmp+"颗龙珠");
                //await()方法就是等待计数器的值达到指定的值，然后执行cyclicBarrier中的线程
                try {
                    cyclicBarrier.await(); //等待
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (BrokenBarrierException e) {
                    e.printStackTrace();
                }
            }).start();
        }
    }
}
